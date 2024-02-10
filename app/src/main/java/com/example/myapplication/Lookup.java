package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

/**
 * The Lookup class allows users to search for teachers based on provided criteria.
 */
public class Lookup extends AppCompatActivity {

    private TextInputEditText nameText, ageText, subjectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lookup);

        // Initialize UI components
        Button backButton = findViewById(R.id.backButton);
        Button findButton = findViewById(R.id.findButton);
        nameText = findViewById(R.id.name);
        ageText = findViewById(R.id.age);
        subjectText = findViewById(R.id.subject);

        // Set click functionality for the back button using lambda
        backButton.setOnClickListener(v -> navigateBack());

        // Set click functionality for the find button using lambda
        findButton.setOnClickListener(v -> searchTeacher());
    }

    /**
     * Navigates back to the StudentProfileActivity.
     */
    private void navigateBack() {
        Intent intent = new Intent(Lookup.this, StudentProfileActivity.class);
        startActivity(intent);
    }

    /**
     * Searches for a teacher based on the provided criteria and starts FoundActivity.
     */
    private void searchTeacher() {
        // Get the input values
        String name = Objects.requireNonNull(nameText.getText()).toString().trim();
        String age = Objects.requireNonNull(ageText.getText()).toString().trim();
        String subject = Objects.requireNonNull(subjectText.getText()).toString().trim();

        // Create an Intent to start FoundActivity
        Intent intent = new Intent(Lookup.this, FoundActivity.class);

        // Put the data into the Intent
        intent.putExtra("AGE", age);
        intent.putExtra("NAME", name);
        intent.putExtra("SUBJECT", subject);

        // Start FoundActivity with the Intent
        startActivity(intent);
    }
}
