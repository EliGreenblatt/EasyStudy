package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class TeacherProfileActivity extends AppCompatActivity {
    Button updateProfileButton;
    Button logoutButton;
    Button uploadFiles;
    Button viewFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_profile);

        updateProfileButton = findViewById(R.id.updateProfile);
        logoutButton = findViewById(R.id.logout);
        uploadFiles = findViewById(R.id.uploadFiles);
        viewFiles = findViewById(R.id.viewFiles);

        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherProfileActivity.this, Update.class);
                startActivity(intent);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherProfileActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        viewFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("YourActivity", "View Files clicked");
            }
        });

        uploadFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to UploadLinksActivity and pass the teacher name as an extra
                Log.i("TAG", "I am in teacher profile");
                String teacherName = UserInformation.getSavedUsername(TeacherProfileActivity.this);
                Intent intent = new Intent(TeacherProfileActivity.this, UploadLinksActivity.class);
                intent.putExtra("TeacherName", teacherName);
                startActivity(intent);
            }
        });
    }
}
