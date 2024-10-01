package org.SchoolApp.Services;

import java.util.List;
import java.util.Optional;

public interface GenericService<T, ID> {
    T create(T entity);
    Optional<T> getById(ID id);
    List<T> getAll();
    void delete(ID id);
}