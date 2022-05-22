package ru.hh.performance_review.dao;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.performance_review.dao.base.CommonDao;
import ru.hh.performance_review.model.ContentOfPoll;

import java.util.List;
import java.util.UUID;

@Repository
public class ContentOfPollDao extends CommonDao {
    public ContentOfPollDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<ContentOfPoll> getAll() {
        return getSession().createQuery("SELECT c FROM ContentOfPoll c", ContentOfPoll.class).getResultList();
    }

    public List<ContentOfPoll> getByPollId(UUID pollId) {
        return getSession().createQuery("SELECT c " +
                "FROM ContentOfPoll c " +
                "WHERE c.poll.pollId = :pollId", ContentOfPoll.class)
            .setParameter("pollId", pollId)
            .getResultList();
    }
}
