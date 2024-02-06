package com.example.myapplication;

import static android.content.ContentValues.TAG;

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

import android.content.Intent;

import androidx.annotation.NonNull;


public class EasyStudy extends Application {
    // Enum to represent user types
    public enum UserType {
        STUDENT,
        TEACHER,
        UNKNOWN
    }
    // Callback interface for user type
    public interface UserTypeCallback {
        void onUserType(UserType userType, int i);
    }

    // Method to navigate to different pages based on user type
    public static void navigateToPage(Context context, UserType userType) {
        switch (userType) {
            case STUDENT:
                // Navigate to the student's profile page
                context.startActivity(new Intent(context, StudentProfileActivity.class));
                break;
            case TEACHER:
                // Navigate to the teacher's update profile page
                Log.i(TAG,"teacherrrrrrrrrrrrrrrrrr");

                context.startActivity(new Intent(context, TeacherProfileActivity.class));

                break;
            case UNKNOWN:
                // Navigate to the logout page or show an error message
                // Example: context.startActivity(new Intent(context, LogoutActivity.class));
                break;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
    }

    public static void addStudent(Student student, Context context) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("students");

        // Check if a user with the same name already exists
        databaseReference.orderByChild("name").equalTo(student.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!userExistsWithSamePassword(dataSnapshot, student.getName(), student.getPassword())) {
                    // User does not exist with the same name and password, proceed to add
                    addStudentToDatabase(student, databaseReference, context);
                } else {
                    // User with the same name and password already exists
                    showErrorMessageDialog(context, "User with the same name and password already exists.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Database error: " + databaseError.getMessage());
            }
        });
    }


    // TODO: ADD THE SAME THING TO THE TEACHER ( ADD TEACHER AND ADD TEACHER TO DATABASE )

        private static void addStudentToDatabase(Student student, DatabaseReference databaseReference, Context context) {
        // Generate a unique key for the new student
        String studentId = databaseReference.push().getKey();
        if(studentId != null){
        // Add the student name to the database
        databaseReference.child(studentId).setValue(student)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firebase", "Student added successfully");
                    EasyStudy.showInfoMessageDialog(context, "You have registered successfully to EasyStudy! :)");
                })
                .addOnFailureListener(e -> Log.e("Firebase", "Failed to add student", e));
    }}

    // Add a teacher to the "teachers" table in the Realtime Database
    public static void addTeacher(Teacher teacher, Context context) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("teachers");

        // Check if a user with the same name already exists
        databaseReference.orderByChild("name").equalTo(teacher.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!userExistsWithSamePassword(dataSnapshot, teacher.getName(), teacher.getPassword())) {
                    // User does not exist with the same name and password, proceed to add
                    addTeacherToDatabase(teacher, databaseReference, context);
                } else {
                    // User with the same name and password already exists
                    showErrorMessageDialog(context, "User with the same name and password already exists.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Database error: " + databaseError.getMessage());
            }
        });
    }


    // Method to add a teacher to the database
    private static void addTeacherToDatabase(Teacher teacher, DatabaseReference databaseReference, Context context) {
        // Generate a unique key for the new teacher
        String teacherId = databaseReference.push().getKey();
        if (teacherId != null) {
            // Add the teacher object to the database
            databaseReference.child(teacherId).setValue(teacher)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("Firebase", "Teacher added successfully");
                        EasyStudy.showInfoMessageDialog(context, "You have registered successfully to the screen");
                    })
                    .addOnFailureListener(e -> Log.e("Firebase", "Failed to add teacher", e));
        }
    }

    public static void checkUserExists(String username, String password, Context context, UserTypeCallback callback) {
        DatabaseReference studentsReference = FirebaseDatabase.getInstance().getReference("students");
        DatabaseReference teachersReference = FirebaseDatabase.getInstance().getReference("teachers");

        // Check in the students table
        studentsReference.orderByChild("name").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot studentSnapshot) {
                if (studentSnapshot.exists()) {
                    // User exists as a student
                    callback.onUserType(UserType.STUDENT, 1); // Return 1 for student
                } else {
                    // User not found in students, check in teachers
                    teachersReference.orderByChild("name").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot teacherSnapshot) {
                            if (teacherSnapshot.exists()) {
                                // User exists as a teacher
                                callback.onUserType(UserType.TEACHER, 0); // Return 0 for teacher
                            } else {
                                // User not found in teachers as well
                                // Proceed with registration
                                //showErrorMessageDialog(context, "User is not registered in the system.");
                                callback.onUserType(UserType.UNKNOWN, -1); // Return -1 for unknown
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError teacherError) {
                            Log.e("Firebase", "Database error: " + teacherError.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError studentError) {
                Log.e("Firebase", "Database error: " + studentError.getMessage());
            }
        });
    }

    private static boolean userExistsWithSamePassword(DataSnapshot dataSnapshot, String username, String password) {
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            if (snapshot.getKey() != null) {
                if (snapshot.getKey().equals(username)) {
                    Student existingStudent = snapshot.getValue(Student.class);
                    Teacher existingTeacher = snapshot.getValue(Teacher.class);

                    if ((existingStudent != null && existingStudent.getPassword().equals(password) && existingTeacher == null) ||
                            (existingTeacher != null && existingTeacher.getPassword().equals(password) && existingStudent == null)) {
                        return true; // User with the same name and password already exists
                    }
                }
            }
        }
        return false;
    }



    public static void showErrorMessageDialog(Context context, String message) {
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
