package com.example.engtutor.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table
public class Lesson {
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
    private StudentsGroup group;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    public Lesson() {
    }
    public Lesson(String title, LocalDateTime date, StudentsGroup group, Teacher teacher) {
        this.title = title;
        this.date = date;
        this.group = group;
        this.teacher = teacher;
    }
    public Lesson(Long id, String title, LocalDateTime date, StudentsGroup group, Teacher teacher) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.group = group;
        this.teacher = teacher;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public StudentsGroup getGroup() {
        return group;
    }

    public void setGroup(StudentsGroup group) {
        this.group = group;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
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
