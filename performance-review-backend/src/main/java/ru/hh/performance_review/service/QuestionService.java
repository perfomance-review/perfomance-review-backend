package ru.hh.performance_review.service;

import ru.hh.performance_review.dto.response.QuestionsResponseDto;

public interface QuestionService {

    QuestionsResponseDto getAllQuestionsForManager(String userId);

}
