package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class Update extends AppCompatActivity {
    // Declaring buttons and text input editors
    private Button backButton;
    private Button updateButton;
    private TextInputEditText emailEditText;
    private TextInputEditText bioEditText;
    private TextInputEditText phoneEditText;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_profile);


        // Grab the reference to the firebase
        FireBaseData fireBaseData = new FireBaseData();

        // Grab the references to the back and update button
        backButton = findViewById(R.id.backButton);
        updateButton = findViewById(R.id.updateButton);

        // Get references to the TextInputEditTexts
        emailEditText = findViewById(R.id.email);
        bioEditText = findViewById(R.id.bio);
        phoneEditText = findViewById(R.id.phone);

        // Set click functionality for back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //navigateBack();
            }
        });

        // Set update button functionality
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
    }

    private void navigateBack() {
        getDetails();
        FireBaseData fireBaseData = new FireBaseData();
        fireBaseData.searchStudent(username, password, new FireBaseData.UserSearchListener() {
            @Override
            public void onStudentFound() {
                startActivity(new Intent(Update.this, StudentProfileActivity.class));
            }

            @Override
            public void onTeacherFound() {
                startActivity(new Intent(Update.this, TeacherProfileActivity.class));
            }

            @Override
            public void onUserNotFound() {
                // Handle user not found
            }

            @Override
            public void onError(String errorMessage) {
                // Handle error
                Log.e("Update", "Error: " + errorMessage);
            }
        });
    }

    private void updateProfile() {
        getDetails();
        Log.i("Update", "Updating profile");
        Toast.makeText(Update.this, "Updating profile...", Toast.LENGTH_SHORT).show();

        String updatedEmail = emailEditText.getText().toString();
        String updatedBio = bioEditText.getText().toString();
        String updatedPhone = phoneEditText.getText().toString();

        FireBaseData fireBaseData = new FireBaseData();

        fireBaseData.searchStudent(username, password, new FireBaseData.UserSearchListener() {
            @Override
            public void onStudentFound() {
                updateStudentProfile(updatedEmail, updatedBio, updatedPhone);
            }

            @Override
            public void onTeacherFound() {
                // If user is found as a teacher, update teacher profile
                updateTeacherProfile(updatedEmail, updatedBio, updatedPhone);
            }

            @Override
            public void onUserNotFound() {
                // If user is not found as a student, try searching as a teacher
                fireBaseData.searchTeacher(username, password, new FireBaseData.UserSearchListener() {
                    @Override
                    public void onStudentFound() {
                        // This should not happen in this scenario
                    }

                    @Override
                    public void onTeacherFound() {
                        // If user is found as a teacher, update teacher profile
                        updateTeacherProfile(updatedEmail, updatedBio, updatedPhone);
                    }

                    @Override
                    public void onUserNotFound() {
                        // Handle user not found
                        Log.i("Update", "User not found");
                    }

                    @Override
                    public void onError(String errorMessage) {
                        // Handle error
                        Log.e("Update", "Error: " + errorMessage);
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                // Handle error
                Log.e("Update", "Error: " + errorMessage);
            }
        });
    }


    private void updateStudentProfile(String email, String bio, String phone) {
        FireBaseData fireBaseData = new FireBaseData();
        fireBaseData.updateStudent(username, password, email, bio, phone, new FireBaseData.UserSearchListener() {
            @Override
            public void onStudentFound() {
                Log.i("Update", "Student profile updated successfully");
                Toast.makeText(Update.this, "Student profile updated successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTeacherFound() {
                // This method will not be called in this scenario
            }

            @Override
            public void onUserNotFound() {
                // Theoretically, we shouldn't reach here
                Log.i("Update", "Student not found");
            }

            @Override
            public void onError(String errorMessage) {
                // Handle error
                Log.e("Update", "Error: " + errorMessage);
            }
        });
    }

    private void updateTeacherProfile(String email, String bio, String phone) {
        FireBaseData fireBaseData = new FireBaseData();
        fireBaseData.updateTeacher(username, password, email, bio, phone, new FireBaseData.UserSearchListener() {
            @Override
            public void onStudentFound() {
                // This method will not be called in this scenario
                Log.i("Update", "updateTeacherProfile: onStudentFound");
            }

            @Override
            public void onTeacherFound() {
                Log.i("Update", "Teacher profile updated successfully");
                Toast.makeText(Update.this, "Teacher profile updated successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUserNotFound() {
                // Theoretically, we shouldn't reach here
                Log.i("Update", "updateTeacherProfile: onUserNotFound");
            }

            @Override
            public void onError(String errorMessage) {
                // Handle error
                Log.e("Update", "Error: " + errorMessage);
            }
        });
    }

    private void getDetails() {
        // Load saved username and password
        username = UserInformation.getSavedUsername(this);
        password = UserInformation.getSavedPassword(this);
    }
}
