package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.CheckBox;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.ArrayList;



public class MainActivity extends AppCompatActivity
{
    private static final String TAG = "MainActivity";
    CheckBox teacher;
    Button loginButton;
    Button registerButton;
    Button login;
    Button register;
    Button backButton;
    TextInputLayout usernameInputLayout;
    TextInputLayout userPassInputLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        teacher = findViewById(R.id.checkBox); // Replace with your CheckBox ID
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        loginButton = findViewById(R.id.loginButton); // Grab the login button reference
        registerButton = findViewById(R.id.registerButton); // Grab the register button reference
        usernameInputLayout = findViewById(R.id.UsernameText); // Grab the input username text
        userPassInputLayout = findViewById(R.id.PasswordText); // Grab the input user password text

        teacher.setVisibility(View.GONE);
        register.setVisibility(View.GONE);
        hideTextFields();

        backButton = findViewById(R.id.backButton);
        backButton.setVisibility(View.GONE);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle back button click
                Log.i(TAG, "Back button clicked");
                showButtons();
                hideBack();
                hideTextFields();
                backButton.setVisibility(View.GONE);
                usernameInputLayout.getEditText().setText(""); // Clear entered username
                userPassInputLayout.getEditText().setText(""); // Clear entered password
            }

        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Login button click
                Log.i(TAG,"Login button clicked");
                hideButtons();
                showTextFields();
                backButton.setVisibility(View.VISIBLE); // Show the back button
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // Handle Login button click
                Log.i(TAG,"Second Login Pressed");

                // Get the entered username and password
                String username = usernameInputLayout.getEditText().getText().toString();
                String password = userPassInputLayout.getEditText().getText().toString();

                // Check if it's a teacher or student
                EasyStudy.checkUserExists(username, password, MainActivity.this, new EasyStudy.UserTypeCallback() {
                    @Override
                    public void onUserType(EasyStudy.UserType userType, int i) {
                        // Determine the user type based on the integer i
                        if (i == 0) {
                            // User type is teacher
                            userType = EasyStudy.UserType.TEACHER;
                        } else if (i == 1) {
                            // User type is student
                            userType = EasyStudy.UserType.STUDENT;
                        } else {
                            // Unknown user type
                            userType = EasyStudy.UserType.UNKNOWN;
                        }

                        // Navigate to the appropriate page based on user type
                        EasyStudy.navigateToPage(MainActivity.this, userType);
                    }

                });
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Login button click
                Log.i(TAG,"Register button clicked");
                hideButtons();
                showTextFields();
                loginButton.setVisibility(View.GONE);
                login.setVisibility(View.GONE);
                teacher.setVisibility(View.VISIBLE);
                register.setVisibility(View.VISIBLE);
                backButton.setVisibility(View.VISIBLE); // Show the back button
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Login button click
                Log.i(TAG,"Register button clicked second time");

                // Get the entered username and password
                String username = usernameInputLayout.getEditText().getText().toString();
                String password = userPassInputLayout.getEditText().getText().toString();

                // Check if it's a teacher or student
                if (teacher.isChecked()) {
                    // If it's a teacher, gather additional information
                    String phone = ""; // Get teacher's phone from UI element
                    String email = ""; // Get teacher's email from UI element
                    String shortBio = ""; // Get teacher's shortBio from UI element
                    List<String> subjects = new ArrayList<>(); // Get teacher's subjects from UI element

                    // Create a Teacher object
                    Teacher newTeacher = new Teacher(null, password, username, phone, email, shortBio, subjects);

                    // Add the teacher to Firebase
                    EasyStudy.addTeacher(newTeacher, MainActivity.this);
                } else {
                    // If it's a student, create a Student object
                    Student newStudent = new Student(username, password, 0, "", "", "");

                    // Add the student to Firebase
                    EasyStudy.addStudent(newStudent, MainActivity.this);
                }
            }
        });
        
    }

    private void hideTextFields()
    {
        usernameInputLayout.setVisibility(View.GONE); // Hide the password field
        userPassInputLayout.setVisibility((View.GONE));// Hide the password field
        login.setVisibility(View.GONE);

    }

    private void showTextFields()
    {
        usernameInputLayout.setVisibility(View.VISIBLE); // Hide the password field
        userPassInputLayout.setVisibility(View.VISIBLE);//  Hide the password field
        login.setVisibility(View.VISIBLE);
    }

    private void hideButtons()
    {
        loginButton.setVisibility(View.GONE);
        registerButton.setVisibility(View.GONE);

    }

    private void hideBack()
    {
        register.setVisibility(View.GONE);
        teacher.setVisibility(View.GONE);
    }

    private void showButtons() {
        loginButton.setVisibility(View.VISIBLE);
        registerButton.setVisibility(View.VISIBLE);
    }
}