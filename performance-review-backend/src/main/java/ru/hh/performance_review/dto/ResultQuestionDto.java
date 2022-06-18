package ru.hh.performance_review.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ResultQuestionDto {
    /**
     * Текст вопроса
     */
    private String textQuestion;
    /**
     * Название компетенции
     */
    private String textCompetence;
    /**
     * Оценка по шкале от 1 до 10
     */
    private long score;
}
