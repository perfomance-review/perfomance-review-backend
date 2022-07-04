package ru.hh.performance_review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.hh.performance_review.dto.CompetenceDto;

import java.util.List;

@Data
@AllArgsConstructor
public class CompetencesResponseDto implements ResponseMessage{

    private List<CompetenceDto> competences;

}
