package ru.hh.performance_review.dao;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.performance_review.dao.base.GenericDao;

@Repository
public class ExcludedRespondentsOfPollDao extends GenericDao {
    public ExcludedRespondentsOfPollDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}
