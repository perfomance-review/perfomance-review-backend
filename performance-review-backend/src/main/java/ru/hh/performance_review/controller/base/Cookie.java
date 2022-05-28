package ru.hh.performance_review.controller.base;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Константы cookies
 * @deprecated вместо него {@link CookieConst}
 * @see CookieConst
 */
@Deprecated
@RequiredArgsConstructor
@Getter
public enum Cookie {

    USER_ID("user-id");

    private final String value;
}
