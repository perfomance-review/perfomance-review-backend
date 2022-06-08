package ru.hh.performance_review.dto.response;

import lombok.Data;
import ru.hh.performance_review.model.PollStatus;

import java.time.LocalDate;

@Data
public class PollProgressDto implements ResponseMessage{

    /**
     * название опроса
     */
    private String title;

    /**
     * описание опроса
     */
    private String description;

    /**
     * статус опроса
     */
    private PollStatus status;

    /**
     * дедлайн
     */
    private LocalDate deadline;

}
