package com.example.engtutor.helpers;

public class StringHelper {
    public static boolean isEmpty(String value){
        return value == null || value.trim().isEmpty();
    }
}
