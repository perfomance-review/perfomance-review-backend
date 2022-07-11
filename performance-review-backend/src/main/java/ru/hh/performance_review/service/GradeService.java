package ru.hh.performance_review.service;

import ru.hh.performance_review.dto.response.GradeUserDto;
import ru.hh.performance_review.dto.response.RatingResponseDto;
import ru.hh.performance_review.dto.response.report.ReportDocumentPollResultDto;

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

    /**
     * Создание данных для отчета результаты опроса
     * возвращает оценку по вопросам и всех пользователей по данному опросу
     *
     * @param managerId - идентификатор менеджера опроса
     * @param pollId    - идентификатор опроса
     * @return - данных для отчета результаты опроса
     */
    ReportDocumentPollResultDto createReportDocumentPollResult(String managerId, String pollId);
}
