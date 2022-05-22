package ru.hh.performance_review.dao;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.performance_review.dao.base.CommonDao;
import ru.hh.performance_review.model.Poll;
import ru.hh.performance_review.model.RespondentsOfPoll;
import ru.hh.performance_review.model.User;

import java.util.List;
import java.util.UUID;

@Repository
public class RespondentsOfPollDao extends CommonDao {
    public RespondentsOfPollDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<RespondentsOfPoll> getAll() {
        return getSession()
            .createQuery("SELECT r " +
                "FROM RespondentsOfPoll r", RespondentsOfPoll.class).getResultList();
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

    public List<RespondentsOfPoll> getByPollId(UUID pollId) {
        return getSession()
            .createQuery("SELECT r " +
                "FROM RespondentsOfPoll r " +
                "WHERE r.poll.pollId = :pollId", RespondentsOfPoll.class)
            .setParameter("pollId", pollId)
            .getResultList();
    }
}
