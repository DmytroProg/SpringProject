package com.example.engtutor.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Lesson implements Serializable {
    @Id
    @SequenceGenerator(
            name = "lesson_sequence",
            sequenceName = "lesson_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "lesson_sequence"
    )
    private Long id;
    private String title;
    private LocalDateTime date;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    public Lesson(String title, LocalDateTime date, Group group, Teacher teacher) {
        this.title = title;
        this.date = date;
        this.group = group;
        this.teacher = teacher;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", date=" + date +
                ", groupId=" + group.getId() +
                ", teacherId=" + teacher.getId() +
                '}';
    }
}
