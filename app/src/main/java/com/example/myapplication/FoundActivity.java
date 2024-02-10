package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
public class FoundActivity extends AppCompatActivity {

    private ListView teacherListView;
    private List<Teacher> foundTeachers;
    private ArrayAdapter<Teacher> adapter;
    private DatabaseReference teachersRef;
    private LinearLayout buttonsLayout;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found);

        backButton = findViewById(R.id.backButton);

        // Initialize Firebase database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        teachersRef = database.getReference("teachers");

        // Retrieve search parameters from the Intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("NAME").trim();
        String age = intent.getStringExtra("AGE").trim();
        String subject = intent.getStringExtra("SUBJECT").trim();


        // Initialize ListView and List
        teacherListView = findViewById(R.id.teacherListView);
        foundTeachers = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, foundTeachers);
        teacherListView.setAdapter(adapter);

        // Initialize buttonsLayout
        buttonsLayout = findViewById(R.id.buttonsLayout);

        Log.i("TAG", name);
        Log.i("TAG", age);
        Log.i("TAG", subject);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();  // Close the activity when the back button is clicked
            }
        });

        // Perform the search in Firebase database
        // Perform the search in Firebase database
        Query query;
        if (!name.isEmpty() && !age.isEmpty() && !subject.isEmpty()) {
            // Search by all parameters (name, age, and subject)
            query = teachersRef.orderByChild("name").equalTo(name);
        } else if (name.isEmpty() && age.isEmpty() && !subject.isEmpty()) {
            // Search only by subject
            query = teachersRef.orderByChild("subjects/" + subject);
        }
        else if (!name.isEmpty() && age.isEmpty() && subject.isEmpty()) {
        // search by name
        query = teachersRef.orderByChild("name").equalTo(name);
        }
        else {
            // Handle the case where no search parameters are provided
                Log.e("FoundActivity", "No search parameters provided");
            return;
        }

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Teacher teacher = snapshot.getValue(Teacher.class);

                    // Additional logging for debugging
                    Log.i("FoundActivity", "Teacher found: " + teacher);

                    if (teacher != null &&
                            (name.isEmpty() || teacher.getName().toLowerCase().equals(name.toLowerCase())) &&
                            (age.isEmpty() || teacher.getAge().toLowerCase().equals(age.toLowerCase())) &&
                            (subject.isEmpty() || teacherContainsSubject(teacher, subject.toLowerCase()))) {
                        // If conditions are met, add the teacher to the list of found teachers
                        foundTeachers.add(teacher);
                        count ++;
                    }
                    if (count >= 4) {
                        break; // Exit the loop once the first 4 teachers are added to the list
                    }
                }

                // Notify the adapter that the data set has changed
                adapter.notifyDataSetChanged();

                // Add buttons based on the number of found teachers
                addButtons(foundTeachers.size());
            }

            private boolean teacherContainsSubject(Teacher teacher, String subject) {
                for (String teacherSubject : teacher.getSubjects()) {
                    if (teacherSubject.toLowerCase().equals(subject)) {
                        return true;
                    }
                }
                return false;
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Log.e("FoundActivity", "Database error: " + databaseError.getMessage());
            }
        });
    }

    private void addButtons(int numberOfTeachers) {
        buttonsLayout.removeAllViews(); // Clear existing buttons

        // Limit the number of teachers to display to a maximum of 4
        int maxTeachers = Math.min(numberOfTeachers, 4);

        for (int i = 0; i < maxTeachers; i++) {
            final int position = i; // Declare final variable to make it effectively final
            Button teacherButton = new Button(this);
            teacherButton.setText("Teacher " + (i + 1));
            teacherButton.setOnClickListener(v -> {
                // Handle button click, navigate to the teacher profile activity
                navigateToTeacherProfile(foundTeachers.get(position).getName());
            });

            buttonsLayout.addView(teacherButton);
        }
    }

    private void navigateToTeacherProfile(String teacherName) {
        // Implement the code to navigate to the teacher profile screen
        // For example:
        Intent intent = new Intent(FoundActivity.this, ViewFilesActivity.class);
        intent.putExtra("TeacherName", teacherName);
        startActivity(intent);
    }
}
