package com.example.engtutor.viewmodel;

import com.example.engtutor.models.*;

public class ViewModelFactory {

    public static <T> ViewModelBase createViewModel(T item){
        if(item instanceof Student student) return new StudentViewModel(student);
        else if(item instanceof Group group) return new GroupViewModel(group);
        else if(item instanceof Teacher teacher) return new TeacherViewModel(teacher);
        else if(item instanceof Lesson lesson) return new LessonViewModel(lesson);

        throw new IllegalArgumentException();
    }
}
