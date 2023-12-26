package com.example.engtutor.controllers;

import com.example.engtutor.models.Lesson;
import com.example.engtutor.models.Teacher;
import com.example.engtutor.services.Service;
import com.example.engtutor.viewmodel.LessonViewModel;
import com.example.engtutor.viewmodel.TeacherViewModel;
import com.example.engtutor.viewmodel.ViewModelBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@EnableCaching
@RequestMapping("api/v1/teachers")
public class TeacherController extends ControllerBase<Teacher>{

    @Autowired
    public TeacherController(Service<Teacher> teacherService) {
        super(teacherService);
    }

    @GetMapping
    public ResponseEntity<Object> getTeachers(){
        return handleResponse(obj -> getViewModels(service.getAll()), HttpStatus.OK);
    }

    @GetMapping(params={"limit", "offset"})
    public ResponseEntity<Object> getPagedTeachers(@RequestParam("limit") int limit,
                                               @RequestParam(value="offset", required = false) int offset){
        return handleResponse(obj -> getViewModels(service.getPaged(limit, offset)), HttpStatus.OK);
    }

    @GetMapping(path = "{teacherId}")
    public ResponseEntity<Object> getTeacher(@PathVariable("teacherId") Long id){
        return handleResponse(obj -> getById(id), HttpStatus.OK);
    }

    @GetMapping(path = "{teacherId}/lessons")
    public ResponseEntity<Object> getLessons(@PathVariable("teacherId") Long id){
        return handleResponse(obj -> {
            List<LessonViewModel> lessons = new ArrayList<>();
            var teacher = service.getById(id)
                    .orElseThrow(() -> new NullPointerException("teacher not found"));
            for (Lesson lesson : teacher.getLessons()) {
                LessonViewModel lessonVM = new LessonViewModel(lesson);
                lessons.add(lessonVM);
            }
            return lessons;
        }, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> addTeacher(@RequestBody TeacherViewModel teacherViewModel){
        return handleResponse(obj -> {
            Teacher teacher = new Teacher(
                    teacherViewModel.firstName,
                    teacherViewModel.lastName,
                    teacherViewModel.dateOfBirth,
                    teacherViewModel.description,
                    teacherViewModel.salary
            );
            service.add(teacher);
            return new TeacherViewModel(teacher);
        }, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "{teacherId}")
    public ResponseEntity<Object> deleteTeacher(@PathVariable("teacherId") Long id){
        return handleResponse(obj -> {
            service.remove(id);
            return null;
        }, HttpStatus.OK);
    }

    @PutMapping(path = "{teacherId}")
    public ResponseEntity<Object> updateTeacher(@PathVariable("teacherId") Long id,
                              @RequestParam(required = false) String firstName,
                              @RequestParam(required = false) String lastName,
                              @RequestParam(required = false) LocalDate dateOfBirth,
                              @RequestParam(required = false) String description,
                              @RequestParam(required = false) int salary){
        return handleResponse(obj -> {
            Teacher teacher = new Teacher(id, firstName, lastName, dateOfBirth, description, salary);
            service.update(id, teacher);
            return new TeacherViewModel(teacher);
        }, HttpStatus.OK);
    }
}
