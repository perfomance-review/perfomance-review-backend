package ru.hh.performance_review.service.validate;

/**
 * Сервис валидации
 */
public interface UserValidateService {

    /**
     * Валидация userId
     *
     * @param userId - идентификатор пользователя
     */
    void userIdValidate(String userId);
}
