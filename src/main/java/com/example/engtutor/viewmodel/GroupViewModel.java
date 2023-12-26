package com.example.engtutor.viewmodel;

import com.example.engtutor.models.Group;

public class GroupViewModel extends ViewModelBase{
    public Long id;
    public String name;

    public GroupViewModel(){

    }

    public GroupViewModel(Group group){
        id = group.getId();
        name = group.getName();
    }
}
