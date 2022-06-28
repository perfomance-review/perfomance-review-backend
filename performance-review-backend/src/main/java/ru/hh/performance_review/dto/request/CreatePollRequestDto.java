package ru.hh.performance_review.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CreatePollRequestDto {
    /**
     * Название опроса
     */
    private String name;
    /**
     * Описание опроса
     */
    private String description;
    /**
     * Дата дедлайна
     */
    private String deadline;
    /**
     * Список id респондентов
     */
    private List<String> respondentIds;
    /**
     * Список id вопросов
     */
    private List<String> questionIds;
}
