package ru.hh.performance_review.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class RespondentDto {

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
    /**
     * должность пользователя
     */
    private String position;

    /**
     * признак испытательного срока
     */
    private Boolean probation;

    /**
     * ссылка на фото пользователя
     */
    private String linkPhoto;

}
