package com.example.myapplication;

import java.util.Date;

public class DiaryEntry {
    private Date date;
    private String student;
    private String subject;
    private String time;
    private boolean approved;
    private String content;

    public DiaryEntry() {
        // Default constructor required for Firebase
    }

    public DiaryEntry(Date date, String student, String subject, String time, String content) {
        this.date = date;
        this.student = student;
        this.subject = subject;
        this.time = time;
        this.approved = false;
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
