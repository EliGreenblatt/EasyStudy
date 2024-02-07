package com.example.myapplication;

import android.util.Log;

import androidx.annotation.NonNull; // Import for @NonNull annotation
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class FireBaseData
{
    // Firebase references for students and teachers
    private DatabaseReference studentsRef;
    private DatabaseReference teachersRef;

    // Constructor
    public FireBaseData()
    {
        // Initialize database references
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        studentsRef = database.getReference("students");
        teachersRef = database.getReference("teachers");
    }


    // Method to update student
    public void updateStudent(String username, String password,String email,
                              String bio, String phone,final UserSearchListener listener) {
        Log.i("FireBaseData", "starting search");
        Log.i("FireBaseData", "User: " + username);
        Log.i("FireBaseData","Password: " + password);
        // Search in students table
        studentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                    String storedUsername = studentSnapshot.child("name").getValue(String.class);
                    String storedPassword = studentSnapshot.child("password").getValue(String.class);
                    Log.i("FireBaseData","Found username: " + storedUsername);
                    Log.i("FireBaseData","Found password: " + storedPassword);
                    if (storedUsername != null && storedPassword != null && storedUsername.equals(username) && storedPassword.equals(password))
                    {
                        Log.i("FireBaseData", "Found student");
                        studentSnapshot.getRef().child("email").setValue(email);
                        studentSnapshot.getRef().child("shortBio").setValue(bio);
                        studentSnapshot.getRef().child("phone").setValue(phone);
                        return;
                    }
                }
                // If not found in students table
                Log.i("FireBaseData", "Not Found in students table");
                listener.onUserNotFound();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("FireBaseData", "On Cancel function");

                listener.onError(databaseError.getMessage());
            }
        });


    }

    // Search for student
    public void searchStudent(String username, String password,final UserSearchListener listener) {
        Log.i("FireBaseData", "starting search");
        Log.i("FireBaseData", "User: " + username);
        Log.i("FireBaseData","Password: " + password);
        // Search in students table
        studentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                    String storedUsername = studentSnapshot.child("name").getValue(String.class);
                    String storedPassword = studentSnapshot.child("password").getValue(String.class);
                    Log.i("FireBaseData","Found username: " + storedUsername);
                    Log.i("FireBaseData","Found password: " + storedPassword);
                    if (storedUsername != null && storedPassword != null && storedUsername.equals(username) && storedPassword.equals(password))
                    {
                        Log.i("FireBaseData", "Found student");
                        listener.onStudentFound();
                    }
                }
                // If not found in students table
                Log.i("FireBaseData", "Not Found in students table");
                listener.onUserNotFound();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("FireBaseData", "On Cancel function");

                listener.onError(databaseError.getMessage());
            }
        });


    }

    // Search for teacher
    public void searchTeacher(String username, String password,final UserSearchListener listener) {
        Log.i("FireBaseData", "starting search");
        Log.i("FireBaseData", "User: " + username);
        Log.i("FireBaseData","Password: " + password);
        // Search in students table
        teachersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                    String storedUsername = studentSnapshot.child("name").getValue(String.class);
                    String storedPassword = studentSnapshot.child("password").getValue(String.class);
                    Log.i("FireBaseData","Found username: " + storedUsername);
                    Log.i("FireBaseData","Found password: " + storedPassword);
                    if (storedUsername != null && storedPassword != null && storedUsername.equals(username) && storedPassword.equals(password))
                    {
                        Log.i("FireBaseData", "Found student");
                        listener.onTeacherFound();
                    }
                }
                // If not found in students table
                Log.i("FireBaseData", "Not Found in students table");
                listener.onUserNotFound();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("FireBaseData", "On Cancel function");

                listener.onError(databaseError.getMessage());
            }
        });

    }


    public interface UserSearchListener {
        void onStudentFound();
        void onTeacherFound();
        void onUserNotFound();
        void onError(String errorMessage);
    }
}


