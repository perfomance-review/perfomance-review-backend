package ru.hh.performance_review.dao;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.performance_review.dao.base.GenericDao;

@Repository
public class PollDao extends GenericDao {

    public PollDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}
