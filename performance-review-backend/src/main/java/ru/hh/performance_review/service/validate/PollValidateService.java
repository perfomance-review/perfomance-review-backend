package ru.hh.performance_review.service.validate;

/**
 * Сервис валидации
 */
public interface PollValidateService {

    /**
     * Валидация pollId
     *
     * @param pollId - идентификатор пользователя
     */
    void pollIdValidate(String pollId);
}
