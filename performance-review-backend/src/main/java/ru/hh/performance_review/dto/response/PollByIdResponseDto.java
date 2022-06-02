package ru.hh.performance_review.dto.response;

import lombok.Data;
import ru.hh.performance_review.dto.UserPollByIdResponseDto;
import ru.hh.performance_review.model.PollStatus;

import java.time.LocalDate;
import java.util.List;

@Data
public class PollByIdResponseDto implements ResponseMessage {

    /**
     * название опроса
     */
    private String title;

    /**
     * статус опроса
     */
    private PollStatus status;

    /**
     * количество вопросов
     */
    private long questionsCount;

    /**
     * дедлайн
     */
    private LocalDate deadline;

    /**
     * список всех респондентов опроса
     */
    private List<UserPollByIdResponseDto> respondents;
}
