package ru.hh.performance_review.service;

public interface ScheduledService {
    /**
     * закрывает опрос по истечению дедлайна
     */
    void closePollsByDeadline();
}
