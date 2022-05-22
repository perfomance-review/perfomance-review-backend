package ru.hh.performance_review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hh.performance_review.dao.ContentOfPollDao;
import ru.hh.performance_review.dao.PollDao;
import ru.hh.performance_review.dao.RespondentsOfPollDao;
import ru.hh.performance_review.dto.GetPollResponseDto;
import ru.hh.performance_review.dto.UserPollByIdResponseDto;
import ru.hh.performance_review.dto.response.PollByIdResponseDto;
import ru.hh.performance_review.mapper.PollMapper;
import ru.hh.performance_review.mapper.UserMapper;
import ru.hh.performance_review.model.ContentOfPoll;
import ru.hh.performance_review.model.Poll;
import ru.hh.performance_review.model.PollStatus;
import ru.hh.performance_review.model.RespondentsOfPoll;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PollServiceImpl implements PollService {
    private final RespondentsOfPollDao respondentsOfPollDao;
    private final ContentOfPollDao contentOfPollDao;
    private final PollDao pollDao;
    private final PollMapper pollMapper;
    private final UserMapper userMapper;

    @Override
    public List<GetPollResponseDto> getPolls(String userId) {
        List<RespondentsOfPoll> respondents = respondentsOfPollDao.getAll();
        List<ContentOfPoll> content = contentOfPollDao.getAll();

        Map<Poll, PollStatus> pollsStatus = respondents.stream()
            .filter(o -> o.getRespondent().getUserId().equals(UUID.fromString(userId)))
            .collect(Collectors.toMap(RespondentsOfPoll::getPoll, RespondentsOfPoll::getStatus));

        List<GetPollResponseDto> polls = new ArrayList<>();

        for (Poll poll : pollsStatus.keySet()) {

            long respondentsCount = respondents.stream()
                .filter(o -> o.getPoll().equals(poll))
                .count();

            long questionsCount = content.stream()
                .filter(o -> o.getPoll().equals(poll))
                .count();

            polls.add(pollMapper.toGetPollResponseDto(poll, respondentsCount, questionsCount, pollsStatus.get(poll)));

        }

        return polls;
    }

    @Override
    public PollByIdResponseDto getPollById(final String pollId) {
        UUID uuid = UUID.fromString(pollId);
        Poll poll = pollDao.getByID(Poll.class, uuid);
        List<UserPollByIdResponseDto> respondents = respondentsOfPollDao.getByPollId(uuid)
            .stream()
            .map(o -> userMapper.toUserPollByIdResponseDto(o.getRespondent()))
            .collect(Collectors.toList());
        List<ContentOfPoll> content = contentOfPollDao.getByPollId(uuid);

        return pollMapper.toPollByIdResponseDto(poll, content.size(), respondents);
    }
}
