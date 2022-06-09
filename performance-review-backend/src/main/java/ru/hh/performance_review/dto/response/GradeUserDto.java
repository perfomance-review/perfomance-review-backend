package ru.hh.performance_review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class GradeUserDto implements ResponseMessage{

    /**
     * Map с вопросами и оценками по ним
     */
    private Map<String, Long> resultForQuestions;
    /**
     * Map с компетенциями и оценками по ним
     */
    private Map<String, Long> resultForCompetences;

}
