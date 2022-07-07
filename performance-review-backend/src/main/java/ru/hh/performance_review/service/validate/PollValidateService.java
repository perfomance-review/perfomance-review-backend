package ru.hh.performance_review.service.validate;

import ru.hh.performance_review.dto.request.CreatePollRequestDto;

import java.util.Set;

/**
 * Сервис валидации
 */
public interface PollValidateService {

    /**
     * Валидация userId, pollId
     *
     * @param userId - идентификатор пользователя
     * @param pollId - идентификатор опроса
     */
    void validatePollById(String userId, String pollId);

    void validateComparePairsOfPoll(String userId, String pollId);

    void validatePollsByUserId(String userId, Set<String> status);

    /**
     * Валидация запроса на создание опроса
     *
     * @param request - запрос на создание опроса
     * @param userId - идентификатор пользователя
     */
    void validateCreatePollRequestDto(CreatePollRequestDto request, String userId);
}
