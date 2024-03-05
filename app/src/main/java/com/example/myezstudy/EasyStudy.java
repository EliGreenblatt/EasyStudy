package com.example.myezstudy;

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

import java.util.ArrayList;
import java.util.List;


public class EasyStudy extends Application {

    public static int day, month, year;
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
                context.startActivity(new Intent(context, StudentProfile.class));
                break;
            case TEACHER:
                UserInformation.KEY_Type = "Teacher";
                // Navigate to the teacher's update profile page
                Log.i(TAG,"teacher");
                context.startActivity(new Intent(context, TeacherProfile.class));
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
        deleteAllOld();
    }
    public void deleteAllOld(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference teachersRef = database.getReference("teachers");
        teachersRef.orderByKey()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot teacherSnapshot : dataSnapshot.getChildren()) {
                                Teacher teacher = teacherSnapshot.getValue(Teacher.class);
                                if (teacher != null && teacher.getMeetings() != null) {
                                    teacher.DeleteOldMeetings(teacherSnapshot);
                                }
                                if (teacher != null ) {
                                    if(teacher.getMessages() != null){
                                    List<Message> allMessages = new ArrayList<>(teacher.getMessages());
                                        for( Message M : allMessages ){
                                            if(M.checkIfOld()){
                                            teacher.getMessages().remove(M);
                                            }
                                        }
                                    }
                                    if(teacher.getChatMessages() != null){
                                        List<ChatMessage> allChatMessages = new ArrayList<>(teacher.getChatMessages());
                                        for( ChatMessage CM : allChatMessages ){
                                            if(CM.checkIfOld()){
                                                    teacher.getChatMessages().remove(CM);
                                            }
                                        }
                                    }
                                    int messagesSize = 0;
                                    if(teacher.getMessages() != null)
                                        messagesSize += teacher.getMessages().size();
                                    if(teacher.getChatMessages() != null)
                                        messagesSize += teacher.getChatMessages().size();
                                    if(messagesSize < teacher.getNewMessage()) {
                                        teacher.setNewMessage(messagesSize);
                                    }
                                    teachersRef.child(teacherSnapshot.getKey()).setValue(teacher);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle error
                    }

                });
        DatabaseReference studentsRef = database.getReference("students");
        studentsRef.orderByKey()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot studentsSnapshot : dataSnapshot.getChildren()) {
                                User student = studentsSnapshot.getValue(User.class);
                                if (student != null ) {
                                    if(student.getMessages() != null){
                                        List<Message> allMessages = new ArrayList<>(student.getMessages());
                                        for( Message M : allMessages ){
                                            if(M.checkIfOld()){
                                                student.getMessages().remove(M);
                                            }
                                        }
                                    }
                                    if(student.getChatMessages() != null){
                                        List<ChatMessage> allChatMessages = new ArrayList<>(student.getChatMessages());
                                        for( ChatMessage CM : allChatMessages ){
                                            if(CM.checkIfOld()){
                                                student.getChatMessages().remove(CM);
                                            }
                                        }
                                    }
                                    int messagesSize = 0;
                                    if(student.getMessages() != null)
                                        messagesSize += student.getMessages().size();
                                    if(student.getChatMessages() != null)
                                        messagesSize += student.getChatMessages().size();
                                    if(messagesSize < student.getNewMessage()) {
                                        student.setNewMessage(messagesSize);
                                    }
                                    studentsRef.child(studentsSnapshot.getKey()).setValue(student);                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle error
                    }
                });
    }
    public static void addStudent(User student, String username, Context context) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("students");

        // Check if a user with the same name already exists
        databaseReference.orderByKey().equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!userExist(dataSnapshot, username)) {
                    // User does not exist with the same name and password, proceed to add
                    addStudentToDatabase(student, username, databaseReference, context);
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

        private static void addStudentToDatabase(User student,String username, DatabaseReference databaseReference, Context context) {

        if(username != null){
        // Add the student name to the database
        databaseReference.child(username).setValue(student)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firebase", "Student added successfully");
                    EasyStudy.showInfoMessageDialog(context, "You have registered successfully to EasyStudy! :)");
                })
                .addOnFailureListener(e -> Log.e("Firebase", "Failed to add student", e));
    }}

    // Add a teacher to the "teachers" table in the Realtime Database
    public static void addTeacher(Teacher teacher, String Username, Context context) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("teachers");

        // Check if a user with the same name already exists
        databaseReference.orderByKey().equalTo(Username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!userExist(dataSnapshot, Username)) {
                    // User does not exist with the same name and password, proceed to add
                    addTeacherToDatabase(teacher, Username, databaseReference, context);
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
    private static void addTeacherToDatabase(Teacher teacher, String Username, DatabaseReference databaseReference, Context context) {

        if (Username != null) {
            // Add the teacher object to the database
            databaseReference.child(Username).setValue(teacher)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("Firebase", "Teacher added successfully");
                        EasyStudy.showInfoMessageDialog(context, "You have registered successfully to EasyStudy! :)");
                    })
                    .addOnFailureListener(e -> Log.e("Firebase", "Failed to add teacher", e));
        }
    }

    public static void checkUserExists(String username, String password, Context context, UserTypeCallback callback) {
        DatabaseReference studentsReference = FirebaseDatabase.getInstance().getReference("students");
        DatabaseReference teachersReference = FirebaseDatabase.getInstance().getReference("teachers");
        // Check in the students table
        studentsReference.orderByKey().equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot studentSnapshot) {
                if (studentSnapshot.exists()) {
                    // User exists as a student
                    callback.onUserType(UserType.STUDENT, 1); // Return 1 for student
                }
                else{
                    // User not found in students, check in teachers
                    teachersReference.orderByKey().equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot teacherSnapshot) {
                            if (teacherSnapshot.exists()) {
                                // User exists as a teacher
                                callback.onUserType(UserType.TEACHER, 0); // Return 0 for teacher
                            }
                            else
                                callback.onUserType(UserType.UNKNOWN, -1); // Return -1 for UNKNOWN

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError teacherError) {
                            Log.e("Firebase", "Database error: " + teacherError.getMessage());
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError studentError) {
                Log.e("Firebase", "Database error: " + studentError.getMessage());
            }
        });


    }

    private static boolean userExist(DataSnapshot dataSnapshot, String username) {
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            if (snapshot.getKey() != null) {
                if (snapshot.getKey().equals(username)) {
                    User existingStudent = snapshot.getValue(User.class);
                    Teacher existingTeacher = snapshot.getValue(Teacher.class);{
                        return true; // User with the same name  already exists
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
