package ru.hh.performance_review.model.base;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@MappedSuperclass
public abstract class AbstractAuditableBaseEntity {
    /**
     * Время последнего создания записи
     */
    @Column(name = "rec_create_dttm")
    private LocalDateTime recCreateDttm;

    /**
     * Время последней модификации записи
     */
    @Column(name = "rec_update_dttm")
    private LocalDateTime recUpdateDttm;

    // Для отслеживания события создания
    @PrePersist
    public void performPrePersistAudit() {
        recCreateDttm = LocalDateTime.now();
        recUpdateDttm = LocalDateTime.now();
    }

    // Для отслеживания события обновления
    @PreUpdate
    public void performPreUpdateAudit() {
        if (recCreateDttm == null) {
            recCreateDttm = LocalDateTime.now();
        }
        recUpdateDttm = LocalDateTime.now();
    }
}