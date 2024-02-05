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
import android.content.DialogInterface;


public class EasyStudy extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
    }

    // Add a student name to the "students" table in the Realtime Database
    public static void addStudent(Student student) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("students");

        // Generate a unique key for the new student
        String studentId = databaseReference.push().getKey();

        // Add the student name to the database
        databaseReference.child(studentId).setValue(student)
                .addOnSuccessListener(aVoid -> Log.d("Firebase", "Student added successfully"))
                .addOnFailureListener(e -> Log.e("Firebase", "Failed to add student", e));
    }

    // Add a teacher to the "teachers" table in the Realtime Database
    public static void addTeacher(Teacher teacher) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("teachers");

        // Generate a unique key for the new teacher
        String teacherId = databaseReference.push().getKey();

        // Add the teacher object to the database
        databaseReference.child(teacherId).setValue(teacher)
                .addOnSuccessListener(aVoid -> Log.d("Firebase", "Teacher added successfully"))
                .addOnFailureListener(e -> Log.e("Firebase", "Failed to add teacher", e));
    }

    // Check if the user exists in the database based on username and password
    public static void checkUserExists(String username, String password, Context context) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("students");

        // Query the database to check if the username and password exist
        databaseReference.orderByChild("name").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User exists, now check the password
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Student student = snapshot.getValue(Student.class);
                        if (student != null && student.getPassword().equals(password)) {
                            // Password matches, perform logic here
                            Log.d("Firebase", "User exists with matching password");
                            EasyStudy.showInfoMessageDialog(context, "Proceeding to the feed");
                            return;
                        }
                    }
                    // Password doesn't match
                    showErrorMessageDialog(context, "Incorrect password. Please try again.");
                } else {
                    // User doesn't exist
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


    public static void showInfoMessageDialog(Context context, String message) {
        showDialog(context, "Info", message);
    }

    private static void showDialog(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
