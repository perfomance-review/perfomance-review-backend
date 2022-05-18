package ru.hh.performance_review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.performance_review.dao.RespondentsOfPollDao;
import ru.hh.performance_review.dao.UserDao;
import ru.hh.performance_review.dao.base.CommonDao;
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

    @Override
    @Transactional
    public void changeStatusPoll(String pollId, String userId, PollStatus status) {
        Poll poll = commonDao.getByID(Poll.class, UUID.fromString(pollId));
        User user = commonDao.getByID(User.class, UUID.fromString(userId));
        RespondentsOfPoll respondentsOfPoll = respondentsOfPollDao.getRespondentsOfPoll(poll, user);
        respondentsOfPoll.setStatus(status);
        commonDao.update(respondentsOfPoll);
    }

    @Override
    @Transactional
    public void saveExcluded(String pollId, String userId, List<String> includedIdsString) {

        if (includedIdsString == null) {
            return;
        }

        List<UUID> includedIds = includedIdsString.stream()
                .map(UUID::fromString)
                .collect(Collectors.toList());

        Poll poll = commonDao.getByID(Poll.class, UUID.fromString(pollId));
        User currentUser = commonDao.getByID(User.class, UUID.fromString(userId));

        userDao.getExcluded(includedIds, UUID.fromString(userId)).stream()
                .map(o -> new ExcludedRespondentsOfPoll(UUID.randomUUID(), poll, currentUser, o))
                .forEach(commonDao::save);
    }

}
