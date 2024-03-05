package com.example.myezstudy;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Date;
public class Teacher extends User {

    private List<String> subjects;
    private List<String> links;

    // Empty constructor required for Firebase
    public Teacher(
    ) {
        super();
    }
    public Teacher(String name,String password ,String age, String phone, String email, String shortBio, List<String> subjects) {
        super( name, password , age,  phone,  email,  shortBio);
        this.subjects = subjects;
    }


    public List<String> getSubjects() {
        return subjects;
    }

    public List<String> getLinks() { return links; }
    public List<Meeting> getMeetings() { return super.getMeetings(); }

    @Override
    public String toString() {
        return
                super.toString() +
                        "Subjects:" + subjects;
    }

    public void setLinks(List<String> linksToUpdate) {
        this.links = linksToUpdate;
    }
    public void addLinks(String linksToUpdate) {
        if(this.links == null)
            this.links = new ArrayList<>();
        this.links.add(linksToUpdate);
    }
    @Override
    public void addMeeting(Meeting MeetingToAdd) {
        if(super.getMeetings() == null)
            super.setMeetings(new ArrayList<>());
        super.getMeetings().add(MeetingToAdd);
        Collections.sort(super.getMeetings());
    }
    public void addMessage(Message MessageToAdd) {
        if(super.getMessages() == null)
            super.setMessages(new ArrayList<>());
        super.getMessages().add(MessageToAdd);
        Collections.sort(super.getMessages());
    }

    @Override
    public void  Notify(Meeting M) {
        for (Meeting meeting : super.getMeetings()) {
            if (meeting.equalTo(M)) {
                if (!meeting.ifAvailable()) {
                    Message newM = new Message("There is a new meeting:\n" + meeting);
                    addMessage(newM);
                    meeting.CancelMetting();

                }
                else {
                    meeting.addPartner(M.getPartner(), M.getPartnerUsername());
                    Message newM = new Message("There is a new meeting:\n" + meeting);
                    addMessage(newM);
                }
                IncNewMessage();
                return;
            }
        }
    }

    public void DeleteOldMeetings(DataSnapshot teacherSnapshot) {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        List<Meeting> allMeetings = new ArrayList<>();
        for (Meeting M : super.getMeetings()) {
            allMeetings.add(M);
        }
        for (Meeting meeting : allMeetings) {
            if (year > meeting.getYear())
                this.NotifyToDeleteMeeting(meeting);
            else if (year == meeting.getYear() &&
                    month > meeting.getMonth())
                this.NotifyToDeleteMeeting(meeting);
            else if (year == meeting.getYear() &&
                    month ==meeting.getMonth() &&
                    day > meeting.getDay())
                this.NotifyToDeleteMeeting(meeting);
        }
    }
@Override
    public void NotifyToDeleteMeeting(Meeting M) {
        for( Meeting meeting : super.getMeetings()){
            if(meeting.equalTo(M)){
                super.getMeetings().remove(meeting);
                if(meeting.ifAvailable())
                    return;
                // Initialize Firebase database reference
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference studentsRef = database.getReference("students");
                studentsRef.orderByKey().equalTo(meeting.getPartnerUsername())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                                        User student = studentSnapshot.getValue(User.class);
                                        if (student != null && student.getMeetings() != null) {
                                            student.NotifyToDeleteMeeting(meeting);
                                            studentsRef.child(meeting.getPartnerUsername()).setValue(student);



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


}

