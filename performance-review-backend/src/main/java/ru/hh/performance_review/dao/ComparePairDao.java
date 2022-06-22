package ru.hh.performance_review.dao;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.performance_review.dao.base.CommonDao;
import ru.hh.performance_review.dto.WinnerRawDto;
import ru.hh.performance_review.model.ComparePair;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ComparePairDao extends CommonDao {
    public ComparePairDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Transactional(readOnly = true)
    public Optional<ComparePair> findOptionalByGetRatingRawDto(UUID respondentId, WinnerRawDto ratingRawDto) {
        return getSession().createQuery(
                "SELECT c " +
                        " FROM ComparePair c " +
                        "    WHERE c.poll.pollId = :pollId " +
                        "       AND c.question.questionId = :questionId " +
                        "       AND c.person1.userId = :person1Id " +
                        "       AND c.person2.userId = :person2Id " +
                        "       AND c.respondent.userId = :respondentId " +
                        "", ComparePair.class)
                .setParameter("pollId", ratingRawDto.getPollId())
                .setParameter("questionId", ratingRawDto.getQuestionId())
                .setParameter("person1Id", ratingRawDto.getPerson1Id())
                .setParameter("person2Id", ratingRawDto.getPerson2Id())
                .setParameter("respondentId", respondentId)
                .uniqueResultOptional();
    }

    @Transactional(readOnly = true)
    public List<ComparePair> getRatingForUserByPollId(UUID userId, UUID pollId) {
        return getSession().createQuery(
                        "SELECT c " +
                                " FROM ComparePair c " +
                                "    WHERE c.poll.pollId = :pollId " +
                                "       AND (c.person1.userId = :userId OR c.person2.userId = :userId) " +
                                "       AND c.winner IS NOT NULL", ComparePair.class)
                .setParameter("pollId", pollId)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Transactional(readOnly = true)
    public List<ComparePair> getRatingForAllByPollId(UUID pollId) {
        return getSession().createQuery(
                        "SELECT c " +
                                " FROM ComparePair c " +
                                "    WHERE c.poll.pollId = :pollId ", ComparePair.class)
                .setParameter("pollId", pollId)
                .getResultList();
    }

    @Transactional(readOnly = true)
    public List<ComparePair> getRatingForAllByPollIdPagination(UUID pollId, Integer page) {

        int flagAll = (page == null) ? 1 : 0;

        return getSession().createQuery(
                      "SELECT pair " +
                                " FROM ComparePair pair " +
                                " INNER JOIN ContentOfPoll cop " +
                                " ON pair.poll = cop.poll " +
                                "     AND pair.question = cop.question " +
                                " WHERE " +
                                "     pair.poll.pollId = :pollId " +
                                "     AND pair.winner IS NOT NULL " +
                                "     AND (cop.order = :numQuestion or 1 = :flagAll)", ComparePair.class)
                .setParameter("pollId", pollId)
                .setParameter("numQuestion", page)
                .setParameter("flagAll", flagAll)
                .getResultList();
    }

    public List<ComparePair> findAllUncompletedComparePairsByUserIdAndPollId(UUID userId, UUID pollId) {
        return getSession().createQuery(
                "SELECT c " +
                        " FROM ComparePair c " +
                        "    WHERE c.poll.pollId = :pollId " +
                        "       AND c.respondent.userId = :respondentId " +
                        "       AND c.winner IS NULL " +
                        "", ComparePair.class)
                .setParameter("pollId", pollId)
                .setParameter("respondentId", userId)
                .getResultList();
    }
}
