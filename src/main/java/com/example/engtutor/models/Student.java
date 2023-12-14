package com.example.engtutor.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Table
public class Student{

    @Id
    @SequenceGenerator(
            name = "student_sequence",
            sequenceName = "student_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "student_sequence"
    )
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    @Transient
    private int age;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="group_id")
    private StudentsGroup studentsGroup;

    public Student(){
    }
    public Student(Long id, String firstName, String lastName, LocalDate dateOfBirth, StudentsGroup studentsGroup) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.studentsGroup = studentsGroup;
    }

    public Student(String firstName, String lastName, LocalDate dateOfBirth, StudentsGroup studentsGroup) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.studentsGroup = studentsGroup;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getAge() {
        age = Period.between(dateOfBirth, LocalDate.now()).getYears();
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public StudentsGroup getGroup() {
        return studentsGroup;
    }

    public void setGroup(StudentsGroup studentsGroup) {
        this.studentsGroup = studentsGroup;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", age=" + age +
                '}';
    }


}
