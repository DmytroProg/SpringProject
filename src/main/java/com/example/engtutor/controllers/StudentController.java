package com.example.engtutor.controllers;

import com.example.engtutor.models.StudentsGroup;
import com.example.engtutor.viewmodel.StudentViewModel;
import com.example.engtutor.services.Service;
import com.example.engtutor.models.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/students")
public class StudentController {

    private final Service<Student> studentService;
    private final Service<StudentsGroup> groupService;

    @Autowired
    public StudentController(Service<Student> studentService, Service<StudentsGroup> groupService) {
        this.studentService = studentService;
        this.groupService = groupService;
    }

    @GetMapping
    public List<StudentViewModel> getStudents(){
        List<StudentViewModel> students = new ArrayList<>();
        for(Student st : studentService.getAll()){
            StudentViewModel studentVM = new StudentViewModel(st);
            students.add(studentVM);
        }
        return students;
    }

    @GetMapping(path = "{studentId}")
    public StudentViewModel getStudent(@PathVariable("studentId") Long id){
        Student student = studentService.getById(id)
                .orElseThrow(() -> new IllegalStateException("student does not exist"));
        return new StudentViewModel(student);
    }

    @PostMapping
    public void addStudent(@RequestBody StudentViewModel studentViewModel){
        Optional<StudentsGroup> group = groupService.getById(studentViewModel.groupId);
        if(group.isEmpty()){
            throw new IllegalStateException("group does not exist");
        }
        else {
            Student student = new Student(
                    studentViewModel.firstName,
                    studentViewModel.lastName,
                    studentViewModel.dateOfBirth,
                    group.get()
            );
            studentService.add(student);
        }
    }

    @DeleteMapping(path = "{studentId}")
    public void deleteStudent(@PathVariable("studentId") Long id){
        studentService.remove(id);
    }

    @PutMapping(path = "{studentId}")
    public void updateStudent(@PathVariable("studentId") Long studentId,
                              @RequestParam(required = false) String firstName,
                              @RequestParam(required = false) String lastName,
                              @RequestParam(required = false) LocalDate dateOfBirth,
                              @RequestParam(required = false) Long groupId){
        StudentsGroup group = groupId != null ? groupService.getById(groupId)
                .orElseThrow(() -> new IllegalStateException("group does not exist")) : null;
        Student student = new Student(studentId, firstName, lastName, dateOfBirth, group);
        studentService.update(studentId, student);
    }
}
