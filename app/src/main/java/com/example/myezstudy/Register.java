package com.example.myezstudy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Register extends AppCompatActivity {

    CheckBox TeacherCheck;
    Button Submit, backReg;
    TextInputLayout UsernameReg, PasswordReg, EmailReg, SubjectReg, ageReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Add CheckBox listener
        TeacherCheck =  findViewById(R.id.TeacherCheck);
        TeacherCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Toggle visibility of subjectSpinner based on whether teacher checkbox is checked
                if (isChecked) {
                    SubjectReg.setVisibility(View.VISIBLE);
                } else {
                    SubjectReg.setVisibility(View.INVISIBLE);
                }
            }
        });
        UsernameReg = findViewById(R.id.UsernameReg);
        PasswordReg = findViewById(R.id.PasswordReg);
        EmailReg = findViewById(R.id.EmailReg);
        SubjectReg = findViewById(R.id.SubjectReg);
        Submit = findViewById(R.id.Submit);
        ageReg = findViewById(R.id.ageReg);
        backReg = findViewById(R.id.backReg);
        backReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = UsernameReg.getEditText().getText().toString();
                String password = PasswordReg.getEditText().getText().toString();
                String email = EmailReg.getEditText().getText().toString();
                String age = ageReg.getEditText().getText().toString();
                // Check if a user with the same name and password already exists
                EasyStudy.checkUserExists(username, password, Register.this, new EasyStudy.UserTypeCallback() {
                    @Override
                    public void onUserType(EasyStudy.UserType userType, int i) {
                        // Determine the user type based on the integer i
                        if (i == 0 || i == 1) {
                            // User with the same name and password already exists (either student or teacher)
                            EasyStudy.showErrorMessageDialog(Register.this, "User with the same name and password already exists.");
                        }
                        else {
                            // User does not exist, proceed with registration
                            if (TeacherCheck.isChecked()) {
                                // If it's a teacher, gather additional information
                                String phone = ""; // Get teacher's phone from UI element
                                String shortBio = ""; // Get teacher's shortBio from UI element
                                List<String> subjects = new ArrayList<>(); // Get teacher's subjects
                                String selectedSubjectsString = Objects.requireNonNull(SubjectReg.getEditText()).getText().toString();
                                String[] selectedSubjectsArray = selectedSubjectsString.split(","); // Assuming subjects are separated by commas
                                for (String subject : selectedSubjectsArray) {
                                    subjects.add(subject.trim()); // Trim to remove leading/trailing spaces
                                }

                                // Create a Teacher object
                                Teacher newTeacher = new Teacher(username, password, age, phone, email, shortBio, subjects);

                                // Add the teacher to Firebase
                                EasyStudy.addTeacher(newTeacher, Register.this);

                                UserInformation.saveUserCredentials(Register.this, username, password);

                                // Navigate to the appropriate page based on user type
                                startActivity(new Intent(Register.this, TeacherProfile.class));
                            }
                            else {
                                // If it's a student, create a Student object
                                User newStudent = new User(username, password, age, "", email, "");

                                // Add the student to Firebase
                                EasyStudy.addStudent(newStudent, Register.this);
                                UserInformation.saveUserCredentials(Register.this, username, password);

                                // Navigate to the appropriate page based on user type
                                startActivity(new Intent(Register.this, StudentProfile.class));

                            }
                        }
                    }
                });
            }
        });
    }
}