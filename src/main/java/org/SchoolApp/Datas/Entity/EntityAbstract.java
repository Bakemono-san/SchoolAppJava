package org.SchoolApp.Datas.Entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class EntityAbstract {
    protected boolean deleted = false;

    protected LocalDateTime deletedAt;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;
}
