package com.example.engtutor.services;

import com.example.engtutor.models.Student;
import com.example.engtutor.models.StudentsGroup;
import com.example.engtutor.repository.GroupRepository;
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
@CacheConfig(cacheNames = "students")
public class StudentService extends Service<Student> {
    private final GroupRepository groupRepository;

    @Autowired
    public StudentService(JpaRepository<Student, Long> studentService, GroupRepository groupRepository){
        super(studentService);
        this.groupRepository = groupRepository;
    }

    @Override
    @Transactional
    @CachePut
    public Student update(Long id, Student entity) {
        Student student = repository.findById(id)
                .orElseThrow(() -> new IllegalStateException("student does not exist"));

        if(entity.getFirstName() != null && !entity.getFirstName().isEmpty() &&
           !Objects.equals(entity.getFirstName(), student.getFirstName())){
            student.setFirstName(entity.getFirstName());
        }

        if(entity.getLastName() != null && !entity.getLastName().isEmpty() &&
                !Objects.equals(entity.getLastName(), student.getLastName())){
            student.setLastName(entity.getLastName());
        }

        if(entity.getDateOfBirth() != null &&
                !Objects.equals(entity.getDateOfBirth(), student.getDateOfBirth())){
            student.setDateOfBirth(entity.getDateOfBirth());
        }

        if(entity.getGroup() != null && entity.getGroup().getId() != null &&
                !Objects.equals(entity.getGroup().getId(), student.getGroup().getId())){
            StudentsGroup group = groupRepository.findById(entity.getGroup().getId())
                            .orElseThrow(() -> new IllegalStateException("group does not exist"));
            student.setGroup(group);
        }

        return student;
    }

    @Override
    @Cacheable
    public List<Student> getAll() {
        return repository.findAll();
    }

    @Override
    @Cacheable
    public List<Student> getPaged(int limit, int offset) {
        return repository.findAll(PageRequest.of(offset, limit)).stream().toList();
    }

    @Override
    @Cacheable
    public Optional<Student> getById(Long id) {
        return repository.findById(id);
    }

    public boolean isValid(Student student){
        if (student.getFirstName() == null || student.getFirstName().trim().isEmpty())
            return false;

        if (student.getLastName() == null || student.getLastName().trim().isEmpty())
            return false;

        if (student.getDateOfBirth() == null || student.getDateOfBirth().isAfter(LocalDate.now()))
            return false;

        return true;
    }
}
