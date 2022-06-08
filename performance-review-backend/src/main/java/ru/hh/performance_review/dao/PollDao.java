package ru.hh.performance_review.dao;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.performance_review.dao.base.CommonDao;
import ru.hh.performance_review.model.Poll;
import ru.hh.performance_review.model.RespondentsOfPoll;

import java.time.LocalDate;
import java.util.List;

@Repository
public class PollDao extends CommonDao {

    public PollDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

   public List<Poll> findByDeadlineLessThan(LocalDate date) {
        return getSession()
            .createQuery("SELECT p " +
                "FROM Poll p WHERE p.deadline < :date", Poll.class)
            .setParameter("date", date)
            .getResultList();
    }
}
