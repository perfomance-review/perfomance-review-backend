package ru.hh.performance_review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.hh.performance_review.model.base.AbstractAuditableBaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

/**
 * Компетенции
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "competence")
public class Competence extends AbstractAuditableBaseEntity {

    /**
     * Уникальный идентификатор записи
     */
    @Id
    @Column(name = "competence_id")
    private UUID competenceId = UUID.randomUUID();

    /**
     * Название компетенции
     */
    @Column(name = "text", unique = true)
    private String text;

    public Competence(String text) {
        this.text = text;
    }
}
