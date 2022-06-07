package ru.hh.performance_review.model;

import lombok.*;
import ru.hh.performance_review.model.base.AbstractAuditableBaseEntity;

import javax.persistence.*;
import java.util.UUID;

/**
 * Таблица для хранения вопросов для данного опроса
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "content_of_poll")
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
