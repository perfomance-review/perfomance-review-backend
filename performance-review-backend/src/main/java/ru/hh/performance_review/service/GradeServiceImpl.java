package ru.hh.performance_review.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.performance_review.dao.ComparePairDao;
import ru.hh.performance_review.dao.base.CommonDao;
import ru.hh.performance_review.dto.response.GradeUserDto;
import ru.hh.performance_review.exception.BusinessServiceException;
import ru.hh.performance_review.exception.InternalErrorCode;
import ru.hh.performance_review.model.ComparePair;
import ru.hh.performance_review.model.Competence;
import ru.hh.performance_review.model.Poll;
import ru.hh.performance_review.model.Question;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService{

    private final ComparePairDao comparePairDao;
    private final CommonDao commonDao;

    @Transactional
    @Override
    public GradeUserDto countGrade(String userId, String pollId) {

       Poll poll = commonDao.getByID(Poll.class, UUID.fromString(pollId));

       if (poll == null) {
           throw new BusinessServiceException(InternalErrorCode.UNKNOWN_POLL, String.format("pollId:%s", pollId));
       }

       if (poll.getDeadline().isAfter(LocalDate.now())) {
           throw new BusinessServiceException(InternalErrorCode.INTERNAL_ERROR,
                   String.format("Опрос %s еще не завершен", pollId));
       }

       List<ComparePair> results = comparePairDao.findOptionalGetRatingForUser(UUID.fromString(userId), UUID.fromString(pollId));


       Map<Question, List<ComparePair>> questionsComparePairs = results.stream()
               .collect(Collectors.groupingBy(ComparePair::getQuestion));

       Map<String, Long> resultQuestion = new HashMap<>();

       for (Question question: questionsComparePairs.keySet()) {
           long countWinner = questionsComparePairs.get(question).stream()
                   .filter(x -> x.getWinner().getUserId().equals(UUID.fromString(userId)))
                   .count();
           double gradeQuestion = (9.0 * countWinner / questionsComparePairs.get(question).size()) + 1;
           resultQuestion.put(question.getText(), Math.round(gradeQuestion));
       }


       Map<Competence, List<ComparePair>> competencesComparePairs = results.stream()
               .filter(x -> Objects.nonNull(x.getQuestion().getCompetence()))
               .collect(Collectors.groupingBy(x -> x.getQuestion().getCompetence()));

       Map<String, Long> resultCompetence = new HashMap<>();

       for (Competence competence: competencesComparePairs.keySet()) {
           long countWinner = competencesComparePairs.get(competence).stream()
                   .filter(x -> x.getWinner().getUserId().equals(UUID.fromString(userId)))
                   .count();
           double gradeCompetence = (9.0 * countWinner / competencesComparePairs.get(competence).size()) + 1;
           resultCompetence.put(competence.getText(), Math.round(gradeCompetence));
       }

       return new GradeUserDto(resultQuestion, resultCompetence);
    }

}
