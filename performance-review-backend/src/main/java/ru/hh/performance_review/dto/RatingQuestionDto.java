package ru.hh.performance_review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class RatingQuestionDto {

    /**
     * Текст вопроса
     */
    private String textQuestion;

    /**
     * Название компетенции
     */
    private String textCompetence;

    /**
     * Список респондентов с оценками
     */
    private List<UserWithScoreDto> usersWithScore;

}
