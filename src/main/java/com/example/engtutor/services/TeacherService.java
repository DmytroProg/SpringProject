package com.example.engtutor.services;

import com.example.engtutor.models.Teacher;
import com.example.engtutor.repository.TeacherRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@org.springframework.stereotype.Service
@CacheConfig(cacheNames = "teachers")
public class TeacherService extends Service<Teacher> {

    @Autowired
    public TeacherService(JpaRepository<Teacher, Long> teacherRepository) {
        super(teacherRepository);
    }
    @Override
    @Transactional
    @CachePut
    public Teacher update(Long id, Teacher entity) {
        Teacher teacher = repository.findById(id)
                .orElseThrow(() -> new IllegalStateException("teacher does not exist"));

        if(entity.getFirstName() != null && !entity.getFirstName().isEmpty() &&
                !Objects.equals(entity.getFirstName(), teacher.getFirstName())){
            teacher.setFirstName(entity.getFirstName());
        }

        if(entity.getLastName() != null && !entity.getLastName().isEmpty() &&
                !Objects.equals(entity.getLastName(), teacher.getLastName())){
            teacher.setLastName(entity.getLastName());
        }

        if(entity.getDateOfBirth() != null &&
                !Objects.equals(entity.getDateOfBirth(), teacher.getDateOfBirth())){
            teacher.setDateOfBirth(entity.getDateOfBirth());
        }

        if(entity.getDescription() != null && !entity.getDescription().isEmpty() &&
                !Objects.equals(entity.getDescription(), teacher.getDescription())){
            teacher.setDescription(entity.getDescription());
        }

        if(!Objects.equals(entity.getSalary(), teacher.getSalary())){
            teacher.setSalary(entity.getSalary());
        }

        return teacher;
    }

    @Override
    @Cacheable
    public List<Teacher> getAll() {
        return repository.findAll();
    }

    @Override
    @Cacheable
    public List<Teacher> getPaged(int limit, int offset) {
        return repository.findAll(PageRequest.of(offset, limit)).stream().toList();
    }

    @Override
    @Cacheable
    public Optional<Teacher> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public boolean isValid(Teacher teacher) {
        if (teacher.getFirstName() == null || teacher.getFirstName().trim().isEmpty())
            return false;

        if (teacher.getLastName() == null || teacher.getLastName().trim().isEmpty())
            return false;

        if (teacher.getDateOfBirth() == null || teacher.getDateOfBirth().isAfter(LocalDate.now()))
            return false;

        if (teacher.getDescription() == null || teacher.getDescription().trim().isEmpty())
            return false;

        if(teacher.getSalary() < 0)
            return false;

        return true;
    }
}
