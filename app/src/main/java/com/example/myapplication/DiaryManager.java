package com.example.myapplication;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Date;

public class DiaryManager {

    private final DatabaseReference diaryRef;

    public DiaryManager(String teacherId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        diaryRef = database.getReference("teachers").child(teacherId).child("diary");
    }

    public void addDiaryEntry(Date date, String student, String subject, String time, String content) {
        String entryId = diaryRef.push().getKey();
        if (entryId != null) {
            DiaryEntry entry = new DiaryEntry(date, student, subject, time,  content);
            diaryRef.child(entryId).setValue(entry);
        }
    }

    public void updateDiaryEntryApprovalStatus(String entryId, boolean approved) {
        diaryRef.child(entryId).child("approved").setValue(approved);
    }

    // Add methods to retrieve, update, and delete diary entries as needed
}
