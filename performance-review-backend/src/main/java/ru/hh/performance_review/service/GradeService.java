package ru.hh.performance_review.service;

import ru.hh.performance_review.dto.response.GradeUserDto;
import ru.hh.performance_review.dto.response.RatingResponseDto;

/**
 * Сервис подсчета оценки
 */
public interface GradeService {

    /**
     * Метод подсчета оценок по вопросам и компетенциям данного пользователя по данному опросу
     */
    GradeUserDto countGrade(String userId, String pollId);

    RatingResponseDto countRating(String userId, String pollId);
}
