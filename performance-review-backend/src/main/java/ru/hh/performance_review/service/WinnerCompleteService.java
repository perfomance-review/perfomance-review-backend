package ru.hh.performance_review.service;

import ru.hh.performance_review.dto.request.UpdateWinnerRequestDto;
import ru.hh.performance_review.dto.response.ResponseMessage;

/**
 * Сервис обновления победителя в паре по вопросу
 */
public interface WinnerCompleteService {

    /**
     * метод обновления победителя в паре по вопросу
     */
    ResponseMessage updateWinner(String userId, UpdateWinnerRequestDto requestDto);
}
