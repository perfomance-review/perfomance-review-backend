package ru.hh.performance_review.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.hh.performance_review.dto.RatingQuestionDto;
import ru.hh.performance_review.dto.ResultCompetenceDto;
import ru.hh.performance_review.dto.ResultQuestionDto;
import ru.hh.performance_review.dto.UserWithScoreDto;
import ru.hh.performance_review.model.Competence;
import ru.hh.performance_review.model.Question;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GradeMapper {

    @Mapping(target = "textQuestion", source = "question.text")
    @Mapping(target = "textCompetence", source = "question.competence.text")
    ResultQuestionDto toResultQuestionDto(Question question, long score);

    @Mapping(target = "textCompetence", source = "competence.text")
    ResultCompetenceDto toResultCompetenceDto(Competence competence, long score);

    @Mapping(target = "textQuestion", source = "question.text")
    @Mapping(target = "textCompetence", source = "question.competence.text")
    RatingQuestionDto toRatingQuestionDto(Question question, List<UserWithScoreDto> usersWithScore);
}
