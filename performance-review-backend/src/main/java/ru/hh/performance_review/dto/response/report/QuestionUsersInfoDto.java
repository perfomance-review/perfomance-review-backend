package ru.hh.performance_review.dto.response.report;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Дто для отчета результаты опроса
 */
@Data
@Accessors(chain = true)
public class QuestionUsersInfoDto {

    /**
     * Полное имя респондента
     */
    private String userFullName;
    /**
     * Текст вопроса
     */
    private String textQuestion;
    /**
     * Оценка по шкале от 1 до 10
     */
    private Long score;

}
