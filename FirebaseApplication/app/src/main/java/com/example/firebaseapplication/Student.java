package com.example.firebaseapplication;

public class Student {
    public String name;
    public String age;
    public String course;

    // Default constructor required for calls to DataSnapshot.getValue(Student.class)
    public Student() {
    }

    public Student(String name, String age, String course) {
        this.name = name;
        this.age = age;
        this.course = course;
    }
}
