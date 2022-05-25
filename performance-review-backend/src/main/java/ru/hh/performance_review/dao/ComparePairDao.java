package ru.hh.performance_review.dao;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.performance_review.dao.base.CommonDao;
import ru.hh.performance_review.dto.WinnerRawDto;
import ru.hh.performance_review.model.ComparePair;

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
}
