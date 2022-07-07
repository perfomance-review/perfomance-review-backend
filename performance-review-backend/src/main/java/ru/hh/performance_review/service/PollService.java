package ru.hh.performance_review.service;

import ru.hh.performance_review.dto.request.CreatePollRequestDto;
import ru.hh.performance_review.dto.response.ComparePairsOfPollDto;
import ru.hh.performance_review.dto.response.PollByIdResponseDto;
import ru.hh.performance_review.dto.response.PollsByUserIdResponseDto;
import ru.hh.performance_review.dto.response.ResponseMessage;

import java.util.Set;

public interface PollService {

    PollsByUserIdResponseDto getPollsByUserId(String user, Set<String> statuses);

    PollByIdResponseDto getPollById(String pollId, String userId);

    /**
     * отдаёт вопрос и сформированные пары по опросу
     *
     * @param userId - идентификатор пользователя
     * @param pollId - pollId запроса
     * @return - ComparePairsOfPollDto
     */
    ComparePairsOfPollDto getComparePairOfPollDto(String userId, String pollId);


    /**
     * Создает опрос
     *
     * @param request - запрос на создание опроса
     * @param userId - идентификатор пользователя
     */
    ResponseMessage createPoll(CreatePollRequestDto request, final String userId);


    PollsByUserIdResponseDto getAllPollsByManagerId(String managerId);

}
