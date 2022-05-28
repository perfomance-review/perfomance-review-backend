package ru.hh.performance_review.dto.request;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Data
public class UpdateWinnerRequestDto {
    /**
     * Идентификатор опроса
     */
    private String pollId;
    /**
     * Идентификатор вопроса
     */
    private String questionId;
    /**
     * Идентификатор участника 1
     */
    private String person1Id;
    /**
     *  Идентификатор участника 2
     */
    private String person2Id;
    /**
     *  Идентификатор выбранного в вопросе
     */
    private String winnerId;
    /**
     *  Идентификатор выбранного в вопросе
     */
    private String isCompleted;

    @Override
    public String toString() {
        return "{" +
                "pollId='" + pollId + '\'' +
                ", questionId='" + questionId + '\'' +
                ", person1Id='" + person1Id + '\'' +
                ", person2Id='" + person2Id + '\'' +
                ", winnerId='" + winnerId + '\'' +
                '}';
    }
}
