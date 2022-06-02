package ru.hh.performance_review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.hh.performance_review.dto.PollByUserIdResponseDto;

import java.util.List;

@Data
@AllArgsConstructor
public class PollsByUserIdResponseDto implements ResponseMessage {

    /**
     * список опросов
     */
    private List<PollByUserIdResponseDto> polls;
}
