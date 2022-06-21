package ru.hh.performance_review.security.context;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import java.util.Set;

/**
 * Данные пользователя
 */
@FieldNameConstants
@Data
@Accessors(fluent = true)
@Builder
public final class AuthUserInfo {
    /**
     * Уникальный идентификатор
     */
    private String id;

    /**
     * Токен пользователя
     */
    private String accessToken;

    /**
     * Логин пользователя
     */
    private String userEmail;

    /**
     * Имя пользователя
     */
    protected String firstName;

    /**
     * Фамилия пользователя
     */
    protected String lastName;

    /**
     * Отчество пользователя
     */
    protected String middleName;

    /**
     * Роли пользователя
     */
    protected Set<String> roles;

}
