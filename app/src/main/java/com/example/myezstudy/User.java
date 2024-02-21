package com.example.myezstudy;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class User {

    private String name;
    private String password;
    private String age;
    private String phone;
    private String email;
    private String shortBio;
    private List<Meeting> Meetings;

    // Empty constructor required for Firebase
    public User() {}
    public User(String name,  String age, String phone,  String email){
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.email = email;
    }
    public User(String name, String password , String age, String phone, String email) {
        this.name = name;
        this.password = password;
        this.age = age;
        this.phone = phone;
        this.email = email;
    }
    public User(String name, String password , String age, String phone, String email, String shortBio) {
        this.name = name;
        this.password = password;
        this.age = age;
        this.phone = phone;
        this.email = email;
        this.shortBio = shortBio;
        this.Meetings = new ArrayList<>();
    }

    public String getName() {
        return name;
    }
    public String getAge() {
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
    public List<Meeting> getMeetings() { return Meetings; }
    public void setMeetings(List<Meeting> meetings) {
        this.Meetings = meetings;
    }
    public void Notify(Meeting M){
        if(this.Meetings == null)
            this.Meetings = new ArrayList<>();
        for( Meeting meeting : Meetings){
            if(meeting.CompareTo(M)){
                Meetings.remove(meeting);
                // Initialize Firebase database reference
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference teachersRef = database.getReference("teachers");
                teachersRef.orderByChild("name").equalTo(M.getPartner().getName())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot teacherSnapshot : dataSnapshot.getChildren()) {
                                        Teacher teacher = teacherSnapshot.getValue(Teacher.class);
                                        if (teacher != null && teacher.getMeetings() != null) {
                                            teacher.Notify(meeting);
                                            teacherSnapshot.getRef().child("meetings").setValue(teacher.getMeetings());

                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle error
                            }
                        });
                return;

            }
        }
        Meetings.add(M);
        Collections.sort(this.getMeetings());

    }
    public void NotifyToDeleteMeeting(Meeting M) {
        if(this.Meetings == null)
            this.Meetings = new ArrayList<>();
        for( Meeting meeting : Meetings) {
            if (meeting.CompareTo(M)) {
                Meetings.remove(meeting);
            }
        }
    }

    public String toString() {
        return
                "name:" + name +
                        ", age:" + age +
                        ", phone:" + phone +"\n" +
                        ", email:" + email + "\n" +
                        "ShortBio:" + shortBio + "\n";

    }
}

