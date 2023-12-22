package com.example.engtutor.services;

import java.util.List;
import java.util.Optional;

public interface Service<T> {
    T add(T entity);
    void remove(Long id);
    T update(Long id, T entity);
    List<T> getAll();
    Optional<T> getById(Long id);

    boolean isValid(T entity);
}
