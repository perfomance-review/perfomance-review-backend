package ru.hh.performance_review.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.hh.performance_review.model.Poll;
import ru.hh.performance_review.model.PollStatus;
import ru.hh.performance_review.model.RespondentsOfPoll;
import ru.hh.performance_review.model.User;

@Mapper(componentModel = "spring")
public interface RespondentsOfPollMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "recCreateDttm", ignore = true)
    @Mapping(target = "recUpdateDttm", ignore = true)
    RespondentsOfPoll toRespondentsOfPoll(Poll poll, User respondent, PollStatus status);
}
