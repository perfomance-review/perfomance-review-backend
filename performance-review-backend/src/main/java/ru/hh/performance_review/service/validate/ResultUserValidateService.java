package ru.hh.performance_review.service.validate;

public interface ResultUserValidateService {

    /**
     * Валидация userId, pollId
     *
     * @param userId - идентификатор пользователя
     * @param pollId - идентификатор опроса
     */

    void validateDataResultUser(String pollId, String userId);

}
