package ru.hh.performance_review.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.hh.performance_review.dto.PollByUserIdResponseDto;
import ru.hh.performance_review.dto.UserPollByIdResponseDto;
import ru.hh.performance_review.dto.response.PollByIdResponseDto;
import ru.hh.performance_review.model.Poll;
import ru.hh.performance_review.model.PollStatus;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PollMapper {
    @Mapping(target = "title", source = "poll.name")
    @Mapping(target = "deadline", source = "poll.deadline")
    PollByUserIdResponseDto toPollByUserIdResponseDto(Poll poll, long respondentsCount, long questionsCount, PollStatus status);

    @Mapping(target = "title", source = "poll.name")
    PollByIdResponseDto toPollByIdResponseDto(Poll poll, int questionsCount, List<UserPollByIdResponseDto> respondents);
}
