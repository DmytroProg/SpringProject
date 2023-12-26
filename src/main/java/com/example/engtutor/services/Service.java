package com.example.engtutor.services;

import java.util.List;
import java.util.Optional;

public interface Service<T> {
    T add(T entity);
    void remove(Long id);
    T update(Long id, T entity);
    List<T> getAll();
    List<T> getPaged(int limit, int offset);
    Optional<T> getById(Long id);
    void validate(T entity);
}
