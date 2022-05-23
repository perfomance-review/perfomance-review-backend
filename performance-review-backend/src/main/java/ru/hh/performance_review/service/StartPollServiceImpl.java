package ru.hh.performance_review.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.control.MappingControl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.performance_review.dao.ContentOfPollDao;
import ru.hh.performance_review.dao.RespondentsOfPollDao;
import ru.hh.performance_review.dao.UserDao;
import ru.hh.performance_review.dao.base.CommonDao;
import ru.hh.performance_review.model.*;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class StartPollServiceImpl implements StartPollService{

    private final CommonDao commonDao;
    private final RespondentsOfPollDao respondentsOfPollDao;
    private final UserDao userDao;
    private final ContentOfPollDao contentOfPollDao;

    @Override
    @Transactional
    public void changeStatusPoll(UUID pollId, UUID userId, PollStatus status) {
        Poll poll = commonDao.getByID(Poll.class, pollId);
        User user = commonDao.getByID(User.class, userId);
        RespondentsOfPoll respondentsOfPoll = respondentsOfPollDao.getRespondentsOfPoll(poll, user);
        respondentsOfPoll.setStatus(status);
        commonDao.update(respondentsOfPoll);
    }

    @Override
    @Transactional
    public void saveExcluded(UUID pollId, UUID userId, List<UUID> includedIds) {

        Poll poll = commonDao.getByID(Poll.class,pollId);
        User currentUser = commonDao.getByID(User.class, userId);

        userDao.getExcluded(includedIds, userId).stream()
                .map(o -> new ExcludedRespondentsOfPoll(UUID.randomUUID(), poll, currentUser, o))
                .forEach(commonDao::saveOrUpdate);
    }

    @Override
    @Transactional
    public void saveComparePair(UUID pollId, UUID userId, List<UUID> includedIds) {

        Poll poll = commonDao.getByID(Poll.class, pollId);
        User user = commonDao.getByID(User.class, userId);
        List<Question> questions = contentOfPollDao.getQuestions(poll);
        List<User> participants = userDao.getIncluded(includedIds);

        for (Question question : questions) {
            for (int i = 0; i < participants.size(); i++) {
                for (int j = i+1; j < participants.size(); j++) {
                    ComparePair comparePair = new ComparePair(poll, question,participants.get(i), participants.get(j), user);
                    commonDao.saveOrUpdate(comparePair);
                }
            }
        }


    }

}
