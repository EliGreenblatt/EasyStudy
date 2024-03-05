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
    private List<Meeting> meetings;
    private List<Message> messages;
    private List<ChatMessage> chatMessages;

    private int newMessage; // count new message;


    // Empty constructor required for Firebase
    public User() {}
    public User(String name,  String age, String phone,  String email){
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.email = email;
        this.newMessage = 0;
    }

    public User(String name, String password , String age, String phone, String email, String shortBio) {
        this.name = name;
        this.password = password;
        this.age = age;
        this.phone = phone;
        this.email = email;
        this.shortBio = shortBio;
        this.newMessage = 0;


    }
    public void IncNewMessage() {
        newMessage++;
    }
    public void setNewMessage(int count) {
        newMessage = count;
    }
    public int getNewMessage() {
        return newMessage;
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
    public List<Meeting> getMeetings() { return meetings; }
    public List<Message> getMessages() { return messages; }
    public void setAge(String age) {
        this.age = age;
    }
    public void setBio(String bio) {
        this.shortBio = bio;
    }
    public void setEmail(String email) {this.email = email;}
    public void setPhone(String phone) {this.phone = phone;}

    public List<ChatMessage> getChatMessages() { return chatMessages; }

    public void setChatMessages(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    public void addChatMessage(ChatMessage Cm) {
        if(this.chatMessages == null)
            this.chatMessages = new ArrayList<>();
        chatMessages.add(Cm);
        newMessage++;
        Collections.sort(this.getChatMessages());
    }
    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void setMeetings(List<Meeting> meetings) {
        this.meetings = meetings;
    }
    public void addMeeting(Meeting M) {
        if(this.meetings == null)
            this.meetings = new ArrayList<>();
        meetings.add(M);
        Collections.sort(this.getMeetings());
    }

    public void Notify(Meeting M){
        if(this.meetings == null)
            this.meetings = new ArrayList<>();
        for( Meeting meeting : meetings){
            if(meeting.equalTo(M)){
                meetings.remove(meeting);
                // Initialize Firebase database reference
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference teachersRef = database.getReference("teachers");
                teachersRef.orderByKey().equalTo(M.getPartner().getName())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot teacherSnapshot : dataSnapshot.getChildren()) {
                                        Teacher teacher = teacherSnapshot.getValue(Teacher.class);
                                        if (teacher != null && teacher.getMeetings() != null) {
                                            teacher.Notify(meeting);
                                            teachersRef.child(M.getPartnerUsername()).setValue(teacher);

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

    }
    public void NotifyToDeleteMeeting(Meeting M) {
        if(messages == null)
            messages = new ArrayList<>();
        if(this.meetings == null)
            this.meetings = new ArrayList<>();
        for( Meeting meeting : meetings) {
            if (meeting.equalTo(M)) {
                meetings.remove(meeting);
                this.getMessages().add(new Message("The meeting: " + meeting
                        + "\n was cancelled"));
                this.newMessage++;
                Collections.sort(this.getMessages());
                return;
            }
        }
    }


    public String toString() {
        return
                "name:" + name +
                        ", age:" + age +
                        ",phone:" + phone +"\n" +
                        "email:" + email + "\n" +
                        "ShortBio:" + shortBio + "\n";

    }
}

