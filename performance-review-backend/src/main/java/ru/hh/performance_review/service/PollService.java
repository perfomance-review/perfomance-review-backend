package ru.hh.performance_review.service;

import ru.hh.performance_review.dto.response.ComparePairsOfPollDto;
import ru.hh.performance_review.dto.response.PollByIdResponseDto;
import ru.hh.performance_review.dto.response.PollsByUserIdResponseDto;

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

    PollsByUserIdResponseDto getAllPollsByManagerId(String managerId);
}
