package com.example.myapplication;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class TeacherProfileActivity extends AppCompatActivity
{
    Button updateProfileButton;
    Button logoutButton;
    Button uploadFiles;
    Button viewFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_profile);

        updateProfileButton = findViewById(R.id.updateProfile);
        logoutButton = findViewById(R.id.logout);
        uploadFiles = findViewById(R.id.uploadFiles);
        viewFiles = findViewById(R.id.viewFiles);
        updateProfileButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // This code will execute when the updateProfileButton is clicked
                Log.i("YourActivity", "updateProfileButton clicked");

                // Add your logic to navigate to the update profile page for students
                Intent intent = new Intent(TeacherProfileActivity.this, Update.class);
                startActivity(intent);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // This code will execute when the updateProfileButton is clicked
                Log.i("YourActivity", "updateProfileButton clicked");

                // Add your logic to navigate to the update profile page for students
                Intent intent = new Intent(TeacherProfileActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        viewFiles.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // This code will execute when the updateProfileButton is clicked
                Log.i("YourActivity", "View Files clicked");
            }
        });

        uploadFiles.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // This code will execute when the updateProfileButton is clicked
                Log.i("YourActivity", "Upload Files clicked");
            }
        });

    }
}
