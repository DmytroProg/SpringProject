package com.example.engtutor.controllers;

import com.example.engtutor.models.Lesson;
import com.example.engtutor.models.StudentsGroup;
import com.example.engtutor.models.Teacher;
import com.example.engtutor.services.Service;
import com.example.engtutor.viewmodel.LessonViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/lessons")
public class LessonController {

    private final Service<Lesson> lessonService;
    private final Service<StudentsGroup> groupService;
    private final Service<Teacher> teacherService;

    @Autowired
    public LessonController(Service<Lesson> lessonService, Service<StudentsGroup> groupService, Service<Teacher> teacherService) {
        this.lessonService = lessonService;
        this.groupService = groupService;
        this.teacherService = teacherService;
    }

    @GetMapping
    public List<LessonViewModel> getLessons(){
        List<LessonViewModel> lessons = new ArrayList<>();
        for(Lesson l : lessonService.getAll()){
            LessonViewModel lessonVM = new LessonViewModel(l);
            lessons.add(lessonVM);
        }
        return lessons;
    }

    @GetMapping(path = "{lessonId}")
    public LessonViewModel getLesson(@PathVariable("lessonId") Long id){
        Lesson lesson = lessonService.getById(id)
                .orElseThrow(() -> new IllegalStateException("lesson does not exist"));

        return new LessonViewModel(lesson);
    }

    @PostMapping
    public void addLesson(@RequestBody LessonViewModel lessonVM){
        StudentsGroup group = groupService.getById(lessonVM.groupId)
                .orElseThrow(() -> new IllegalStateException("group does not exist"));
        Teacher teacher = teacherService.getById(lessonVM.teacher.id)
                .orElseThrow(() -> new IllegalStateException("teacher does not exist"));
        Lesson lesson = new Lesson(
                lessonVM.title,
                lessonVM.date,
                group,
                teacher
        );
        lessonService.add(lesson);
    }

    @DeleteMapping(path = "{lessonId}")
    public void deleteLesson(@PathVariable("lessonId") Long id){
        lessonService.remove(id);
    }

    @PutMapping(path = "{lessonId}")
    public void updateLesson(@PathVariable("lessonId") Long id,
                             @RequestParam(required = false) String title,
                             @RequestParam(required = false) LocalDateTime date,
                             @RequestParam(required = false) Long groupId,
                             @RequestParam(required = false) Long teacherId){
        StudentsGroup group = groupService.getById(groupId)
                .orElseThrow(() -> new IllegalStateException("group does not exist"));
        Teacher teacher = teacherService.getById(teacherId)
                .orElseThrow(() -> new IllegalStateException("teacher does not exist"));

        Lesson lesson = new Lesson(id, title, date, group, teacher);
        lessonService.update(id, lesson);
    }
}
