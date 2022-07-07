package ru.hh.performance_review.consts;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Константы запроса
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestParams {

    public static final String USER_ID = "user-id";
    public static final String POLL_ID = "poll_id";
    public static final String STATUS = "status";
    public static final String RESPONDENT_ID = "respondentId";
    public static final String QUESTION_ID = "questionId";
}
