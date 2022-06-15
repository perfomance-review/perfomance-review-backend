package ru.hh.performance_review.service;

import ru.hh.performance_review.dto.response.GradeUserDto;
import ru.hh.performance_review.dto.response.RatingResponseDto;

/**
 * Сервис подсчета оценки
 */
public interface GradeService {

    /**
     * Метод возвращает оценку по вопросам и компетенциям данного пользователя по данному опросу
     */
    GradeUserDto countGrade(String userId, String pollId);

    /**
     * Метод возвращает рейтинг респондентов по данному опросу в разрезе вопросов
     * Поддерживает пагинацию, page - номер вопроса с 1
     */
    RatingResponseDto countRating(String pollId, Integer page);
}
