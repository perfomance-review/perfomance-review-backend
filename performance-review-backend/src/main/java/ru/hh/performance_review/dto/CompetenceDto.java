package ru.hh.performance_review.dto;

import lombok.Data;

import java.util.UUID;


@Data
public class CompetenceDto {

    /**
     * Идентификатор клмпетенции
     */
    private UUID id;
    /**
     * Название клмпетенции
     */
    private String text;

}
