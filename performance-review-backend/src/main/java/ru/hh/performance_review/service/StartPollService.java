package ru.hh.performance_review.service;

import ru.hh.performance_review.dto.response.EmptyResponseDto;
import ru.hh.performance_review.dto.response.PollProgressDto;
import ru.hh.performance_review.model.Poll;
import ru.hh.performance_review.model.PollStatus;
import ru.hh.performance_review.model.RespondentsOfPoll;
import ru.hh.performance_review.model.User;

import java.util.List;
import java.util.UUID;

/**
 * Сервис начала опроса
 */
public interface StartPollService {

    /**
     * Метод для начала опроса:
     * Меняет меняет статус опроса с OPEN на PROGRESS
     * сохраняет в БД респондентов, которых исключили из опроса
     * сохраняет в таблице compare_pair все возможные сочетания респондентов по всем вопросам
     *       для данного пользователя и данного опроса
     */
    PollProgressDto doStartPoll(String pollId, String userId, List<String> includedIdsString);

}
