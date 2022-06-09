package ru.hh.performance_review.service;

import ru.hh.performance_review.dto.response.GradeUserDto;

/**
 * Сервис подсчета оценки
 */
public interface GradeService {

    /**
     * Метод подсчета оценок по вопросам и компетенциям данного пользователя по данному опросу
     */
    GradeUserDto countGrade(String userId, String pollId);
}
