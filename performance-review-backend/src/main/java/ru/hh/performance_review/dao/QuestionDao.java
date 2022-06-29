package ru.hh.performance_review.dao;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.performance_review.dao.base.CommonDao;
import ru.hh.performance_review.model.Question;
import ru.hh.performance_review.model.User;

import java.util.List;
import java.util.UUID;

@Repository
public class QuestionDao extends CommonDao {

    public QuestionDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<Question> getQuestionsByUserId(UUID userId) {
        return getSession().createQuery("SELECT q FROM Question q " +
                                                  "WHERE q.manager IS NULL OR q.manager.userId = :userId", Question.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public List<Question> getAllByIds(List<UUID> ids) {
        return getSession().createQuery("SELECT q " +
                "FROM Question q " +
                "WHERE q.questionId in (:ids)", Question.class )
            .setParameterList("ids", ids)
            .getResultList();
    }
}
