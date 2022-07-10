package ru.hh.performance_review.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.hh.performance_review.dto.CompetenceDto;
import ru.hh.performance_review.model.Competence;

@Mapper(componentModel = "spring")
public interface CompetenceMapper {

    @Mapping(target = "id", source = "competence.competenceId")
    CompetenceDto toCompetenceDto(Competence competence);
}
