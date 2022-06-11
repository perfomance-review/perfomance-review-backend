package ru.hh.performance_review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.hh.performance_review.dto.response.compairofpoll.UserInfoDto;

import java.util.Map;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class RatingResponseDto implements ResponseMessage{

    /**
     * текст вопроса
     */
    private String textQuestion;
    /**
     * название компетенции
     */
    private String textCompetence;
    /**
     * результаты опроса для всех пользователей в разрезе вопроса
     */
    private Map<UserInfoDto, Long> ratingQuestion;
}
