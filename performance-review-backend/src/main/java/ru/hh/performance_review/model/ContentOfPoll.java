package ru.hh.performance_review.model;

import liquibase.pro.packaged.I;
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
 * Таблица для хранения вопросов для данного опроса
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "content_of_poll", schema = "performance_review")
public class ContentOfPoll extends AbstractAuditableBaseEntity {

    /**
     * Уникальный идентификатор записи
     */
    @Id
    @Column(name = "id")
    private UUID id = UUID.randomUUID();

    /**
     * id опроса
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poll_id")
    private Poll poll;

    /**
     * id вопроса
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    /**
     * порядковый номер вопроса
     */
    @Column(name = "q_order")
    private Integer order;

}
