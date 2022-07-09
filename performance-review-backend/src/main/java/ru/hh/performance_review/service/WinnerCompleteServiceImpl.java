package ru.hh.performance_review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hh.performance_review.dao.ComparePairDao;
import ru.hh.performance_review.dao.RespondentsOfPollDao;
import ru.hh.performance_review.dto.WinnerRawDto;
import ru.hh.performance_review.dto.request.UpdateWinnerRequestDto;
import ru.hh.performance_review.dto.response.EmptyResponseDto;
import ru.hh.performance_review.dto.response.ResponseMessage;
import ru.hh.performance_review.exception.BusinessServiceException;
import ru.hh.performance_review.exception.InternalErrorCode;
import ru.hh.performance_review.mapper.GetWinnerMapper;
import ru.hh.performance_review.model.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WinnerCompleteServiceImpl implements WinnerCompleteService {

    private final RespondentsOfPollDao respondentsOfPollDao;
    private final ComparePairDao comparePairDao;
    private final GetWinnerMapper getWinnerMapper;


    @Transactional
    @Override
    public ResponseMessage updateWinner(String userId, UpdateWinnerRequestDto requestDto) {
        WinnerRawDto ratingRawDto = getWinnerMapper.toGetRatingRawDto(requestDto);
        UUID respondentId = UUID.fromString(userId);
        Optional<ComparePair> comparePairOptional = comparePairDao.findOptionalByGetRatingRawDto(respondentId, ratingRawDto);

        if (comparePairOptional.isEmpty()) {
            throw new BusinessServiceException(InternalErrorCode.INTERNAL_ERROR,
                    String.format("Не найдена запись в таблице compare_pair с user-id:%s данными %s", respondentId, requestDto));
        }
        ComparePair comparePair = comparePairOptional.get();
        User winner = getWinner(ratingRawDto, comparePair, respondentId);
        comparePair.setWinner(winner);
        comparePairDao.update(comparePair);

        if (Boolean.parseBoolean(requestDto.getIsCompleted())) {
            completedPollByRespondent(comparePair.getPoll(), comparePair.getRespondent());
            closedPoll(comparePair.getPoll().getPollId());
        }

        return new EmptyResponseDto();
    }

    /**
     * Метод закрывает опрос, если у всех респондентов статус = COMPLETED
     *
     * @param pollId       - id опроса
     */
    private void closedPoll(UUID pollId) {
        List<RespondentsOfPoll> respondentsOfPollList = respondentsOfPollDao.getByPollId(pollId);
        boolean isCompleted = respondentsOfPollList.stream()
                .allMatch(x -> x.getStatus().equals(PollStatus.COMPLETED));
        if (isCompleted) {
            respondentsOfPollList.forEach(respondentsOfPoll -> {
                        respondentsOfPoll.setStatus(PollStatus.CLOSED);
                        respondentsOfPollDao.update(respondentsOfPoll);
                    });
        }
    }

    /**
     * Метод проверяет есть ли у респондента пары для определения победителя
     *
     * @param poll       - опрос
     * @param respondent - респондент
     */
    private void completedPollByRespondent(Poll poll, User respondent) {
        respondentsOfPollDao
                .findOptionalByRespondentsOfPoll(poll, respondent)
                .ifPresent(respondentsOfPoll -> {
                    respondentsOfPoll.setStatus(PollStatus.COMPLETED);
                    respondentsOfPollDao.update(respondentsOfPoll);
                });
    }

    /**
     * Метод получения победителя из двух участников
     *
     * @param ratingRawDto
     * @param comparePair
     * @return
     */
    private User getWinner(WinnerRawDto ratingRawDto, ComparePair comparePair, UUID respondentId) {
        UUID winnerId = ratingRawDto.getWinnerId();
        UUID person1Id = ratingRawDto.getPerson1Id();
        UUID person2Id = ratingRawDto.getPerson2Id();
        if (person1Id.equals(winnerId)) {
            return comparePair.getPerson1();
        }
        if (person2Id.equals(winnerId)) {
            return comparePair.getPerson2();
        }

        throw new BusinessServiceException(InternalErrorCode.INTERNAL_ERROR,
                String.format("Неизвестный winner_id:%s у respondentId:%s", winnerId, respondentId));
    }
}
