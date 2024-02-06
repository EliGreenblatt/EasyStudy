package com.example.myapplication;
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
    TextInputEditText nameEditText;
    TextInputEditText bioEditText;
    TextInputEditText phoneEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // This updates the layout to the "update_profile" layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_profile);

        // Grab the references to the back and update button
        backButton = findViewById(R.id.backButton);
        updateButton = findViewById(R.id.updateButton);

        // Get references to the TextInputEditTexts
        emailEditText = findViewById(R.id.email);
        nameEditText = findViewById(R.id.name);
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
            }
        });

        // Set update button functionality, needs to take the input from the text fields
        // and update the database accordingly
        updateButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Take the input from the input fields and update database
                // afterwards print updated successfully and use call the return function
                // you built in the previous button
            }
        });

    }



}
