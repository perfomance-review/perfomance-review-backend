package ru.hh.performance_review.service;

import ru.hh.performance_review.model.PollStatus;

import java.util.List;
import java.util.UUID;

/**
 * Сервис начала опроса
 */
public interface StartPollService {

    /**
     * Метод изменяет статус опроса на PROGRESS
     */
    void changeStatusPoll(UUID pollId, UUID userId, PollStatus status);

    /**
     * Метод сохраняет в БД респондентов, которых исключили из опроса
     */
    void saveExcluded(UUID pollId, UUID userId, List<UUID> includedIds);

    /**
     * Метод сохраняет в таблице compare_pair все возможные сочетания респондентов по всем вопросам
     * для данного пользователя и данного опроса
     */
    void saveComparePair(UUID pollId, UUID userId, List<UUID> includedIds);

}
