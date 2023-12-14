package com.example.engtutor.viewmodel;

import com.example.engtutor.models.Student;

import java.time.LocalDate;

public class StudentViewModel {
    public Long id;
    public String firstName;
    public String lastName;
    public LocalDate dateOfBirth;
    public int age;
    public Long groupId;
    public String groupName;

    public StudentViewModel(){

    }
    public StudentViewModel(Student student){
        id = student.getId();
        firstName = student.getFirstName();
        lastName = student.getLastName();
        dateOfBirth = student.getDateOfBirth();
        age = student.getAge();
        groupId = student.getGroup().getId();
        groupName = student.getGroup().getName();
    }
}
