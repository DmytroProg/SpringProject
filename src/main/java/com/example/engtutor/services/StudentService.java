package com.example.engtutor.services;

import com.example.engtutor.helpers.Output;
import com.example.engtutor.helpers.StringHelper;
import com.example.engtutor.models.Student;
import com.example.engtutor.models.Group;
import com.example.engtutor.repository.GroupRepository;
import com.example.engtutor.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@org.springframework.stereotype.Service
@CacheConfig(cacheNames = "students")
public class StudentService implements Service<Student> {

    private final StudentRepository repository;
    private final GroupRepository groupRepository;

    @Autowired
    public StudentService(StudentRepository repository, GroupRepository groupRepository){
        this.repository = repository;
        this.groupRepository = groupRepository;
    }

    @Override
    @CachePut(key="#entity.id")
    public Student add(Student entity) {
        validate(entity);
        return repository.save(entity);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(key = "'allStudents'"),
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
            @CacheEvict(key = "'allStudents'"),
            @CacheEvict(key = "#id")
    })
    public Student update(Long id, Student entity) {
        Student student = repository.findById(id)
                .orElseThrow(() -> new IllegalStateException("student does not exist"));

        if(entity.getFirstName() != null &&
           !Objects.equals(entity.getFirstName(), student.getFirstName())){
            student.setFirstName(entity.getFirstName());
        }

        if(entity.getLastName() != null &&
                !Objects.equals(entity.getLastName(), student.getLastName())){
            student.setLastName(entity.getLastName());
        }

        if(entity.getDateOfBirth() != null &&
                !Objects.equals(entity.getDateOfBirth(), student.getDateOfBirth())){
            student.setDateOfBirth(entity.getDateOfBirth());
        }

        if(entity.getGroup() != null && entity.getGroup().getId() != null &&
                !Objects.equals(entity.getGroup().getId(), student.getGroup().getId())){
            Group group = groupRepository.findById(entity.getGroup().getId())
                            .orElseThrow(() -> new IllegalStateException("group does not exist"));
            student.setGroup(group);
        }

        return student;
    }

    @Override
    @Cacheable(key="'allStudents'")
    public List<Student> getAll() {
        return repository.findAll();
    }

    @Override
    @Cacheable(key="'allStudents'")
    public List<Student> getPaged(int limit, int offset) {
        return repository.findAll(PageRequest.of(offset, limit)).stream().toList();
    }

    @Override
    @Cacheable(key="#id")
    public Optional<Student> getById(Long id) {
        return repository.findById(id);
    }

    public void validate(Student student){
        if (StringHelper.isEmpty(student.getFirstName()))
            throw new IllegalArgumentException("first name" + Output.EMPTY_VALUE);

        if (StringHelper.isEmpty(student.getLastName()))
            throw new IllegalArgumentException("last name" + Output.EMPTY_VALUE);

        if (student.getDateOfBirth() == null || student.getDateOfBirth().isAfter(LocalDate.now()))
            throw new IllegalArgumentException("date of birth" + Output.INCORRECT_VALUE);
    }
}
