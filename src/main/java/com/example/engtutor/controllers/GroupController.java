package com.example.engtutor.controllers;

import com.example.engtutor.models.Student;
import com.example.engtutor.models.StudentsGroup;
import com.example.engtutor.services.Service;
import com.example.engtutor.viewmodel.GroupViewModel;
import com.example.engtutor.viewmodel.StudentViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/groups")
public class GroupController {
    private final Service<StudentsGroup> groupService;

    @Autowired
    public GroupController(Service<StudentsGroup> groupService) {

        this.groupService = groupService;
    }

    @GetMapping(path="{groupId}/students")
    public List<StudentViewModel> getStudents(@PathVariable("groupId") Long groupId){
        List<StudentViewModel> students = new ArrayList<>();
        Optional<StudentsGroup> group = groupService.getById(groupId);
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
    public List<GroupViewModel> getGroups(){
        List<GroupViewModel> groups = new ArrayList<>();
        for(StudentsGroup g : groupService.getAll()){
            GroupViewModel groupVM = new GroupViewModel();
            groupVM.id = g.getId();
            groupVM.name = g.getName();
            groups.add(groupVM);
        }
        return groups;
    }

    @GetMapping("{groupId}")
    public GroupViewModel getGroup(@PathVariable("groupId") Long id){
        StudentsGroup group = groupService.getById(id)
                .orElseThrow(() -> new IllegalStateException("group does not exist"));

        return new GroupViewModel(group);
    }

    @PostMapping
    public void addGroup(@RequestBody GroupViewModel groupVM){
        StudentsGroup group = new StudentsGroup(groupVM.id, groupVM.name);
        groupService.add(group);
    }

    @DeleteMapping(path = "{groupId}")
    public void deleteGroup(@PathVariable("groupId") Long id){
        groupService.remove(id);
    }

    @PutMapping(path = "{groupId}")
    public void updateGroup(@PathVariable("groupId") Long groupId,
                              @RequestParam(required = false) String name){

        StudentsGroup group = new StudentsGroup(groupId, name);
        groupService.update(groupId, group);
    }
}
