package com.example.engtutor.controllers;

import com.example.engtutor.models.Lesson;
import com.example.engtutor.models.Group;
import com.example.engtutor.models.Teacher;
import com.example.engtutor.services.Service;
import com.example.engtutor.viewmodel.ErrorViewModel;
import com.example.engtutor.viewmodel.LessonViewModel;
import com.example.engtutor.viewmodel.SnapshotViewModel;
import com.example.engtutor.viewmodel.ViewModelBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@EnableCaching
@RequestMapping("api/v1/lessons")
public class LessonController extends ControllerBase<Lesson>{

    private final Service<Group> groupService;
    private final Service<Teacher> teacherService;

    @Autowired
    public LessonController(Service<Lesson> lessonService, Service<Group> groupService, Service<Teacher> teacherService) {
        super(lessonService);
        this.groupService = groupService;
        this.teacherService = teacherService;
    }

    @GetMapping
    public ResponseEntity<Object> getLessons(){
        return handleResponse(obj -> getViewModels(service.getAll()), HttpStatus.OK);
    }

    @GetMapping(params={"limit", "offset"})
    public ResponseEntity<Object> getPagedLessons(@RequestParam("limit") int limit,
                                                  @RequestParam(value="offset", required = false) int offset){
        return handleResponse(obj -> getViewModels(service.getPaged(limit, offset)), HttpStatus.OK);
    }

    @GetMapping(path = "{lessonId}")
    public ResponseEntity<Object> getLesson(@PathVariable("lessonId") Long id){
        return handleResponse(obj -> getById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> addLesson(@RequestBody LessonViewModel lessonVM){
        return handleResponse(obj -> {
            var group = groupService.getById(lessonVM.groupId)
                    .orElseThrow(() -> new NullPointerException("group not found"));
            var teacher = teacherService.getById(lessonVM.teacherId)
                    .orElseThrow(() -> new NullPointerException("teacher not found"));

            Lesson lesson = new Lesson(
                    lessonVM.title,
                    lessonVM.date,
                    group,
                    teacher
            );
            service.add(lesson);
            return new LessonViewModel(lesson);
        }, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "{lessonId}")
    public ResponseEntity<Object> deleteLesson(@PathVariable("lessonId") Long id){
        return handleResponse(obj -> {
            service.remove(id);
            return new SnapshotViewModel(id);
        }, HttpStatus.OK);
    }

    @PutMapping(path = "{lessonId}")
    public ResponseEntity<Object> updateLesson(@PathVariable("lessonId") Long id,
                             @RequestParam(required = false) String title,
                             @RequestParam(required = false) LocalDateTime date,
                             @RequestParam(required = false) Long groupId,
                             @RequestParam(required = false) Long teacherId){
        return handleResponse(obj -> {
            var group = groupService.getById(groupId)
                    .orElseThrow(() -> new NullPointerException("group not found"));
            var teacher = teacherService.getById(teacherId)
                    .orElseThrow(() -> new NullPointerException("teacher not found"));

            Lesson lesson = new Lesson(id, title, date, group, teacher);
            service.update(id, lesson);
            var newLesson = service.getById(id)
                    .orElseThrow(NullPointerException::new);
            return new LessonViewModel(newLesson);
        }, HttpStatus.OK);
    }
}
