package ru.hh.performance_review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.hh.performance_review.dto.RatingQuestionDto;

import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class RatingResponseDto implements ResponseMessage {

    private List<RatingQuestionDto> questionsAndUsersWithScore;

}
