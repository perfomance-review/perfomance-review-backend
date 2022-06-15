package ru.hh.performance_review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.hh.performance_review.dto.response.compairofpoll.UserInfoDto;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class UserWithScoreDto {

    /**
     * Информация о респонденте
     */
    private UserInfoDto userInfo;

    /**
     * Оценка по шкале от 0 до 100
     */
    private Long score;
}
