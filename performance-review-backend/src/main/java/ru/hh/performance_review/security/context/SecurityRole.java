package ru.hh.performance_review.security.context;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Роли пользователя
 * @see ru.hh.performance_review.model.RoleEnum;
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityRole {

    public static final String ADMINISTRATOR = "ADMINISTRATOR";
    public static final String MANAGER = "MANAGER";
    public static final String RESPONDENT = "RESPONDENT";

}
