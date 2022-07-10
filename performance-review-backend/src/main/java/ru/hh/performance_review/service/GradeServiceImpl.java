package ru.hh.performance_review.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.hh.performance_review.dao.ComparePairDao;
import ru.hh.performance_review.dao.ContentOfPollDao;
import ru.hh.performance_review.dao.RespondentsOfPollDao;
import ru.hh.performance_review.dao.base.CommonDao;
import ru.hh.performance_review.dto.RatingQuestionDto;
import ru.hh.performance_review.dto.ResultCompetenceDto;
import ru.hh.performance_review.dto.ResultQuestionDto;
import ru.hh.performance_review.dto.UserWithScoreDto;
import ru.hh.performance_review.dto.response.GradeUserDto;
import ru.hh.performance_review.dto.response.RatingResponseDto;
import ru.hh.performance_review.exception.BusinessServiceException;
import ru.hh.performance_review.exception.InternalErrorCode;
import ru.hh.performance_review.mapper.GradeMapper;
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
    private final ContentOfPollDao contentOfPollDao;
    private final GradeMapper gradeMapper;
    private final RespondentsOfPollDao respondentsOfPollDao;


    @Transactional
    @Override
    public GradeUserDto countGrade(String userId, String pollId) {
        
        checkPollStatus(pollId);
        
        List<ComparePair> results = comparePairDao.getRatingForUserByPollId(UUID.fromString(userId), UUID.fromString(pollId));

        if (CollectionUtils.isEmpty(results)) {
            return new GradeUserDto(Collections.emptyList(), Collections.emptyList());
        }

        Map<Question, List<ComparePair>> questionsComparePairs = results.stream()
               .collect(Collectors.groupingBy(ComparePair::getQuestion));

        Map<Question,Double> questionsAndGrade = new HashMap<>();

        for (Question question: questionsComparePairs.keySet()) {
            long countWinner = questionsComparePairs.get(question).stream()
                    .filter(x -> x.getWinner().getUserId().equals(UUID.fromString(userId)))
                    .count();
            double gradeQuestion = (9.0 * countWinner / questionsComparePairs.get(question).size()) + 1;

            questionsAndGrade.put(question, gradeQuestion);
        }

        List<ContentOfPoll> listContentOfPoll = contentOfPollDao.getByPollId(UUID.fromString(pollId));

        Map<String, Integer> orderOfQuestions = listContentOfPoll.stream()
                .collect(Collectors.toMap(cop -> cop.getQuestion().getText(), ContentOfPoll::getOrder));

        List<ResultQuestionDto> resultQuestionDtoList = questionsAndGrade.entrySet().stream()
                .map(x -> gradeMapper.toResultQuestionDto(x.getKey(), Math.round(x.getValue())))
                .sorted(Comparator.comparing(x -> orderOfQuestions.get(x.getTextQuestion())))
                .collect(Collectors.toList());

        List<ResultCompetenceDto> resultCompetenceDtoList = questionsAndGrade.entrySet().stream()
                .filter(x -> Objects.nonNull(x.getKey().getCompetence()))
                .collect(Collectors.toMap(x -> x.getKey().getCompetence(), Map.Entry::getValue, (a, b) -> (a + b) /2))
                .entrySet()
                .stream()
                .map(x -> gradeMapper.toResultCompetenceDto(x.getKey(), Math.round(x.getValue())))
                .collect(Collectors.toList());

        return new GradeUserDto(resultQuestionDtoList, resultCompetenceDtoList);
    }

    @Transactional
    @Override
    public RatingResponseDto countRating(String pollId, Integer page) {

        checkPollStatus(pollId);

        List<ComparePair> allResults = comparePairDao.getRatingForAllByPollIdPagination(UUID.fromString(pollId), page);  // все записи compare_pair по опросу

        if (CollectionUtils.isEmpty(allResults)) {
            return new RatingResponseDto(Collections.emptyList());
        }

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
                    throw new BusinessServiceException(InternalErrorCode.INTERNAL_ERROR,
                            String.format("Респондент с id %s не найден в таблице результатов",respondent.getUserId()));
                }

                double gradeQuestion = 100.0 * countWinner / countParticipant;

                resultQuestion.add(new UserWithScoreDto(userMapper.toUserInfoDto(respondent), Math.round(gradeQuestion)));
            }
            resultQuestion = resultQuestion.stream()
                    .sorted(Comparator.comparing(UserWithScoreDto::getScore).reversed())
                    .collect(Collectors.toList());

            resultAllQuestions.add(gradeMapper.toRatingQuestionDto(question, resultQuestion));
        }

       if (page == null) {                  // если запрошены все вопросы - то сортировка по order

           List<ContentOfPoll> listContentOfPoll = contentOfPollDao.getByPollId(UUID.fromString(pollId));

           Map<String, Integer> orderOfQuestions = listContentOfPoll.stream()
                   .collect(Collectors.toMap(cop -> cop.getQuestion().getText(), ContentOfPoll::getOrder));

           resultAllQuestions.sort(Comparator.comparing(x -> orderOfQuestions.get(x.getTextQuestion())));
       }

        return new RatingResponseDto(resultAllQuestions);

    }
    
    private void checkPollStatus(String pollId) {
        
        Poll poll = commonDao.getByID(Poll.class, UUID.fromString(pollId));

        if (poll == null) {
            throw new BusinessServiceException(InternalErrorCode.UNKNOWN_POLL, String.format("pollId:%s", pollId));
        }

        if (!respondentsOfPollDao.isClosedOrCompleted(UUID.fromString(pollId))) {
            throw new BusinessServiceException(InternalErrorCode.INTERNAL_ERROR,
                    String.format("Опрос %s еще не завершен", pollId));
        }
    }
    
}
