package com.example.engtutor.services;

import com.example.engtutor.models.Student;
import com.example.engtutor.models.StudentsGroup;
import com.example.engtutor.repository.GroupRepository;
import com.example.engtutor.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@org.springframework.stereotype.Service
public class StudentService implements Service<Student>{

    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository, GroupRepository groupRepository) {
        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
    }

    @Override
    public Student add(Student entity) {
        if(!isValid(entity)) throw new IllegalArgumentException();
        return studentRepository.save(entity);
    }

    @Override
    public void remove(Long id) {
        studentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Student update(Long id, Student entity) {
        Student student = studentRepository.findById(id)
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
    public List<Student> getAll() {
        return studentRepository.findAll();
    }

    @Override
    public Optional<Student> getById(Long id) {
        return studentRepository.findById(id);
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
