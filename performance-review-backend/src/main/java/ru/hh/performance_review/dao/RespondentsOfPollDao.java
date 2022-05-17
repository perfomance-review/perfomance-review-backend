package ru.hh.performance_review.dao;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.performance_review.dao.base.CommonDao;
import ru.hh.performance_review.model.RespondentsOfPoll;

import java.util.List;

@Repository
public class RespondentsOfPollDao extends CommonDao {
    public RespondentsOfPollDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

  public List<RespondentsOfPoll> getAll() {
    return getSession().createQuery("SELECT r FROM RespondentsOfPoll r", RespondentsOfPoll.class).getResultList();
  }
}
