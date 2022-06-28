package ru.hh.performance_review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.hh.performance_review.dto.QuestionDto;

import java.util.List;

@Data
@AllArgsConstructor
public class QuestionsResponseDto implements ResponseMessage{

    private List<QuestionDto> questions;

}
