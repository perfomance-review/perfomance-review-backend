package ru.hh.performance_review.dao;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.performance_review.dao.base.CommonDao;
import ru.hh.performance_review.model.RoleEnum;
import ru.hh.performance_review.model.User;

import java.util.List;
import java.util.UUID;
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

    public List<User> getExcluded(List<UUID> participantsIds, UUID currentUserId) {

        return getSession().createQuery("SELECT u " +
                                           "FROM User u " +
                                           "WHERE u.role = :paramRole AND u.id != :paramCurrentUser " +
                        "                      AND u.id not in (:paramParticipantsIds)", User.class )
                .setParameter("paramRole", RoleEnum.RESPONDENT)
                .setParameter("paramCurrentUser" , currentUserId)
                .setParameterList("paramParticipantsIds", participantsIds)
                .getResultList();
    }
}
