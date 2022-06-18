package ru.hh.performance_review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.hh.performance_review.dto.ResultCompetenceDto;
import ru.hh.performance_review.dto.ResultQuestionDto;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class GradeUserDto implements ResponseMessage{

    /**
     * Список вопросов с оценками по ним
     */
    private List<ResultQuestionDto> resultForQuestions;
    /**
     * Список компетенций с оценками по ним
     */
    private List<ResultCompetenceDto> resultForCompetences;

}
