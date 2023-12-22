package com.example.engtutor.viewmodel;

import com.example.engtutor.models.Lesson;

import java.time.LocalDateTime;

public class LessonViewModel extends ViewModelBase{
    public Long id;
    public String title;
    public LocalDateTime date;
    public Long groupId;
    public GroupViewModel group;
    public Long teacherId;
    public TeacherViewModel teacher;

    public LessonViewModel(){

    }
    public LessonViewModel(Lesson lesson){
        id = lesson.getId();
        title = lesson.getTitle();
        date = lesson.getDate();
        groupId = lesson.getGroup().getId();
        group = new GroupViewModel(lesson.getGroup());
        teacherId = lesson.getTeacher().getId();
        teacher = new TeacherViewModel(lesson.getTeacher());
    }
}
