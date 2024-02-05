package com.example.myapplication;
public class Student {
    private String name;
    private String password;
    private int age;
    private String phone;
    private String email;
    private String shortBio;

    // Empty constructor required for Firebase
    public Student() {
    }

    public Student( String name,String password ,int age, String phone, String email, String shortBio) {
        this.name = name;
        this.password = password;
        this.age = age;
        this.phone = phone;
        this.email = email;
        this.shortBio = shortBio;
    }



    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getShortBio() {
        return shortBio;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

