package ru.hh.performance_review.consts;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ReportType {

    USERS_INFO("users.xlsx"),
    CREATED_POLLS("poll_created.xlsx"),
    POLL_RESULTS("poll_results.xlsx");

    @Getter
    private final String reportName;

}
