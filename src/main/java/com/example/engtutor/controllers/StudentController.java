package com.example.engtutor.controllers;

import com.example.engtutor.models.Group;
import com.example.engtutor.viewmodel.ErrorViewModel;
import com.example.engtutor.viewmodel.SnapshotViewModel;
import com.example.engtutor.viewmodel.StudentViewModel;
import com.example.engtutor.services.Service;
import com.example.engtutor.models.Student;
import com.example.engtutor.viewmodel.ViewModelBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@EnableCaching
@RequestMapping("api/v1/students")
public class StudentController extends ControllerBase<Student>{

    private final Service<Group> groupService;

    @Autowired
    public StudentController(Service<Student> studentService, Service<Group> groupService) {
        super(studentService);
        this.groupService = groupService;
    }

    @GetMapping
    public ResponseEntity<Object> getStudents(){
        return handleResponse(obj -> getViewModels(service.getAll()), HttpStatus.OK);
    }

    @GetMapping(params={"limit", "offset"})
    public ResponseEntity<Object> getPagedStudents(@RequestParam("limit") int limit,
                                               @RequestParam(value="offset", required = false) int offset){
        return handleResponse(obj -> getViewModels(service.getPaged(limit, offset)), HttpStatus.OK);
    }

    @GetMapping(path = "{studentId}")
    public ResponseEntity<Object> getStudent(@PathVariable("studentId") Long id){
        return handleResponse(obj -> getById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> addStudent(@RequestBody StudentViewModel studentViewModel){
        return handleResponse(obj -> {
            var group = groupService.getById(studentViewModel.groupId)
                    .orElseThrow(() -> new NullPointerException("group not found"));

            Student student = new Student(
                    studentViewModel.firstName,
                    studentViewModel.lastName,
                    studentViewModel.dateOfBirth,
                    group
            );
            service.add(student);
            return new StudentViewModel(student);
        }, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "{studentId}")
    public ResponseEntity<Object> deleteStudent(@PathVariable("studentId") Long id){
        return handleResponse(obj -> {
            service.remove(id);
            return new SnapshotViewModel(id);
        }, HttpStatus.OK);
    }

    @PutMapping(path = "{studentId}")
    public ResponseEntity<Object> updateStudent(@PathVariable("studentId") Long studentId,
                              @RequestParam(required = false) String firstName,
                              @RequestParam(required = false) String lastName,
                              @RequestParam(required = false) LocalDate dateOfBirth,
                              @RequestParam(required = false) Long groupId){
        return handleResponse(obj -> {
            var group = groupService.getById(groupId)
                    .orElseThrow(() -> new NullPointerException("group not found"));
            Student student = new Student(studentId, firstName, lastName, dateOfBirth, group);
            service.update(studentId, student);
            var newStudent = service.getById(studentId)
                    .orElseThrow(NullPointerException::new);
            return new StudentViewModel(newStudent);
        }, HttpStatus.OK);
    }
}
