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

/**
 * The main application class for EasyStudy, responsible for initializing Firebase and handling user-related functionality.
 */
public class EasyStudy extends Application {
    // variables for date information
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

    /**
     * Navigate to different pages based on the user type.
     * @param context application context.
     * @param userType The user type to determine the page navigate to.
     */
    public static void navigateToPage(Context context, UserType userType) {
        switch (userType) {
            case STUDENT:
                UserInformation.KEY_Type = "User";
                // navigate to the student's profile page
                context.startActivity(new Intent(context, StudentProfile.class));
                break;
            case TEACHER:
                UserInformation.KEY_Type = "Teacher";
                // navigate to the teacher's update profile page
                Log.i(TAG,"teacher");
                context.startActivity(new Intent(context, TeacherProfile.class));
                break;
            case UNKNOWN:
                break;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        // Delete old messages and meetings
        deleteAllOld();
    }

    /**
     * Delete old messages and meetings from the database.
     * This method goes through both the "teachers" and "students" tables in the database,
     * removes old messages and chat messages, updates the new message count, and sets the modified data back to the database.
     */
    public void deleteAllOld() {
        // instance of Firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Handle teachers data
        DatabaseReference teachersRef = database.getReference("teachers");

        // Listen for a single data event on the teachers table
        teachersRef.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            // Callback triggered when we get the data
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check if there is any data
                if (dataSnapshot.exists()) {
                    // Loop through each teacher in the database
                    for (DataSnapshot teacherSnapshot : dataSnapshot.getChildren()) {
                        // Get the teacher object from the database
                        Teacher teacher = teacherSnapshot.getValue(Teacher.class);

                        // Delete old meetings
                        if (teacher != null && teacher.getMeetings() != null) {
                            teacher.DeleteOldMeetings(teacherSnapshot);
                        }

                        if (teacher != null) {
                            // Remove old messages
                            if (teacher.getMessages() != null) {
                                List<Message> allMessages = new ArrayList<>(teacher.getMessages());
                                // Loop through each message and remove the old ones
                                for (Message M : allMessages) {
                                    if (M.checkIfOld()) {
                                        teacher.getMessages().remove(M);
                                    }
                                }
                            }

                            // Remove old chat messages
                            if (teacher.getChatMessages() != null) {
                                List<ChatMessage> allChatMessages = new ArrayList<>(teacher.getChatMessages());
                                // Loop through each chat message and remove the old ones
                                for (ChatMessage CM : allChatMessages) {
                                    if (CM.checkIfOld()) {
                                        teacher.getChatMessages().remove(CM);
                                    }
                                }
                            }

                            // Calculate the total number of messages
                            int messagesSize = 0;
                            if (teacher.getMessages() != null)
                                messagesSize += teacher.getMessages().size();
                            if (teacher.getChatMessages() != null)
                                messagesSize += teacher.getChatMessages().size();

                            // Update the new message count if needed
                            if (messagesSize < teacher.getNewMessage()) {
                                teacher.setNewMessage(messagesSize);
                            }

                            // Update the modified data back to the "teachers" table in the database
                            teachersRef.child(teacherSnapshot.getKey()).setValue(teacher);
                        }
                    }
                }
            }

            @Override
            // Callback triggered if the data retrieval is canceled or encounters an error
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        // Handle students' data
        DatabaseReference studentsRef = database.getReference("students");

        // Listen for a single data event on the students table
        studentsRef.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            // Callback triggered when data is received
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check if data exists
                if (dataSnapshot.exists()) {
                    // Loop through each student in the database
                    for (DataSnapshot studentsSnapshot : dataSnapshot.getChildren()) {
                        // Get the student object from the database
                        User student = studentsSnapshot.getValue(User.class);

                        if (student != null) {
                            // Remove old messages
                            if (student.getMessages() != null) {
                                List<Message> allMessages = new ArrayList<>(student.getMessages());
                                // Loop through each message and remove the old ones
                                for (Message M : allMessages) {
                                    if (M.checkIfOld()) {
                                        student.getMessages().remove(M);
                                    }
                                }
                            }

                            // Remove old chat messages
                            if (student.getChatMessages() != null) {
                                List<ChatMessage> allChatMessages = new ArrayList<>(student.getChatMessages());
                                // Loop through each chat message and remove the old ones
                                for (ChatMessage CM : allChatMessages) {
                                    if (CM.checkIfOld()) {
                                        student.getChatMessages().remove(CM);
                                    }
                                }
                            }

                            // Calculate the total number of messages
                            int messagesSize = 0;
                            if (student.getMessages() != null)
                                messagesSize += student.getMessages().size();
                            if (student.getChatMessages() != null)
                                messagesSize += student.getChatMessages().size();

                            // Update the new message count if needed
                            if (messagesSize < student.getNewMessage()) {
                                student.setNewMessage(messagesSize);
                            }

                            // Update the modified data back to the "students" table in the database
                            studentsRef.child(studentsSnapshot.getKey()).setValue(student);
                        }
                    }
                }
            }

            @Override
            // Callback triggered if the data retrieval is canceled or encounters an error
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    /**
     * Add a student to the students table in the database.
     * @param student object to add.
     * @param username username of the student.
     * @param context The application context.
     */
    public static void addStudent(User student, String username, Context context) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("students");
        if(username != null){
            // Add the student name to the database
            databaseReference.child(username).setValue(student)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("Firebase", "Student added successfully");
                        EasyStudy.showInfoMessageDialog(context, "You have registered successfully to EasyStudy! :)");
                    })
                    .addOnFailureListener(e -> Log.e("Firebase", "Failed to add student", e));
        }
    }


    /**
     * Add a student to the students table in the database.
     * @param teacher object to add.
     * @param username username of the student.
     * @param context The application context.
     */
    public static void addTeacher(Teacher teacher, String username, Context context) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("teachers");
        if (username != null) {
            // Add the teacher object to the database
            databaseReference.child(username).setValue(teacher)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("Firebase", "Teacher added successfully");
                        EasyStudy.showInfoMessageDialog(context, "You have registered successfully to EasyStudy! :)");
                    })
                    .addOnFailureListener(e -> Log.e("Firebase", "Failed to add teacher", e));
        }    }

    /**
     * Check if a user with the provided username exists in the database.
     * @param username username to check.
     * @param context application context.
     * @param callback callback interface to return the user type and an additional code.
     */
    public static void checkUserExists(String username, Context context, UserTypeCallback callback) {
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

    /**
     * Display an error message dialog with a given message.
     * @param context application context.
     * @param message error message to display.
     */
    public static void showErrorMessageDialog(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    /**
     * Display an information message dialog with a given message.
     * @param context application context.
     * @param message information message to display.
     */
    public static void showInfoMessageDialog(Context context, String message) {
        showDialog(context, "Info", message);
    }

    /**
     * Display a generic message dialog with a specified title and message.
     * @param context application context.
     * @param title title of the dialog.
     * @param message message to display.
     */
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
