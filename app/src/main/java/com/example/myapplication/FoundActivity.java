package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
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
    }
}
