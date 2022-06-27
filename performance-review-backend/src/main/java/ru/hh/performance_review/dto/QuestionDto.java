package ru.hh.performance_review.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class QuestionDto {

    /**
     * UUID вопроса
     */
    private UUID questionId;

    /**
     * Текст вопроса
     */
    private String textQuestion;

    /**
     * текст компетенции
     */
    private String textCompetence;

}
