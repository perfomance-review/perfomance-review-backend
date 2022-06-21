package ru.hh.performance_review.dao;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.performance_review.dao.base.CommonDao;
import ru.hh.performance_review.model.RoleEnum;
import ru.hh.performance_review.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public class UserDao extends CommonDao {

    public UserDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Transactional(readOnly = true)
    public List<User> getAll() {
        return getSession().createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    public List<User> getExcluded(List<UUID> includedIds, UUID currentUserId) {

        return getSession().createQuery("SELECT u " +
                                           "FROM User u " +
                                           "WHERE u.role = :paramRole AND u.id != :paramCurrentUser " +
                        "                      AND u.id not in (:paramParticipantsIds)", User.class )
                .setParameter("paramRole", RoleEnum.RESPONDENT)
                .setParameter("paramCurrentUser" , currentUserId)
                .setParameterList("paramParticipantsIds", includedIds)
                .getResultList();
    }

    public List<User> getIncluded(List<UUID> includedIds) {

        return getSession().createQuery("SELECT u " +
                                                  "FROM User u " +
                                                  "WHERE u.id in (:paramParticipantsIds)", User.class )
                .setParameterList("paramParticipantsIds", includedIds)
                .getResultList();
    }

    @Transactional(readOnly = true)
    public Optional<User> findByUserEmailAndUserPassword(String userEmail, String userPassword) {
        return getSession().createQuery(
                "SELECT u FROM User u " +
                        " WHERE u.email = :userEmail " +
                        "   AND u.password = :userPassword" +
                        "", User.class)
                .setParameter("userEmail", userEmail)
                .setParameter("userPassword", userPassword)
                .uniqueResultOptional();
    }

    @Transactional(readOnly = true)
    public Optional<User> findByUserNameAndUserPassword(String username, String userPassword) {
        return getSession().createQuery(
                "SELECT u FROM User u " +
                        " WHERE u.firstName = :username " +
                        "   AND u.password = :userPassword" +
                        "", User.class)
                .setParameter("username", username)
                .setParameter("userPassword", userPassword)
                .uniqueResultOptional();
    }

    @Transactional(readOnly = true)
    public Optional<User> findByUserEmail(String userEmail) {
        return getSession().createQuery(
                "SELECT u FROM User u " +
                        " WHERE u.email = :userEmail " +
                        "", User.class)
                .setParameter("userEmail", userEmail)
                .uniqueResultOptional();
    }
}
