package ru.hh.performance_review.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.hh.performance_review.dto.GetPollResponseDto;
import ru.hh.performance_review.model.Poll;
import ru.hh.performance_review.model.PollStatus;

@Mapper(componentModel = "spring")
public interface PollMapper {
  @Mapping(target = "title", source = "poll.name")
  @Mapping(target = "deadline", source = "poll.deadline")
  GetPollResponseDto toGetPollResponseDto(Poll poll, long respondentsCount, long questionsCount, PollStatus status);
}