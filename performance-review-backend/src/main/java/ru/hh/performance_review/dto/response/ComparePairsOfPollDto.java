package ru.hh.performance_review.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.hh.performance_review.dto.response.compairofpoll.ComparePairsOfPollInfoDto;

import java.util.List;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class ComparePairsOfPollDto implements ResponseMessage {

    /**
     * Идентификатор опроса
     */
    private String pollId;

    /**
     * Идентификатор респондента
     */
    private String respondentId;

    /**
     * id текущего вопроса
     */
    private UUID questionId;

    /**
     * Текст вопроса
     */
    private String text;

    /**
     * id следующего вопроса
     */
    private boolean hasNext = false;

    /**
     * Информация о парах
     */
    private List<ComparePairsOfPollInfoDto> pairsOfPollInfo;

}
