package ru.hh.performance_review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.performance_review.dao.QuestionDao;
import ru.hh.performance_review.dto.QuestionDto;
import ru.hh.performance_review.dto.response.QuestionsResponseDto;
import ru.hh.performance_review.mapper.QuestionMapper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionDao questionDao;
    private final QuestionMapper questionMapper;

    @Override
    @Transactional(readOnly = true)
    public QuestionsResponseDto getAllQuestionsForManager(String userId) {

        List<QuestionDto> questionDtoList = questionDao.getQuestionsByUserId(UUID.fromString(userId))
                .stream()
                .map(questionMapper::toQuestionDto)
                .collect(Collectors.toList());

        return new QuestionsResponseDto(questionDtoList);

    }



}
