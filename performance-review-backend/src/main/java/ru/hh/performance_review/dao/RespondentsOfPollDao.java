package ru.hh.performance_review.dao;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.performance_review.dao.base.CommonDao;
import ru.hh.performance_review.model.Poll;
import ru.hh.performance_review.model.RespondentsOfPoll;
import ru.hh.performance_review.model.User;

@Repository
public class RespondentsOfPollDao extends CommonDao {
    public RespondentsOfPollDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public RespondentsOfPoll getRespondentsOfPoll(Poll poll, User user) {
        return getSession()
                .createQuery("SELECT rop " +
                               "FROM RespondentsOfPoll rop " +
                               "WHERE rop.respondent = :paramUser AND rop.poll = :paramPoll", RespondentsOfPoll.class)
                .setParameter("paramUser", user)
                .setParameter("paramPoll", poll)
                .getSingleResult();
    }
}
