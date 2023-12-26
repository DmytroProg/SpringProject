package com.example.engtutor.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Entity
@Table
@NoArgsConstructor
@Getter
@Setter
public class Teacher implements Serializable {
    @Id
    @SequenceGenerator(
            name = "teacher_sequence",
            sequenceName = "teacher_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "teacher_sequence"
    )
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    @Transient
    private int age;
    private String description;
    private int salary;

    @OneToMany(mappedBy = "teacher")
    private List<Lesson> lessons;

    public Teacher(Long id, String firstName, String lastName, LocalDate dateOfBirth, String description, int salary) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.description = description;
        this.salary = salary;
    }

    public Teacher(String firstName, String lastName, LocalDate dateOfBirth, String description, int salary) {
        this.description = description;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.salary = salary;
    }

    public int getAge() {
        age = Period.between(dateOfBirth, LocalDate.now()).getYears();
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", dateOfBirth=" + getDateOfBirth() +
                ", age=" + getAge() +
                ", description=" + getDescription() +
                ", salary=" + getSalary() +
                '}';
    }
}
