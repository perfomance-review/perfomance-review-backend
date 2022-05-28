package ru.hh.performance_review.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class WinnerRawDto {
    /**
     * Идентификатор опроса
     */
    private UUID pollId;
    /**
     * Идентификатор вопроса
     */
    private UUID questionId;
    /**
     * Идентификатор участника 1
     */
    private UUID person1Id;
    /**
     *  Идентификатор участника 2
     */
    private UUID person2Id;
    /**
     *  Идентификатор выбранного в вопросе
     */
    private UUID winnerId;
}
