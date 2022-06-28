package ru.hh.performance_review.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.hh.performance_review.dto.QuestionDto;
import ru.hh.performance_review.model.Question;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    @Mapping(target = "textQuestion", source = "question.text")
    @Mapping(target = "textCompetence", source = "question.competence.text")
    QuestionDto toQuestionDto(Question question);

}
