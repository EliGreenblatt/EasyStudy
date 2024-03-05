package com.example.myezstudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchTeachers extends AppCompatActivity {
    private ListView teacherListView;
    private List<String> foundTeachers;
    private List<String> foundUsernames;
    private ArrayAdapter adapter;
    private DatabaseReference teachersRef;
    private LinearLayout buttonsLayout;
    Button SearchButton, back;
    TextInputLayout ByName, ByUsername, BySubject;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_teachers);

        back = findViewById(R.id.back);
        SearchButton = findViewById(R.id.SearchButton);
        ByName = findViewById(R.id.ByName);
        ByUsername = findViewById(R.id.ByUserName);
        BySubject = findViewById(R.id.BySubject);


        // back button takes us back to the previous page
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(SearchTeachers.this, StudentProfile.class);
                startActivity(intent);
            }
        });

        // we take the input from the text fields and search the teacher
        // table for a match
        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the input values
                String name = ByName.getEditText().getText().toString().trim().toLowerCase();
                String username = ByUsername.getEditText().getText().toString().trim().toLowerCase();
                String subject = BySubject.getEditText().getText().toString().trim().toLowerCase();
                // Initialize Firebase database reference
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                teachersRef = database.getReference("teachers");
                // Initialize ListView and List
                teacherListView = findViewById(R.id.TeacherListView);
                foundTeachers = new ArrayList<>();
                foundUsernames = new ArrayList<>();
                adapter = new ArrayAdapter<>(SearchTeachers.this,android.R.layout.simple_list_item_1, foundTeachers);
                teacherListView.setAdapter(adapter);

                Log.i("TAG", name);
                Log.i("TAG", username);
                Log.i("TAG", subject);
                // Perform the search in Firebase database
                // Perform the search in Firebase database
                Query query;
                teachersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot teacherSnapshot : dataSnapshot.getChildren()) {
                                Teacher teacher = teacherSnapshot.getValue(Teacher.class);
                                Log.i("FoundActivity", "Teacher found: " + teacher);
                                if (teacher != null &&
                                        (name.isEmpty() || teacher.getName().toLowerCase().equals(name)) &&
                                        (username.isEmpty() || Objects.requireNonNull(teacherSnapshot.getKey())
                                                .toLowerCase().equals(username)) &&
                                        (subject.isEmpty() || teacherContainsSubject(teacher, subject))) {
                                    // If conditions are met, add the teacher to the list of found teachers
                                    foundTeachers.add("Username: " + teacherSnapshot.getKey() +"\n" + teacher
                                    + "\n");
                                    foundUsernames.add(teacherSnapshot.getKey());
                                }
                            }
                        }
                        // Notify the adapter that the data set has changed
                        adapter.notifyDataSetChanged();


                        teacherListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                Intent intent = new Intent(SearchTeachers.this, ViewTeacher.class);
                                intent.putExtra("TeacherUsername", foundUsernames.get(position));
                                startActivity(intent);
                            }
                        });
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
        });

    }
}
