package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class Lookup extends AppCompatActivity
{
    // Declaring buttons and text input editors
    Button backButton;
    Button findButton;
    TextInputEditText nameText;
    TextInputEditText ageText;
    TextInputEditText subjectText;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lookup);

        backButton = findViewById(R.id.backButton);
        findButton = findViewById(R.id.findButton);
        nameText = findViewById(R.id.name);
        ageText = findViewById(R.id.age);
        subjectText = findViewById(R.id.subject);


        // back button takes us back to the previous page
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Lookup.this, StudentProfileActivity.class);
                startActivity(intent);
            }
        });

        // we take the input from the text fields and search the teacher
        // table for a match
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the input values
                String name = nameText.getText().toString().trim();
                String age = ageText.getText().toString().trim();
                String subject = subjectText.getText().toString().trim();

                // Create an Intent to start FoundActivity
                Intent intent = new Intent(Lookup.this, FoundActivity.class);

                // Put the data into the Intent
                intent.putExtra("AGE", age);
                intent.putExtra("NAME", name);
                intent.putExtra("SUBJECT", subject);

                // Start FoundActivity with the Intent
                startActivity(intent);
            }
        });

    }
}
