package ru.hh.performance_review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.hh.performance_review.dto.RespondentDto;

import java.util.List;

@Data
@AllArgsConstructor
public class RespondentsResponseDto implements ResponseMessage {

    private List<RespondentDto> respondents;

}
