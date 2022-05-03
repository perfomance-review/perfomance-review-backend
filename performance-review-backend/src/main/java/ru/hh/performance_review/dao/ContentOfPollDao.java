package ru.hh.performance_review.dao;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.performance_review.dao.base.GenericDao;

@Repository
public class ContentOfPollDao extends GenericDao {
    public ContentOfPollDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}
