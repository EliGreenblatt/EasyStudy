package com.example.myapplication;

import android.app.Application;
import android.util.Log;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import android.content.Context;
import android.app.AlertDialog;



public class EasyStudy extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
    }

    // Add a student name to the "students" table in the Realtime Database
    public static void addStudent(String studentName) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("students");

        // Generate a unique key for the new student
        String studentId = databaseReference.push().getKey();

        // Add the student name to the database
        databaseReference.child(studentId).child("name").setValue(studentName)
                .addOnSuccessListener(aVoid -> Log.d("Firebase", "Student added successfully"))
                .addOnFailureListener(e -> Log.e("Firebase", "Failed to add student", e));
    }

    // Check if the user exists in the database
    public static void checkUserExists(String username, Context context) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("students");

        // Query the database to check if the username exists
        databaseReference.orderByChild("name").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User exists, perform logic here
                    Log.d("Firebase", "User exists");
                } else {
                    // User doesn't exist, perform logic here
                    Log.d("Firebase", "User does not exist");

                    showErrorMessageDialog(context, "User does not exist. Please check your credentials.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Database error: " + databaseError.getMessage());
            }
        });
    }
    private static void showErrorMessageDialog(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
