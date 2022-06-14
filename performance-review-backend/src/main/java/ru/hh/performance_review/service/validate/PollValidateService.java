package ru.hh.performance_review.service.validate;

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
}
