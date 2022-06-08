package ru.hh.performance_review.dto.response.compairofpoll;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ComparePairsOfPollInfoDto {

    /**
     * Информация о первом участнике опроса
     */
    private UserInfoDto person1;

    /**
     * Информация о втором участнике опроса
     */
    private UserInfoDto person2;

}
