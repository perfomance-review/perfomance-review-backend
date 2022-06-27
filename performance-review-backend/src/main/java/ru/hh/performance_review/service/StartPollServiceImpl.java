package ru.hh.performance_review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.performance_review.dao.ContentOfPollDao;
import ru.hh.performance_review.dao.RespondentsOfPollDao;
import ru.hh.performance_review.dao.UserDao;
import ru.hh.performance_review.dao.base.CommonDao;
import ru.hh.performance_review.dto.response.PollProgressDto;
import ru.hh.performance_review.exception.BusinessServiceException;
import ru.hh.performance_review.exception.InternalErrorCode;
import ru.hh.performance_review.mapper.PollMapper;
import ru.hh.performance_review.model.*;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class StartPollServiceImpl implements StartPollService{

    private final CommonDao commonDao;
    private final RespondentsOfPollDao respondentsOfPollDao;
    private final UserDao userDao;
    private final ContentOfPollDao contentOfPollDao;
    private final PollMapper pollMapper;
    private static final Random random = new Random();


    @Transactional
    private void saveExcluded(Poll poll, User user, List<UUID> includedIds) {

        userDao.getExcluded(includedIds, user.getUserId()).stream()
                .map(o -> new ExcludedRespondentsOfPoll(UUID.randomUUID(), poll, user, o))
                .forEach(commonDao::save);
    }

    @Transactional
    private void saveComparePair(Poll poll, User user, List<UUID> includedIds) {

        List<Question> questions = contentOfPollDao.getQuestions(poll);
        List<User> participants = userDao.getIncluded(includedIds);

        ComparePair comparePair;

        for (Question question : questions) {
            for (int i = 0; i < participants.size(); i++) {
                for (int j = i+1; j < participants.size(); j++) {
                    if (random.nextBoolean()) {
                        comparePair = new ComparePair(poll, question, participants.get(i), participants.get(j), user);
                    }
                    else {
                        comparePair = new ComparePair(poll, question, participants.get(j), participants.get(i), user);
                    }
                    commonDao.save(comparePair);
                }
            }
        }
    }

    @Override
    @Transactional
    public PollProgressDto doStartPoll(String pollId, String userId, List<String> includedIdsString) {

        User user = commonDao.getByID(User.class, UUID.fromString(userId));
        if (user == null) {
            throw new BusinessServiceException(InternalErrorCode.UNKNOWN_USER, String.format("userId:%s", userId));
        }

        Poll poll = commonDao.getByID(Poll.class, UUID.fromString(pollId));
        if (poll == null) {
            throw new BusinessServiceException(InternalErrorCode.UNKNOWN_POLL, String.format("pollId:%s", pollId));
        }

        List<UUID> includedIds = includedIdsString.stream()
                        .map(UUID::fromString)
                        .collect(Collectors.toList());

        PollStatus newStatus = changeStatusPoll(poll, user);
        saveExcluded(poll, user, includedIds);
        saveComparePair(poll, user, includedIds);

        return pollMapper.toPollProgressDto(poll, newStatus);

    }

    @Transactional
    private PollStatus changeStatusPoll(Poll poll, User user) {
        Optional<RespondentsOfPoll> respondentsOfPollOptional = respondentsOfPollDao.findOptionalByRespondentsOfPoll(poll, user);

        if (respondentsOfPollOptional.isEmpty()) {
            throw new BusinessServiceException(InternalErrorCode.INTERNAL_ERROR,
                    String.format("Не найдена запись в таблице respondents_of_poll с user-id:%s и poll_id %s", user.getUserId(), poll.getPollId()));
        }

        RespondentsOfPoll respondentsOfPoll  = respondentsOfPollOptional.get();
        if (!respondentsOfPoll.getStatus().equals(PollStatus.OPEN)) {
            throw new BusinessServiceException(InternalErrorCode.INTERNAL_ERROR,
                    String.format("Для старта опрос с poll_id %s для user-id:%s должен иметь статус %s", poll.getPollId(), user.getUserId(), PollStatus.OPEN));
        }

        respondentsOfPoll.setStatus(PollStatus.PROGRESS);
        respondentsOfPollDao.update(respondentsOfPoll);

        return respondentsOfPoll.getStatus();
    }

}
