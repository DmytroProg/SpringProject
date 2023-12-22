package com.example.engtutor.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public abstract class Service<T> {

    protected final JpaRepository<T, Long> repository;

    public Service(JpaRepository<T, Long> repository) {
        this.repository = repository;
    }

    public T add(T entity) {
        if(!isValid(entity)) throw new IllegalArgumentException();
        return repository.save(entity);
    }
    public void remove(Long id) {
        repository.deleteById(id);
    }
    public abstract T update(Long id, T entity);

    public abstract List<T> getAll();
    public abstract List<T> getPaged(int limit, int offset);
    public abstract Optional<T> getById(Long id);
    public abstract boolean isValid(T entity);
}
