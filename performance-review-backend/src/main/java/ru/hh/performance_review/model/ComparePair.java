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
@Table(name = "compare_pair", schema = "performance_review")
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
    @ManyToOne
    @JoinColumn(name = "poll_id")
    private Poll poll;
    /**
     * id вопроса
     */
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
    /**
     * id победителя
     */
    @ManyToOne
    @JoinColumn(name = "winner_id")
    private User winner;
    /**
     * id проигравшего
     */
    @ManyToOne
    @JoinColumn(name = "looser_id")
    private User looser;
    /**
     * id респондента
     */
    @ManyToOne
    @JoinColumn(name = "respondent_id")
    private User respondent;


}
