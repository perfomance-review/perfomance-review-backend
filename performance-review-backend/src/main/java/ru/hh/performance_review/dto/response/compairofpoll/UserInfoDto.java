package ru.hh.performance_review.dto.response.compairofpoll;

import lombok.Data;

import java.util.UUID;

@Data
public class UserInfoDto {

    /**
     * id пользователя
     */
    private UUID userId;

    /**
     * имя пользователя
     */
    private String firstName;

    /**
     * фамилия пользователя
     */
    private String secondName;

    /**
     * отчество пользователя
     */
    private String middleName;


}
