package com.example.engtutor.viewmodel;

import com.example.engtutor.models.Teacher;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
public class TeacherViewModel extends ViewModelBase{
    public Long id;
    public String firstName;
    public String lastName;
    public LocalDate dateOfBirth;
    public int age;
    public String description;
    public int salary;

    public TeacherViewModel(Teacher teacher){
        id = teacher.getId();
        firstName = teacher.getFirstName();
        lastName = teacher.getLastName();
        dateOfBirth = teacher.getDateOfBirth();
        age = teacher.getAge();
        description = teacher.getDescription();
        salary = teacher.getSalary();
    }
}
