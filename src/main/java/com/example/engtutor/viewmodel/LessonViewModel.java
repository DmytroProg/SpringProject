package com.example.engtutor.viewmodel;

import com.example.engtutor.models.Lesson;
import com.example.engtutor.models.StudentsGroup;
import com.example.engtutor.models.Teacher;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

import java.time.LocalDateTime;

public class LessonViewModel {
    public Long id;
    public String title;
    public LocalDateTime date;
    public Long groupId;
    public String groupName;
    public TeacherViewModel teacher;

    public LessonViewModel(){

    }
    public LessonViewModel(Lesson lesson){
        id = lesson.getId();
        title = lesson.getTitle();
        date = lesson.getDate();
        groupId = lesson.getGroup().getId();
        groupName = lesson.getGroup().getName();
        teacher = new TeacherViewModel(lesson.getTeacher());
    }
}
