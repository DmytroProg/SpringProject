package com.example.engtutor.controllers;

import com.example.engtutor.models.Lesson;
import com.example.engtutor.models.Student;
import com.example.engtutor.models.StudentsGroup;
import com.example.engtutor.models.Teacher;
import com.example.engtutor.services.Service;
import com.example.engtutor.viewmodel.LessonViewModel;
import com.example.engtutor.viewmodel.StudentViewModel;
import com.example.engtutor.viewmodel.TeacherViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/teachers")
public class TeacherController {

    private final Service<Teacher> teacherService;

    @Autowired
    public TeacherController(Service<Teacher> teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping
    public List<TeacherViewModel> getTeachers(){
        List<TeacherViewModel> teachers = new ArrayList<>();
        for(Teacher t : teacherService.getAll()){
            TeacherViewModel teacherVM = new TeacherViewModel(t);
            teachers.add(teacherVM);
        }
        return teachers;
    }

    @GetMapping(path = "{teacherId}")
    public TeacherViewModel getTeacher(@PathVariable("teacherId") Long id){
        Teacher teacher = teacherService.getById(id)
                .orElseThrow(() -> new IllegalStateException("teacher does not exist"));

        return new TeacherViewModel(teacher);
    }

    @GetMapping(path = "{teacherId}/lessons")
    public List<LessonViewModel> getLessons(@PathVariable("teacherId") Long id){
        List<LessonViewModel> lessons = new ArrayList<>();
        Teacher teacher = teacherService.getById(id)
                .orElseThrow(() -> new IllegalStateException("teacher does not exist"));
        for(Lesson l : teacher.getLessons()){
            LessonViewModel lessonVM = new LessonViewModel(l);
            lessons.add(lessonVM);
        }
        return lessons;
    }

    @PostMapping
    public void addTeacher(@RequestBody TeacherViewModel teacherViewModel){
        Teacher teacher = new Teacher(
                teacherViewModel.firstName,
                teacherViewModel.lastName,
                teacherViewModel.dateOfBirth,
                teacherViewModel.description,
                teacherViewModel.salary
            );
        teacherService.add(teacher);
    }

    @DeleteMapping(path = "{teacherId}")
    public void deleteTeacher(@PathVariable("teacherId") Long id){
        teacherService.remove(id);
    }

    @PutMapping(path = "{teacherId}")
    public void updateTeacher(@PathVariable("teacherId") Long id,
                              @RequestParam(required = false) String firstName,
                              @RequestParam(required = false) String lastName,
                              @RequestParam(required = false) LocalDate dateOfBirth,
                              @RequestParam(required = false) String description,
                              @RequestParam(required = false) int salary){
        Teacher teacher = new Teacher(id, firstName, lastName, dateOfBirth, description, salary);
        teacherService.update(id, teacher);
    }
}
