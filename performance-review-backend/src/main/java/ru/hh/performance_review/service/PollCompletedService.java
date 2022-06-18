package ru.hh.performance_review.service;

import org.springframework.transaction.annotation.Transactional;

public interface PollCompletedService {
    /**
     * закрывает опрос, если истек дедлайн
     */
    @Transactional
    void closePollsByDeadline();
}
