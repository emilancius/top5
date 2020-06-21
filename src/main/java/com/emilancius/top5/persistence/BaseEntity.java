package com.emilancius.top5.persistence;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {

    @Column(name = "CREATED_AT")
    private Instant created;

    @Column(name = "LAST_UPDATED_AT")
    private Instant updated;

    @PrePersist
    public void onSave() {
        if (created == null) {
            created = Instant.now();
        }
    }

    @PreUpdate
    public void onUpdate() {
        updated = Instant.now();
    }
}
