package com.example.engtutor.controllers;

import com.example.engtutor.models.Lesson;
import com.example.engtutor.models.StudentsGroup;
import com.example.engtutor.models.Teacher;
import com.example.engtutor.services.Service;
import com.example.engtutor.viewmodel.LessonViewModel;
import com.example.engtutor.viewmodel.ViewModelBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@EnableCaching
@RequestMapping("api/v1/lessons")
public class LessonController extends ControllerBase<Lesson>{

    private final Service<StudentsGroup> groupService;
    private final Service<Teacher> teacherService;

    @Autowired
    public LessonController(Service<Lesson> lessonService, Service<StudentsGroup> groupService, Service<Teacher> teacherService) {
        super(lessonService);
        this.groupService = groupService;
        this.teacherService = teacherService;
    }

    @GetMapping
    public List<ViewModelBase> getLessons(){
        return getViewModels(service.getAll());
    }

    @GetMapping(params={"limit", "offset"})
    public List<ViewModelBase> getPagedLessons(@RequestParam("limit") int limit,
                                               @RequestParam("offset") int offset){
        return getViewModels(service.getPaged(limit, offset));
    }

    @GetMapping(path = "{lessonId}")
    public ViewModelBase getLesson(@PathVariable("lessonId") Long id){
        return getById(id);
    }

    @PostMapping
    public void addLesson(@RequestBody LessonViewModel lessonVM){
        StudentsGroup group = groupService.getById(lessonVM.groupId)
                .orElseThrow(() -> new IllegalStateException("group does not exist"));
        Teacher teacher = teacherService.getById(lessonVM.teacherId)
                .orElseThrow(() -> new IllegalStateException("teacher does not exist"));
        Lesson lesson = new Lesson(
                lessonVM.title,
                lessonVM.date,
                group,
                teacher
        );
        service.add(lesson);
    }

    @DeleteMapping(path = "{lessonId}")
    public void deleteLesson(@PathVariable("lessonId") Long id){
        service.remove(id);
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
        service.update(id, lesson);
    }
}
