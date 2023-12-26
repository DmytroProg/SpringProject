package com.example.engtutor.services;

import com.example.engtutor.helpers.Output;
import com.example.engtutor.helpers.StringHelper;
import com.example.engtutor.models.Lesson;
import com.example.engtutor.models.Group;
import com.example.engtutor.models.Teacher;
import com.example.engtutor.repository.GroupRepository;
import com.example.engtutor.repository.LessonRepository;
import com.example.engtutor.repository.TeacherRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@org.springframework.stereotype.Service
@CacheConfig(cacheNames = "lessons")
public class LessonService implements Service<Lesson> {

    private final LessonRepository repository;
    private final GroupRepository groupRepository;
    private final TeacherRepository teacherRepository;

    @Autowired
    public LessonService(LessonRepository repository, GroupRepository groupRepository, TeacherRepository teacherRepository) {
        this.repository = repository;
        this.groupRepository = groupRepository;
        this.teacherRepository = teacherRepository;
    }

    @Override
    @CachePut(key="#entity.id")
    public Lesson add(Lesson entity) {
        validate(entity);
        return repository.save(entity);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(key = "'allLessons'"),
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
            @CacheEvict(key = "'allLessons'"),
            @CacheEvict(key = "#id")
    })
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

        if(entity.getGroup() != null &&
                !Objects.equals(entity.getGroup().getId(), lesson.getGroup().getId())){
            Group group = groupRepository.findById(entity.getGroup().getId())
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
    @Cacheable(key="'allLessons'")
    public List<Lesson> getAll() {
        return repository.findAll();
    }

    @Override
    @Cacheable(key="'allLessons'")
    public List<Lesson> getPaged(int limit, int offset) {
        return repository.findAll(PageRequest.of(offset, limit)).stream().toList();
    }

    @Override
    @Cacheable(key="#id")
    public Optional<Lesson> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void validate(Lesson lesson) {
        if (StringHelper.isEmpty(lesson.getTitle()))
            throw new IllegalArgumentException("title" + Output.EMPTY_VALUE);

        if(lesson.getDate() == null)
            throw new IllegalArgumentException("date" + Output.EMPTY_VALUE);
    }
}
