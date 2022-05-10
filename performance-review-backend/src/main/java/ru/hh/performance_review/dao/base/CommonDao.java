package ru.hh.performance_review.dao.base;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.performance_review.model.base.AbstractAuditableBaseEntity;

import java.io.Serializable;


/**
 * CommonDao нужно для предоставления общих методов для работы с сущностями, например, можно описать
 * методы get или save, которые не часто будут различаться.
 */
@Repository
public class CommonDao {

    private final SessionFactory sessionFactory;

    public CommonDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Transactional
    public <T> T getByID(Class<T> clazz, Serializable id) {
        return getSession().get(clazz, id);
    }

    @Transactional
    public void save(AbstractAuditableBaseEntity baseEntity) {
        if (baseEntity == null) {
            return;
        }
        getSession().save(baseEntity);
    }

    @Transactional
    public void update(AbstractAuditableBaseEntity baseEntity) {
        if (baseEntity == null) {
            return;
        }
        getSession().update(baseEntity);
    }

    @Transactional
    public void delete(AbstractAuditableBaseEntity baseEntity) {
        if (baseEntity == null) {
            return;
        }
        getSession().delete(baseEntity);
    }

    @Transactional
    public void persist(AbstractAuditableBaseEntity baseEntity) {
        if (baseEntity == null) {
            return;
        }
        getSession().persist(baseEntity);
    }

    @Transactional
    public void saveOrUpdate(AbstractAuditableBaseEntity baseEntity) {
        if (baseEntity == null) {
            return;
        }
        getSession().saveOrUpdate(baseEntity);
    }

}
