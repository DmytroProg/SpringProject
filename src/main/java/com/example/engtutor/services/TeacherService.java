package com.example.engtutor.services;

import com.example.engtutor.models.Teacher;
import com.example.engtutor.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@org.springframework.stereotype.Service
public class TeacherService implements Service<Teacher>{

    private final TeacherRepository teacherRepository;

    @Autowired
    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Override
    public Teacher add(Teacher entity) {
        return teacherRepository.save(entity);
    }

    @Override
    public void remove(Long id) {
        teacherRepository.deleteById(id);
    }

    @Override
    public Teacher update(Long id, Teacher entity) {
        Teacher teacher = teacherRepository.findById(id)
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
    public List<Teacher> getAll() {
        return teacherRepository.findAll();
    }

    @Override
    public Optional<Teacher> getById(Long id) {
        return teacherRepository.findById(id);
    }
}
