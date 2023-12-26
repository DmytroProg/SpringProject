package com.example.engtutor.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;

@Entity
@Table
@NoArgsConstructor
@Getter
@Setter
public class Student implements Serializable {

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
    private Group group;

    public Student(Long id, String firstName, String lastName, LocalDate dateOfBirth, Group group) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.group = group;
    }

    public Student(String firstName, String lastName, LocalDate dateOfBirth, Group group) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.group = group;
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
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", age=" + age +
                '}';
    }
}
