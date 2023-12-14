package com.example.engtutor.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table
public class StudentsGroup {
    @Id
    @SequenceGenerator(
            name = "group_sequence",
            sequenceName = "group_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "group_sequence"
    )
    private Long id;
    private String name;

    @OneToMany(mappedBy = "studentsGroup")
    private List<Student> students;

    public StudentsGroup() {
    }
    public StudentsGroup(String name) {
        this.name = name;
    }
    public StudentsGroup(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    @Override
    public String toString(){
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}