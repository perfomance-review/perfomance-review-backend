package ru.hh.performance_review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class RatingQuestionDto {

    private String textQuestion;

    private String textCompetence;

    private List<UserWithScoreDto> usersWithScore;

}
