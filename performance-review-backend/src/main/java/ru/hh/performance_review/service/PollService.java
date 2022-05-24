package ru.hh.performance_review.service;

import ru.hh.performance_review.dto.GetPollResponseDto;
import ru.hh.performance_review.dto.response.PollByIdResponseDto;

import java.util.List;


public interface PollService {

    List<GetPollResponseDto> getPolls(String user);

    PollByIdResponseDto getPollById(String pollId);
}
