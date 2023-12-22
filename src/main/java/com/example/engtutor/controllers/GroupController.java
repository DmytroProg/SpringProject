package com.example.engtutor.controllers;

import com.example.engtutor.models.Student;
import com.example.engtutor.models.StudentsGroup;
import com.example.engtutor.services.Service;
import com.example.engtutor.viewmodel.GroupViewModel;
import com.example.engtutor.viewmodel.StudentViewModel;
import com.example.engtutor.viewmodel.ViewModelBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/groups")
public class GroupController extends ControllerBase<StudentsGroup>{

    @Autowired
    public GroupController(Service<StudentsGroup> groupService) {
        super(groupService);
    }

    @GetMapping(path="{groupId}/students")
    public List<StudentViewModel> getStudents(@PathVariable("groupId") Long groupId){
        List<StudentViewModel> students = new ArrayList<>();
        Optional<StudentsGroup> group = service.getById(groupId);
        if(group.isEmpty()){
            throw new IllegalStateException("group does not exist");
        }

        for(Student st : group.get().getStudents()){
            StudentViewModel studentVM = new StudentViewModel(st);
            students.add(studentVM);
        }
        return students;
    }

    @GetMapping
    public List<ViewModelBase> getGroups(){
        return getViewModels(service.getAll());
    }

    @GetMapping("{groupId}")
    public ViewModelBase getGroup(@PathVariable("groupId") Long id){
        return getById(id);
    }

    @PostMapping
    public void addGroup(@RequestBody GroupViewModel groupVM){
        StudentsGroup group = new StudentsGroup(groupVM.id, groupVM.name);
        service.add(group);
    }

    @DeleteMapping(path = "{groupId}")
    public void deleteGroup(@PathVariable("groupId") Long id){
        service.remove(id);
    }

    @PutMapping(path = "{groupId}")
    public void updateGroup(@PathVariable("groupId") Long groupId,
                              @RequestParam(required = false) String name){

        StudentsGroup group = new StudentsGroup(groupId, name);
        service.update(groupId, group);
    }
}
