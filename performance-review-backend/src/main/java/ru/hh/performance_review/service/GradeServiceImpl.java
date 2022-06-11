package ru.hh.performance_review.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.performance_review.dao.ComparePairDao;
import ru.hh.performance_review.dao.base.CommonDao;
import ru.hh.performance_review.dto.response.EmptyResponseDto;
import ru.hh.performance_review.dto.response.GradeUserDto;
import ru.hh.performance_review.dto.response.RatingResponseDto;
import ru.hh.performance_review.dto.response.compairofpoll.UserInfoDto;
import ru.hh.performance_review.exception.BusinessServiceException;
import ru.hh.performance_review.exception.InternalErrorCode;
import ru.hh.performance_review.mapper.UserMapper;
import ru.hh.performance_review.model.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class GradeServiceImpl implements GradeService{

    private final ComparePairDao comparePairDao;
    private final CommonDao commonDao;
    private final UserMapper userMapper;

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

       List<ComparePair> results = comparePairDao.getRatingForUserByPollId(UUID.fromString(userId), UUID.fromString(pollId));


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


    @Transactional
    @Override
    public RatingResponseDto countRating(String userId, String pollId) {

        Poll poll = commonDao.getByID(Poll.class, UUID.fromString(pollId));

        if (poll == null) {
            throw new BusinessServiceException(InternalErrorCode.UNKNOWN_POLL, String.format("pollId:%s", pollId));
        }

        if (poll.getDeadline().isAfter(LocalDate.now())) {
            throw new BusinessServiceException(InternalErrorCode.INTERNAL_ERROR,
                    String.format("Опрос %s еще не завершен", pollId));
        }

        List<ComparePair> allResults = comparePairDao.getRatingForAllByPollId(UUID.fromString(pollId));

        List<User> respondents = Stream.concat(allResults.stream().map(ComparePair::getPerson1),
                                               allResults.stream().map(ComparePair::getPerson2))
                                .distinct()
                                .collect(Collectors.toList());

        Map<Question, List<ComparePair>> questionsComparePairs = allResults.stream()
                .collect(Collectors.groupingBy(ComparePair::getQuestion));

        List<RatingResponseDto> resultAllQuestions = new ArrayList<>();

        for (Question question: questionsComparePairs.keySet()) {
            Map<UserInfoDto, Long> resultUserForQuestion = new HashMap<>();
            for (User respondent : respondents) {
                long countWinner = questionsComparePairs.get(question).stream()
                        .filter(x -> x.getWinner().getUserId().equals(respondent.getUserId()))
                        .count();
                long countParticipant = questionsComparePairs.get(question).stream()
                        .filter(x -> (x.getPerson1().getUserId().equals(respondent.getUserId()) || x.getPerson2().getUserId().equals(respondent.getUserId())))
                        .count();

                log.info("countWinner: " + countWinner);
                log.info("countParticipant: " + countParticipant);

                if (countParticipant == 0) {
                    continue;
                }
                long gradeQuestion = 100 * countWinner / countParticipant;
                resultUserForQuestion.put(userMapper.toUserInfoDto(respondent), gradeQuestion);
            }
            resultAllQuestions.add(new RatingResponseDto(question.getText(), question.getCompetence().getText(), resultUserForQuestion));
        }

        return resultAllQuestions.get(0);

//            resultAllQuestions.add(new RatingResponseDto()
//                                       .setTextQuestion(question.getText())
//                                       .setTextCompetence(question.getCompetence().getText())
//                                       .setRatingQuestion(resultUserForQuestion));

    }

}
