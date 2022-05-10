package ru.hh.performance_review.dao;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.performance_review.dao.base.CommonDao;

@Repository
public class QuestionDao extends CommonDao {

    public QuestionDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}
