package ru.hh.performance_review.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.performance_review.dao.ComparePairDao;
import ru.hh.performance_review.dao.base.CommonDao;
import ru.hh.performance_review.dto.RatingQuestionDto;
import ru.hh.performance_review.dto.UserWithScoreDto;
import ru.hh.performance_review.dto.response.GradeUserDto;
import ru.hh.performance_review.dto.response.RatingResponseDto;
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
        
        checkPollStatus(pollId);
        
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

        // TODO: 11.06.2022  Убрать проверку статуса опроса в отдельный метод
        // TODO: 11.06.2022 Делать проверку на то, что запрос принадлежит этому менеджеру???

        checkPollStatus(pollId);
        
        List<ComparePair> allResults = comparePairDao.getRatingForAllByPollId(UUID.fromString(pollId));  // все записи compare_pair по опросу

        List<User> respondents = Stream.concat(allResults.stream().map(ComparePair::getPerson1),        // все участники (реальные) опроса
                        allResults.stream().map(ComparePair::getPerson2))
                .distinct()
                .collect(Collectors.toList());

        Map<Question, List<ComparePair>> questionsComparePairs = allResults.stream()        // все записи compare_pair в разрезе вопросов
                .collect(Collectors.groupingBy(ComparePair::getQuestion));

        List<RatingQuestionDto> resultAllQuestions = new ArrayList<>();         // результаты по все вопросам

        for (Question question: questionsComparePairs.keySet()) {

            List<UserWithScoreDto> resultQuestion = new ArrayList<>();         // результаты по 1 вопросу

            for (User respondent : respondents) {

                long countWinner = questionsComparePairs.get(question).stream()
                        .filter(x -> x.getWinner().getUserId().equals(respondent.getUserId()))
                        .count();
                long countParticipant = questionsComparePairs.get(question).stream()
                        .filter(x -> (x.getPerson1().getUserId().equals(respondent.getUserId()) || x.getPerson2().getUserId().equals(respondent.getUserId())))
                        .count();

                if (countParticipant == 0) {
                    // TODO: 12.06.2022 Исключение??? 
                    continue;
                }

                long gradeQuestion = 100 * countWinner / countParticipant;

                resultQuestion.add(new UserWithScoreDto(userMapper.toUserInfoDto(respondent), gradeQuestion));
            }
            resultQuestion = resultQuestion.stream()
                    .sorted(Comparator.comparing(UserWithScoreDto::getScore).reversed())
                    .collect(Collectors.toList());

            resultAllQuestions.add(new RatingQuestionDto(question.getText(), question.getCompetence().getText(), resultQuestion));
        }

        return new RatingResponseDto(resultAllQuestions);

    }
    
    private void checkPollStatus(String pollId) {
        
        Poll poll = commonDao.getByID(Poll.class, UUID.fromString(pollId));

        if (poll == null) {
            throw new BusinessServiceException(InternalErrorCode.UNKNOWN_POLL, String.format("pollId:%s", pollId));
        }

        if (poll.getDeadline().isAfter(LocalDate.now())) {
            throw new BusinessServiceException(InternalErrorCode.INTERNAL_ERROR,
                    String.format("Опрос %s еще не завершен", pollId));
        }
    }
    
}
