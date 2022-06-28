package ru.hh.performance_review.service;

import ru.hh.performance_review.dto.response.RespondentsResponseDto;
import ru.hh.performance_review.dto.response.UserResponseDto;
import ru.hh.performance_review.dto.response.UsersInfoResponseDto;
import ru.hh.performance_review.security.context.AuthUserInfo;

/**
 * Сервис получения данных пользователя
 */
public interface UserService {

    /**
     * Метод получения userId случайного пользователя
     *
     * @return - userId
     */
    String getAnyRespondentId();

    /**
     * Метод получения пользователя по userId
     *
     * @return - User
     */
    UserResponseDto getRespondentByUserId(String userId);

    UsersInfoResponseDto getAllUsers();

    /**
     * Метод получения всех респондентов для данного менеджера
     *
     * @return - UserInfo
     */
    RespondentsResponseDto getAllRespondentsForManager(String userId);

    AuthUserInfo findAuthUserInfoByUserEmail(String userEmail);
}
