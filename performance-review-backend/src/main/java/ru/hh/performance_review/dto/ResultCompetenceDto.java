package ru.hh.performance_review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResultCompetenceDto {
    /**
     * Название компетенции
     */
    private String textCompetence;
    /**
     * Оценка по шкале от 1 до 10
     */
    private long score;
}
