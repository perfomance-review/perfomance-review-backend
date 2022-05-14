package ru.hh.performance_review.dao;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.performance_review.dao.base.CommonDao;
import ru.hh.performance_review.model.RespondentsOfPoll;
import ru.hh.performance_review.model.User;

import java.util.List;

@Repository
public class UserDao extends CommonDao {

    public UserDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<User> getAll() {
        return getSession().createQuery("SELECT u FROM User u", User.class).getResultList();
    }
}
