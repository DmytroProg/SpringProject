package com.example.engtutor.viewmodel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ErrorViewModel extends ViewModelBase{
    private String message;

    public ErrorViewModel(Exception ex){
        message = ex.getMessage();
    }
}
