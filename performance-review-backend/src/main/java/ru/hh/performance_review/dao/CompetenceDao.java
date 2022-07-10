package ru.hh.performance_review.dao;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.performance_review.dao.base.CommonDao;
import ru.hh.performance_review.model.Competence;

import java.util.List;

@Repository
public class CompetenceDao extends CommonDao {
    public CompetenceDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Transactional(readOnly = true)
    public List<Competence> getAll(){

        return getSession().createQuery("SELECT c FROM Competence c", Competence.class)
                .getResultList();
    }
}
