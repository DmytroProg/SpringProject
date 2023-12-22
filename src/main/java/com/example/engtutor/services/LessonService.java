package com.example.engtutor.services;

import com.example.engtutor.models.Lesson;
import com.example.engtutor.models.StudentsGroup;
import com.example.engtutor.models.Teacher;
import com.example.engtutor.repository.GroupRepository;
import com.example.engtutor.repository.TeacherRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@org.springframework.stereotype.Service
@CacheConfig(cacheNames = "lessons")
public class LessonService extends Service<Lesson> {

    private final GroupRepository groupRepository;
    private final TeacherRepository teacherRepository;

    @Autowired
    public LessonService(JpaRepository<Lesson, Long> lessonRepository, GroupRepository groupRepository, TeacherRepository teacherRepository) {
        super(lessonRepository);
        this.groupRepository = groupRepository;
        this.teacherRepository = teacherRepository;
    }

    @Override
    @Transactional
    @CachePut
    public Lesson update(Long id, Lesson entity) {
        Lesson lesson = repository.findById(id)
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
    @Cacheable
    public List<Lesson> getAll() {
        return repository.findAll();
    }

    @Override
    @Cacheable
    public List<Lesson> getPaged(int limit, int offset) {
        return repository.findAll(PageRequest.of(offset, limit)).stream().toList();
    }

    @Override
    @Cacheable
    public Optional<Lesson> getById(Long id) {
        return repository.findById(id);
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
