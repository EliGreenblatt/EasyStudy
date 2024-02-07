package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found);

        // Initialize Firebase database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        teachersRef = database.getReference("teachers");

        // Retrieve search parameters from the Intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("NAME");
        String age = intent.getStringExtra("AGE");
        String subject = intent.getStringExtra("SUBJECT");

        // Initialize ListView and List
        teacherListView = findViewById(R.id.teacherListView);
        foundTeachers = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, foundTeachers);
        teacherListView.setAdapter(adapter);

        // Initialize buttonsLayout
        buttonsLayout = findViewById(R.id.buttonsLayout);

        // Perform the search in Firebase database
        Query query = teachersRef.orderByChild("name").equalTo(name);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Teacher teacher = snapshot.getValue(Teacher.class);
                    if (teacher != null && teacher.getName().equals(name) && teacher.getAge().equals(age) && teacher.getSubjects().contains(subject)) {
                        // If all conditions are met, add the teacher to the list of found teachers
                        foundTeachers.add(teacher);
                    }
                }
                // Notify the adapter that the data set has changed
                adapter.notifyDataSetChanged();

                // Add buttons based on the number of found teachers
                addButtons(foundTeachers.size());
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

        for (int i = 0; i < numberOfTeachers; i++) {
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
