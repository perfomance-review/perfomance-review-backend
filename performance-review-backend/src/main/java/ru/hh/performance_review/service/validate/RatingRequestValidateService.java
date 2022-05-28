package ru.hh.performance_review.service.validate;

import ru.hh.performance_review.dto.request.UpdateWinnerRequestDto;

public interface RatingRequestValidateService {

    /**
     * Валидация данных запроса на обновление победителя
     *
     * @param updateWinnerRequestDto
     */
    void validateUpdateWinnerRequestDto(String userId, UpdateWinnerRequestDto updateWinnerRequestDto);
}
