package ru.hh.performance_review.dto.response;

import lombok.Data;
import ru.hh.performance_review.dto.UserPollByIdResponseDto;

import java.time.LocalDate;
import java.util.List;

@Data
public class PollByIdResponseDto implements ResponseMessage {

    /**
     * название опроса
     */
    private String title;

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
