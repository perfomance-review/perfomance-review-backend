package ru.hh.performance_review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.hh.performance_review.model.base.AbstractAuditableBaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Таблица для хранения респондентов для данного опроса
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "respondents_of_poll", schema = "performance_review")
public class RespondentsOfPoll extends AbstractAuditableBaseEntity {

    /**
     * Уникальный идентификатор записи
     */
    @Id
    @Column(name = "id")
    private UUID id = UUID.randomUUID();

    /**
     * id опроса
     */
    @ManyToOne
    @JoinColumn(name = "poll_id")
    private Poll poll;

    /**
     * id респондента
     */
    @ManyToOne
    @JoinColumn(name = "respondent_id")
    private User respondent;

    /**
     * статус опроса
     */
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PollStatus status;


}
