package com.example.engtutor.services;

import com.example.engtutor.helpers.Output;
import com.example.engtutor.helpers.StringHelper;
import com.example.engtutor.models.Teacher;
import com.example.engtutor.repository.TeacherRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@org.springframework.stereotype.Service
@CacheConfig(cacheNames = "teachers")
public class TeacherService implements Service<Teacher> {

    private final TeacherRepository repository;
    @Autowired
    public TeacherService(TeacherRepository repository) {
        this.repository = repository;
    }

    @Override
    @CachePut(key="#entity.id")
    public Teacher add(Teacher entity) {
        validate(entity);
        return repository.save(entity);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(key = "'allTeachers'"),
            @CacheEvict(key = "#id")
    })
    public void remove(Long id) {
        var item = repository.findById(id);
        if(item.isEmpty())
            throw new IllegalArgumentException("group" + Output.VALUE_NOT_EXIST);
        repository.deleteById(id);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(key = "'allTeachers'"),
            @CacheEvict(key = "#id")
    })
    public Teacher update(Long id, Teacher entity) {
        Teacher teacher = repository.findById(id)
                .orElseThrow(() -> new IllegalStateException("teacher does not exist"));

        if(!StringHelper.isEmpty(entity.getFirstName()) &&
                !Objects.equals(entity.getFirstName(), teacher.getFirstName())){
            teacher.setFirstName(entity.getFirstName());
        }

        if(!StringHelper.isEmpty(entity.getLastName()) &&
                !Objects.equals(entity.getLastName(), teacher.getLastName())){
            teacher.setLastName(entity.getLastName());
        }

        if(entity.getDateOfBirth() != null &&
                !Objects.equals(entity.getDateOfBirth(), teacher.getDateOfBirth())){
            teacher.setDateOfBirth(entity.getDateOfBirth());
        }

        if(!StringHelper.isEmpty(entity.getDescription()) &&
                !Objects.equals(entity.getDescription(), teacher.getDescription())){
            teacher.setDescription(entity.getDescription());
        }

        if(!Objects.equals(entity.getSalary(), teacher.getSalary())){
            teacher.setSalary(entity.getSalary());
        }

        return teacher;
    }

    @Override
    @Cacheable(key="'allTeachers'")
    public List<Teacher> getAll() {
        return repository.findAll();
    }

    @Override
    @Cacheable(key="'allTeachers'")
    public List<Teacher> getPaged(int limit, int offset) {
        return repository.findAll(PageRequest.of(offset, limit)).stream().toList();
    }

    @Override
    @Cacheable(key="#id")
    public Optional<Teacher> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void validate(Teacher teacher) {
        if (StringHelper.isEmpty(teacher.getFirstName()))
            throw new IllegalArgumentException("first name" + Output.EMPTY_VALUE);

        if (StringHelper.isEmpty(teacher.getLastName()))
            throw new IllegalArgumentException("last name" + Output.EMPTY_VALUE);

        if (teacher.getDateOfBirth() == null || teacher.getDateOfBirth().isAfter(LocalDate.now()))
            throw new IllegalArgumentException("date of birth" + Output.INCORRECT_VALUE);

        if(teacher.getSalary() < 0)
            throw new IllegalArgumentException("salary" + Output.EMPTY_VALUE);
    }
}
