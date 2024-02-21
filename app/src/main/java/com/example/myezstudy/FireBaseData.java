package com.example.myezstudy;

import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * interact with Firebase Realtime Database for user data operations.
 */
public class FireBaseData {
    // Firebase references for students and teachers
    private final DatabaseReference studentsRef;
    private final DatabaseReference teachersRef;

    // Constructor
    public FireBaseData() {
        // Initialize database references
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        studentsRef = database.getReference("students");
        teachersRef = database.getReference("teachers");
    }

    // Method to update student
    public void updateStudent(String username, String password, String email,
                              String bio, String phone, final UserSearchListener listener) {
        // Search in students table
        studentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                    String storedUsername = studentSnapshot.child("name").getValue(String.class);
                    String storedPassword = studentSnapshot.child("password").getValue(String.class);
                    if (storedUsername != null && storedPassword != null && storedUsername.equals(username) && storedPassword.equals(password)) {
                        if(!email.isEmpty())
                            studentSnapshot.getRef().child("email").setValue(email);
                        if(!bio.isEmpty())
                        studentSnapshot.getRef().child("shortBio").setValue(bio);
                        if(!phone.isEmpty())
                        studentSnapshot.getRef().child("phone").setValue(phone);
                        return;
                    }
                }
                // If not found in students table
                listener.onUserNotFound();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        });
    }

    // Method to update teacher
    public void updateTeacher(String username, String password, String email, String bio, String phone, final UserSearchListener listener) {
        // Search in teachers table
        teachersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot teacherSnapshot : dataSnapshot.getChildren()) {
                    String storedUsername = teacherSnapshot.child("name").getValue(String.class);
                    String storedPassword = teacherSnapshot.child("password").getValue(String.class);
                    if (storedUsername != null && storedPassword != null && storedUsername.equals(username) && storedPassword.equals(password)) {
                        if(!email.isEmpty())
                            teacherSnapshot.getRef().child("email").setValue(email);
                        if(!bio.isEmpty())
                            teacherSnapshot.getRef().child("shortBio").setValue(bio);
                        if(!phone.isEmpty())
                            teacherSnapshot.getRef().child("phone").setValue(phone);
                        listener.onTeacherFound();
                        return;
                    }
                }
                // If not found in teachers table
                listener.onUserNotFound();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        });
    }

    // Search for student
    public void searchStudent(String username, String password, final UserSearchListener listener) {
        // Search in students table
        studentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                    String storedUsername = studentSnapshot.child("name").getValue(String.class);
                    String storedPassword = studentSnapshot.child("password").getValue(String.class);
                    if (storedUsername != null && storedPassword != null && storedUsername.equals(username) && storedPassword.equals(password)) {
                        listener.onStudentFound();
                        return;
                    }
                }
                // If not found in students table
                listener.onUserNotFound();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        });
    }

    // Search for teacher
    public void searchTeacher(String username, String password, final UserSearchListener listener) {
        // Search in teachers table
        teachersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot teacherSnapshot : dataSnapshot.getChildren()) {
                    String storedUsername = teacherSnapshot.child("name").getValue(String.class);
                    String storedPassword = teacherSnapshot.child("password").getValue(String.class);
                    if (storedUsername != null && storedPassword != null && storedUsername.equals(username) && storedPassword.equals(password)) {
                        listener.onTeacherFound();
                        return;
                    }
                }
                // If not found in teachers table
                listener.onUserNotFound();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        });
    }

    /**
     * Listener interface for user search operations.
     */
    public interface UserSearchListener {
        void onStudentFound();
        void onTeacherFound();
        void onUserNotFound();
        void onError(String errorMessage);
    }
}


