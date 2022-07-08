package ru.hh.performance_review.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.hh.performance_review.dto.PollByUserIdResponseDto;
import ru.hh.performance_review.dto.UserPollByIdResponseDto;
import ru.hh.performance_review.dto.request.CreatePollRequestDto;
import ru.hh.performance_review.dto.response.PollByIdResponseDto;
import ru.hh.performance_review.dto.response.PollProgressDto;
import ru.hh.performance_review.model.Poll;
import ru.hh.performance_review.model.PollStatus;
import ru.hh.performance_review.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PollMapper {
    @Mapping(target = "title", source = "poll.name")
    @Mapping(target = "description", source = "poll.description")
    @Mapping(target = "deadline", source = "poll.deadline")
    @Mapping(target = "createdDt", source = "poll.recCreateDttm")
    PollByUserIdResponseDto toPollByUserIdResponseDto(Poll poll, long respondentsCount, long questionsCount, PollStatus status);

    @Mapping(target = "title", source = "poll.name")
    @Mapping(target = "description", source = "poll.description")
    PollByIdResponseDto toPollByIdResponseDto(Poll poll, PollStatus status, int questionsCount, List<UserPollByIdResponseDto> respondents);

    @Mapping(target = "title", source = "poll.name")
    @Mapping(target = "description", source = "poll.description")
    @Mapping(target = "deadline", source = "poll.deadline")
    PollProgressDto toPollProgressDto(Poll poll, PollStatus status);

    @Mapping(target = "recCreateDttm", ignore = true)
    @Mapping(target = "recUpdateDttm", ignore = true)
    @Mapping(target = "pollId", ignore = true)
    Poll fromCreatePollRequestDto(CreatePollRequestDto request, User manager);
}
