package ru.hh.performance_review.dao;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.performance_review.dao.base.CommonDao;
import ru.hh.performance_review.model.Poll;
import ru.hh.performance_review.model.PollStatus;
import ru.hh.performance_review.model.RespondentsOfPoll;
import ru.hh.performance_review.model.User;

import java.util.List;
import java.util.Optional;
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

    public List<RespondentsOfPoll> getByPollId(UUID pollId) {
        return getSession()
            .createQuery("SELECT r " +
                "FROM RespondentsOfPoll r " +
                "WHERE r.poll.pollId = :pollId", RespondentsOfPoll.class)
            .setParameter("pollId", pollId)
            .getResultList();
    }

    public Optional<RespondentsOfPoll> findOptionalByRespondentsOfPoll(Poll poll, User respondent) {
        return getSession().createQuery(
                "SELECT r FROM RespondentsOfPoll r " +
                        "   WHERE r.respondent = :respondent " +
                        "       AND r.poll = :poll" +
                        "", RespondentsOfPoll.class)
                .setParameter("respondent", respondent)
                .setParameter("poll", poll)
                .uniqueResultOptional();
    }

    /**
     * метод обновления статуса опроса
     */
    public void changeStatusPoll(Poll poll, User user, PollStatus status) {
        findOptionalByRespondentsOfPoll(poll, user)
                .ifPresent(respondentsOfPoll -> {
                    respondentsOfPoll.setStatus(status);
                    update(respondentsOfPoll);
                });
    }

    public List<RespondentsOfPoll> getByPollIds(List<UUID> pollIds) {
        return getSession()
            .createQuery("SELECT r " +
                "FROM RespondentsOfPoll r " +
                "WHERE r.poll.pollId IN :pollIds", RespondentsOfPoll.class)
            .setParameterList("pollIds", pollIds)
            .getResultList();
    }

    public List<RespondentsOfPoll> getByUserIdWithActiveStatus(UUID userId) {
        List<PollStatus> activeStatus = List.of(PollStatus.OPEN, PollStatus.PROGRESS);
        return getSession()
            .createQuery("SELECT r " +
                "FROM RespondentsOfPoll r " +
                "WHERE r.respondent.userId = :userId AND r.status IN :status", RespondentsOfPoll.class)
            .setParameter("userId", userId)
            .setParameterList("status", activeStatus)
            .getResultList();
    }

    public Optional<RespondentsOfPoll> findOptionalByRespondentIdAndPollId(UUID userId, UUID pollId) {
        return getSession().createQuery(
                "SELECT r FROM RespondentsOfPoll r " +
                        "   WHERE r.respondent.userId = :userId " +
                        "       AND r.poll.pollId = :pollId" +
                        "", RespondentsOfPoll.class)
                .setParameter("userId", userId)
                .setParameter("pollId", pollId)
                .uniqueResultOptional();
    }
}
