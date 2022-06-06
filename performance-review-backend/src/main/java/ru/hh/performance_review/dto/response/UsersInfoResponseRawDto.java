package ru.hh.performance_review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.hh.performance_review.model.RoleEnum;

import java.util.UUID;

@AllArgsConstructor
@Data
public class UsersInfoResponseRawDto {


    private UUID userId;
    /**
     * email пользователя
     */
    private String email;
    /**
     * пароль пользователя
     */
    private String password;
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

    /**
     * роль пользователя (администратор, менеджер, респондент)
     */
    private RoleEnum role;

    /**
     * user, который пригласил данного пользователя (null, администратор, менеджер)
     */
    private UUID leaderId;
}
