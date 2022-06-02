package ru.hh.performance_review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.hh.performance_review.model.base.AbstractAuditableBaseEntity;

import javax.persistence.*;
import java.util.UUID;

/**
 * Таблица результатов сравнения пары
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "compare_pair")
public class ComparePair extends AbstractAuditableBaseEntity {

    /**
     * Уникальный идентификатор записи
     */
    @Id
    @Column(name = "pair_id")
    private UUID pairId = UUID.randomUUID();

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
     * id победителя
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winner_id")
    private User winner;
    /**
     * id 1 участника
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person1_id")
    private User person1;
    /**
     * id 2 участника
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person2_id")
    private User person2;
    /**
     * id респондента
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "respondent_id")
    private User respondent;

    public ComparePair(Poll poll, Question question, User person1, User person2, User respondent) {
        this.poll = poll;
        this.question = question;
        this.person1 = person1;
        this.person2 = person2;
        this.respondent = respondent;
    }
}
