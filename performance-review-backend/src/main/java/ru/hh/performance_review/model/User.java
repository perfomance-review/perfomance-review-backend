package ru.hh.performance_review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.hh.performance_review.model.base.AbstractAuditableBaseEntity;

import javax.persistence.*;
import java.util.UUID;

/**
 * Пользователи
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users", schema = "performance_review")
public class User extends AbstractAuditableBaseEntity {

    /**
     * Уникальный идентификатор записи
     */
    @Id
    @Column(name = "user_id")
    private UUID userId = UUID.randomUUID();
    /**
     * email пользователя
     */
    @Column(name = "email")
    private String email;
    /**
     * пароль пользователя
     */
    @Column(name = "password")
    private String password;
    /**
     * имя пользователя
     */
    @Column(name = "first_name")
    private String firstName;
    /**
     * фамилия пользователя
     */
    @Column(name = "second_name")
    private String secondName;
    /**
     * отчество пользователя
     */
    @Column(name = "middle_name")
    private String middleName;
    /**
     * должность пользователя
     */
    @Column(name = "position")
    private String position; // или объект?

    /**
     * признак испытательного срока
     */
    @Column(name = "probation")
    private Boolean probation;

    /**
     * ссылка на фото пользователя
     */
    @Column(name = "link_photo")
    private String linkPhoto;

    /**
     * роль пользователя (администратор, менеджер, респондент)
     */
    @Column(name = "role")
    @Enumerated(EnumType.STRING )
    private RoleEnum role;

    /**
     * user, который пригласил данного пользователя (null, администратор, менеджер)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lead_id")
    private User leader;



}
