package com.example.engtutor.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="_groups")
@NoArgsConstructor
@Getter
@Setter
public class Group implements Serializable {
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

    @OneToMany(mappedBy = "group")
    private List<Student> students;

    public Group(String name) {
        this.name = name;
    }
    public Group(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString(){
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}