package com.example.engtutor.services;

import com.example.engtutor.models.Lesson;
import com.example.engtutor.models.StudentsGroup;
import com.example.engtutor.models.Teacher;
import com.example.engtutor.repository.GroupRepository;
import com.example.engtutor.repository.LessonRepository;
import com.example.engtutor.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@org.springframework.stereotype.Service
public class LessonService implements Service<Lesson>{

    private final LessonRepository lessonRepository;
    private final GroupRepository groupRepository;
    private final TeacherRepository teacherRepository;

    @Autowired
    public LessonService(LessonRepository lessonRepository, GroupRepository groupRepository, TeacherRepository teacherRepository) {
        this.lessonRepository = lessonRepository;
        this.groupRepository = groupRepository;
        this.teacherRepository = teacherRepository;
    }

    @Override
    public Lesson add(Lesson entity) {
        if(!isValid(entity)) throw new IllegalArgumentException();
        return lessonRepository.save(entity);
    }

    @Override
    public void remove(Long id) {
        lessonRepository.deleteById(id);
    }

    @Override
    public Lesson update(Long id, Lesson entity) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("student does not exist"));

        if(entity.getTitle() != null && !entity.getTitle().isEmpty() &&
                !Objects.equals(entity.getTitle(), lesson.getTitle())){
            lesson.setTitle(entity.getTitle());
        }

        if(entity.getDate() != null &&
                !Objects.equals(entity.getDate(), lesson.getDate())){
            lesson.setDate(entity.getDate());
        }

        if(entity.getGroup() != null && entity.getGroup().getId() != null &&
                !Objects.equals(entity.getGroup().getId(), lesson.getGroup().getId())){
            StudentsGroup group = groupRepository.findById(entity.getGroup().getId())
                    .orElseThrow(() -> new IllegalStateException("group does not exist"));
            lesson.setGroup(group);
        }

        if(entity.getTeacher() != null && entity.getTeacher().getId() != null &&
                !Objects.equals(entity.getTeacher().getId(), lesson.getTeacher().getId())){
            Teacher teacher = teacherRepository.findById(entity.getTeacher().getId())
                    .orElseThrow(() -> new IllegalStateException("teacher does not exist"));
            lesson.setTeacher(teacher);
        }

        return lesson;
    }

    @Override
    public List<Lesson> getAll() {
        return lessonRepository.findAll();
    }

    @Override
    public Optional<Lesson> getById(Long id) {
        return lessonRepository.findById(id);
    }

    @Override
    public boolean isValid(Lesson lesson) {
        if (lesson.getTitle() == null || lesson.getTitle().trim().isEmpty())
            return false;

        if (lesson.getDate() == null)
            return false;

        return true;
    }
}
