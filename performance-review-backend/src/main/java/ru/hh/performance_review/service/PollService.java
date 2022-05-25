package ru.hh.performance_review.service;

import ru.hh.performance_review.dto.response.PollByIdResponseDto;
import ru.hh.performance_review.dto.response.PollsByUserIdResponseDto;

public interface PollService {

    PollsByUserIdResponseDto getPollsByUserId(String user);

    PollByIdResponseDto getPollById(String pollId);
}
