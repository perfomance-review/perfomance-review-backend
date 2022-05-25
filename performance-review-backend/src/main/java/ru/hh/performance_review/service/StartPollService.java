package ru.hh.performance_review.service;

import ru.hh.performance_review.dto.response.EmptyDtoResponse;
import ru.hh.performance_review.dto.response.UserResponseDto;
import ru.hh.performance_review.model.Poll;
import ru.hh.performance_review.model.PollStatus;
import ru.hh.performance_review.model.User;

import java.util.List;
import java.util.UUID;

/**
 * Сервис начала опроса
 */
public interface StartPollService {

    /**
     * Метод изменяет статус опроса на PROGRESS
     */
    void changeStatusPoll(Poll poll, User user, PollStatus status);

    /**
     * Метод сохраняет в БД респондентов, которых исключили из опроса
     */
    void saveExcluded(Poll poll, User user, List<UUID> includedIds);

    /**
     * Метод сохраняет в таблице compare_pair все возможные сочетания респондентов по всем вопросам
     * для данного пользователя и данного опроса
     */
    void saveComparePair(Poll poll, User user, List<UUID> includedIds);

    public EmptyDtoResponse doStartPoll(String pollId, String userId, List<String> includedIdsString);

}
