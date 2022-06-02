package ru.hh.performance_review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.performance_review.dao.ContentOfPollDao;
import ru.hh.performance_review.dao.RespondentsOfPollDao;
import ru.hh.performance_review.dao.UserDao;
import ru.hh.performance_review.dao.base.CommonDao;
import ru.hh.performance_review.dto.response.EmptyResponseDto;
import ru.hh.performance_review.exception.BusinessServiceException;
import ru.hh.performance_review.exception.InternalErrorCode;
import ru.hh.performance_review.model.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class StartPollServiceImpl implements StartPollService{

    private final CommonDao commonDao;
    private final RespondentsOfPollDao respondentsOfPollDao;
    private final UserDao userDao;
    private final ContentOfPollDao contentOfPollDao;

    @Override
    @Transactional
    public void saveExcluded(Poll poll, User user, List<UUID> includedIds) {

        userDao.getExcluded(includedIds, user.getUserId()).stream()
                .map(o -> new ExcludedRespondentsOfPoll(UUID.randomUUID(), poll, user, o))
                .forEach(commonDao::save);
    }

    @Override
    @Transactional
    public void saveComparePair(Poll poll, User user, List<UUID> includedIds) {

        List<Question> questions = contentOfPollDao.getQuestions(poll);
        List<User> participants = userDao.getIncluded(includedIds);

        for (Question question : questions) {
            for (int i = 0; i < participants.size(); i++) {
                for (int j = i+1; j < participants.size(); j++) {
                    ComparePair comparePair = new ComparePair(poll, question, participants.get(i), participants.get(j), user);
                    commonDao.save(comparePair);
                }
            }
        }
    }

    @Override
    @Transactional
    public EmptyResponseDto doStartPoll(String pollId, String userId, List<String> includedIdsString) {

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

        respondentsOfPollDao.changeStatusPoll(poll, user, PollStatus.PROGRESS);
        saveExcluded(poll, user, includedIds);
        saveComparePair(poll, user, includedIds);

        return new EmptyResponseDto();

    }

}
