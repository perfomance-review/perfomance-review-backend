package ru.hh.performance_review.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UserPollByIdResponseDto {

    /**
     * id пользователя
     */
    private UUID userId;

    /**

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

    /**
     * должность пользователя
     */
    private String position;

    /**
     * ссылка на фото пользователя
     */
    private String linkPhoto;
}
