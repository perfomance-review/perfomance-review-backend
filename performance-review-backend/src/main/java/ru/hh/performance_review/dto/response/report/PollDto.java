package ru.hh.performance_review.dto.response.report;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.hh.performance_review.model.Poll;
import ru.hh.performance_review.model.PollStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class PollDto {

    /**
     * Название опроса
     */
    private String name;

    /**
     * Описание опроса
     */
    private String description;

    /**
     * дедлайн опроса
     */
    private LocalDate deadline;

    /**
     * Дата создания
     */
    private LocalDateTime recCreateDttm;

    /**
     * Статус опроса
     */
    private String status = PollStatus.COMPLETED.getHumanValue();

    public PollDto(Poll poll) {
        this.name = poll.getName();
        this.description = poll.getDescription();
        this.deadline = poll.getDeadline();
        this.recCreateDttm = poll.getRecCreateDttm();

    }
}
