package ru.hh.performance_review.dao;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.performance_review.dao.base.CommonDao;

@Repository
public class CompetenceDao extends CommonDao {
    public CompetenceDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}
