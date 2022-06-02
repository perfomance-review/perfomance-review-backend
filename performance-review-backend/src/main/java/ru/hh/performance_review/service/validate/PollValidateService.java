package ru.hh.performance_review.service.validate;

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
    void getPollByIdValidate(String userId, String pollId);

    void validateComparePairsOfPoll(String userId, String pollId);
}
