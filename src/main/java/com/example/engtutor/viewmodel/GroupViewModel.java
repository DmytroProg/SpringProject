package com.example.engtutor.viewmodel;

import com.example.engtutor.models.StudentsGroup;

public class GroupViewModel {
    public Long id;
    public String name;

    public GroupViewModel(){

    }

    public GroupViewModel(StudentsGroup group){
        id = group.getId();
        name = group.getName();
    }
}
