package com.example.myapplication;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class Update extends AppCompatActivity
{
    // Declaring buttons and text input editors
    Button backButton;
    Button updateButton;
    TextInputEditText emailEditText;
    TextInputEditText bioEditText;
    TextInputEditText phoneEditText;
    String username;
    String password;
    public void getDetails(Context context) {
        // Load saved username and password
        password = UserInformation.getSavedUsername(context);
        username = UserInformation.getSavedPassword(context);

        // Now you can use the savedUsername and savedPassword as needed
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // This updates the layout to the "update_profile" layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_profile);

        // Grab the reference to the fire base
        FireBaseData fireBaseData = new FireBaseData();


        // Grab the references to the back and update button
        backButton = findViewById(R.id.backButton);
        updateButton = findViewById(R.id.updateButton);

        // Get references to the TextInputEditTexts
        emailEditText = findViewById(R.id.email);
        bioEditText = findViewById(R.id.bio);
        phoneEditText = findViewById(R.id.phone);


        // Set click functionality for back button, this button needs to take us back to the
        // Student or Teacher profile activity page
        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Enter here function that will take us back to teacher
                // or student accordingly
                getDetails(Update.this);
                fireBaseData.searchStudent(username, password, new FireBaseData.UserSearchListener() {
                    @Override
                    public void onStudentFound()
                    {

                    }

                    @Override
                    public void onTeacherFound()
                    {
                        Intent intent = new Intent(Update.this, StudentProfileActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onUserNotFound() {

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });

                fireBaseData.searchTeacher(username, password, new FireBaseData.UserSearchListener()
                {
                    @Override
                    public void onStudentFound()
                    {

                    }

                    @Override
                    public void onTeacherFound()
                    {
                        Intent intent = new Intent(Update.this, TeacherProfileActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onUserNotFound() {

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
            }
        });

        // Set update button functionality, needs to take the input from the text fields
        // and update the database accordingly
        updateButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getDetails(Update.this);
                Log.i("Update", username + " in update");
                Log.i("Update", password + " in update");

                // Perform a search for a student
                Log.i("Update", "Searching for user");

                String updatedEmail = emailEditText.getText().toString();
                String updatedBio = bioEditText.getText().toString();
                String updatedPhone = phoneEditText.getText().toString();
                fireBaseData.updateStudent(username, password,updatedEmail,
                        updatedBio, updatedPhone, new FireBaseData.UserSearchListener() {
                    @Override
                    public void onStudentFound()
                    {
                        // Student found, do something with the student object
                        Log.i("Update"," Student found ");
                    }

                    @Override
                    public void onTeacherFound()
                    {
                        // This method will not be called in this scenario
                    }

                    @Override
                    public void onUserNotFound()
                    {
                        // Theoretically we shouldn't reach here
                        Log.i("Update","Teacher not found");
                    }

                    @Override
                    public void onError(String errorMessage)
                    {
                        // Handle error
                    }
                });
            }


        });



    }
}
