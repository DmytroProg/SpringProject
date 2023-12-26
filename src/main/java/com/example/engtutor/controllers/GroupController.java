package com.example.engtutor.controllers;

import com.example.engtutor.models.Student;
import com.example.engtutor.models.Group;
import com.example.engtutor.services.Service;
import com.example.engtutor.viewmodel.ErrorViewModel;
import com.example.engtutor.viewmodel.GroupViewModel;
import com.example.engtutor.viewmodel.StudentViewModel;
import com.example.engtutor.viewmodel.ViewModelBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@EnableCaching
@RequestMapping("api/v1/groups")
public class GroupController extends ControllerBase<Group>{

    @Autowired
    public GroupController(Service<Group> groupService) {
        super(groupService);
    }

    @GetMapping(path="{groupId}/students")
    public ResponseEntity<Object> getStudents(@PathVariable("groupId") Long groupId){
        return handleResponse((obj) -> {
            List<StudentViewModel> students = new ArrayList<>();
            var group = service.getById(groupId)
                    .orElseThrow(() -> new NullPointerException("group not found"));

            for(Student student : group.getStudents()){
                StudentViewModel studentVM = new StudentViewModel(student);
                students.add(studentVM);
            }

            return students;
        }, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> getGroups(){
        return handleResponse(obj -> getViewModels(service.getAll()), HttpStatus.OK);
    }

    @GetMapping(params={"limit", "offset"})
    public ResponseEntity<Object> getPagedGroups(@RequestParam("limit") int limit,
                                               @RequestParam(value="offset", required = false) int offset){
        return handleResponse(obj -> getViewModels(service.getPaged(limit, offset)), HttpStatus.OK);
    }

    @GetMapping("{groupId}")
    public ResponseEntity<Object> getGroup(@PathVariable("groupId") Long id){
        return handleResponse(obj -> getById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> addGroup(@RequestBody GroupViewModel groupVM){
        return handleResponse(obj -> {
            Group group = new Group(groupVM.id, groupVM.name);
            service.add(group);
            return new GroupViewModel(group);
        }, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "{groupId}")
    public ResponseEntity<Object> deleteGroup(@PathVariable("groupId") Long id){
        return handleResponse(obj -> {
            service.remove(id);
            return null;
        }, HttpStatus.OK);
    }

    @PutMapping(path = "{groupId}")
    public ResponseEntity<Object> updateGroup(@PathVariable("groupId") Long groupId,
                              @RequestParam(required = false) String name){
        return handleResponse(obj -> {
            Group group = new Group(groupId, name);
            service.update(groupId, group);
            return new GroupViewModel(group);
        }, HttpStatus.OK);
    }
}
