package ru.hh.performance_review.dao.base;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

// GenericDao нужно для предоставления общих методов для работы с сущностями, например, можно описать
// методы get или save, которые не часто будут различаться.

@Repository
public class GenericDao {

  private final SessionFactory sessionFactory;

  public GenericDao(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  protected Session getSession() {
    return sessionFactory.getCurrentSession();
  }

  public <T> T getByID(Class<T> clazz, Serializable id) {
    return getSession().get(clazz, id);
  }

  public void save(Object object) {
    if (object == null) {
      return;
    }
    getSession().save(object);
  }

  public void update(Object object) {
    if (object == null) {
      return;
    }
    getSession().update(object);
  }

  public void delete(Object object) {
    if (object == null) {
      return;
    }
    getSession().delete(object);
  }

  public void persist(Object object) {
    if (object == null) {
      return;
    }
    getSession().persist(object);
  }

  public void saveOrUpdate(Object object) {
    if (object == null) {
      return;
    }
    getSession().saveOrUpdate(object);
  }

}
