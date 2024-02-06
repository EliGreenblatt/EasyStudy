// StudentProfileActivity.java
package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

public class StudentProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        Button updateProfileButton = findViewById(R.id.updateProfileButton);
        Button logoutButton = findViewById(R.id.logoutButton);

        Button searchButton = findViewById(R.id.searchButton);
        // On search button click we go to a page where we can there filter accordingly
        // and find the teachers
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentProfileActivity.this, Lookup.class);
                startActivity(intent);
            }
        });

        // Handle "Update Profile" button click
        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add your logic to navigate to the update profile page for students
                Log.i("Main Activity", "Testing");
                Intent intent = new Intent(StudentProfileActivity.this, Update.class);
                startActivity(intent);            }
        });

        // Handle "Logout" button click
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add your logic to perform logout (e.g., return to the login screen)
                //Example
                startActivity(new Intent(StudentProfileActivity.this, MainActivity.class));
                //finish(); // Close the current activity
            }
        });
    }
}
