package com.example.myapplication;

import java.util.List;

public class Teacher {
    private String name;
    private String age;
    private String password;
    private String phone;
    private String email;
    private String shortBio;
    private List<String> subjects;
    private List<String> links;

    // Empty constructor required for Firebase
    public Teacher() {
    }

    public Teacher(String name,String password ,String age, String phone, String email, String shortBio, List<String> subjects) {
        this.name = name;
        this.password = password;
        this.age = age;
        this.phone = phone;
        this.email = email;
        this.shortBio = shortBio;
        this.subjects = subjects;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
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

    public List<String> getSubjects() {
        return subjects;
    }

    public String getAge() { return age; }

    public List<String> getLinks() { return links; }

    @Override
    public String toString() {
        return
                "name:" + name +
                ", age:" + age +
                ", phone:" + phone +
                ", email:" + email +
                ", subjects:" + subjects +
                ", links:" + links +
                "                           ";
    }

    public void addLink(String link) {
        if(link != null)
        {
            this.links.add(link);
        }

    }


    // TODO: Add getters and setters for other attributes


}

