package com.example.myapplication;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EasyStudy extends Application
{

    @Override
    public void onCreate()
    {
        super.onCreate();

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
    }

    // Add a student name to the "students" table in the Realtime Database
    public static void addStudent(String studentName)
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("students");

        // Generate a unique key for the new student
        String studentId = databaseReference.push().getKey();

        // Add the student name to the database
        databaseReference.child(studentId).child("name").setValue(studentName)
                .addOnSuccessListener(aVoid -> Log.d("Firebase", "Student added successfully"))
                .addOnFailureListener(e -> Log.e("Firebase", "Failed to add student", e));
    }
}
