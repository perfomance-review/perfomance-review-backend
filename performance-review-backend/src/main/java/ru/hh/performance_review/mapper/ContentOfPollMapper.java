package ru.hh.performance_review.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.hh.performance_review.model.ContentOfPoll;
import ru.hh.performance_review.model.Poll;
import ru.hh.performance_review.model.Question;

@Mapper(componentModel = "spring")
public interface ContentOfPollMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "recCreateDttm", ignore = true)
    @Mapping(target = "recUpdateDttm", ignore = true)
    ContentOfPoll toContentOfPoll(Poll poll, Question question, Integer order);
}
