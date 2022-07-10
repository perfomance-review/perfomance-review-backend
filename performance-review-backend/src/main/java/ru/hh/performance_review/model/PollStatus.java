package ru.hh.performance_review.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PollStatus {
    OPEN("OPEN", "Создан"),
    PROGRESS("PROGRESS", "Открыт для прохождения"),
    COMPLETED("COMPLETED", "Завершен"),
    CLOSED("CLOSED", "Закрыт для прохождения");

    private final String value;
    @Getter
    private final String humanValue;

}
