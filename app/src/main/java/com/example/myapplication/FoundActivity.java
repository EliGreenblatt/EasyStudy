package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found);

        // Retrieve search parameters from the Intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("NAME");
        String age = intent.getStringExtra("AGE");
        String subject = intent.getStringExtra("SUBJECT");

        Log.i("FoundActivity", name);
        Log.i("FoundActivity", age);
        Log.i("FoundActivity", subject);

        // Initialize views
        teacherListView = findViewById(R.id.teacherListView);
        foundTeachers = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, foundTeachers);
        teacherListView.setAdapter(adapter);

        // Query the "teachers" node in the database based on search parameters
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("teachers");
        Query query = databaseReference.orderByChild("name").equalTo(name);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                foundTeachers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Teacher teacher = snapshot.getValue(Teacher.class);
                    // Check additional filters (e.g., age, subject) here if needed
                    if (teacherMatchesFilters(teacher, age, subject)) {
                        foundTeachers.add(teacher);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FoundActivity", "Database error: " + databaseError.getMessage());
            }
        });
    }

    private boolean teacherMatchesFilters(Teacher teacher, String age, String subject) {
        // Implement your filtering logic based on age and subject here
        // For example:
        // if (!teacher.getAge().equals(age)) {
        //     return false;
        // }
        // if (!teacher.getSubject().equals(subject)) {
        //     return false;
        // }
        // return true;
        return true; // Placeholder, update with your filtering logic
    }
}
