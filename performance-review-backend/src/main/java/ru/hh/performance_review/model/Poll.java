package ru.hh.performance_review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.hh.performance_review.model.base.AbstractAuditableBaseEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Опросы
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "poll", schema = "performance_review")
public class Poll extends AbstractAuditableBaseEntity {

    /**
     * Уникальный идентификатор записи
     */
    @Id
    @Column(name = "poll_id")
    private UUID pollId = UUID.randomUUID();

    /**
     * Название опроса
     */
    @Column(name = "name")
    private String name;

    /**
     * id менеджера данног опроса
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "manager_id")
    private User manager;

    /**
     * дедлайн опроса
     */
    @Column(name = "deadline")
    private LocalDate deadline;

}
