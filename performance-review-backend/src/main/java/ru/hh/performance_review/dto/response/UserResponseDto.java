package ru.hh.performance_review.dto.response;

import lombok.Data;
import ru.hh.performance_review.model.RoleEnum;

@Data
public class UserResponseDto implements ResponseMessage {

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

    /**
     * роль пользователя (администратор, менеджер, респондент)
     */
    private RoleEnum role;
}
