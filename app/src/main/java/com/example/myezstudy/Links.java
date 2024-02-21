package com.example.myezstudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Links extends AppCompatActivity {
    private ListView linksList;
    TextInputLayout enterLink;

    private DatabaseReference teachersRef, studentRef;
    private String teacherName;
    private Button backButton2, addLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_links);
        linksList = findViewById(R.id.linksList);
        backButton2 = findViewById(R.id.backButton2);
        enterLink = findViewById(R.id.enterLink);
        addLink =  findViewById(R.id.addLink);
        // back button takes us back to the previous page
        backButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        teachersRef = database.getReference("teachers");

        // Retrieve teacher name directly without checking the intent
        teacherName = UserInformation.getSavedUsername(this);

        if (teacherName == null || teacherName.isEmpty()) {
            // Handle the case where teacherName is not provided
            finish(); // Close the activity if teacherName is not available
        }

        loadFiles();
        addLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                uploadLinks();;
            }
        });

    }
    private void uploadLinks() {
        String link = enterLink.getEditText().getText().toString().trim();
        // Check if at least one link is provided
        if (link.isEmpty() ) {
            Toast.makeText(this, "Please enter at least one link", Toast.LENGTH_SHORT).show();
            return;
        }

        // Assuming you have the teacher's name, search for the teacher in the database
        if (teacherName != null && !teacherName.isEmpty()) {
            teachersRef.orderByChild("name").equalTo(teacherName)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot teacherSnapshot : dataSnapshot.getChildren()) {
                                    Teacher teacher = teacherSnapshot.getValue(Teacher.class);
                                    if (teacher != null) {
                                        // Update links for the teacher object
                                        teacher.addLinks(link);

                                        // Push the updated teacher object back to Firebase
                                        teachersRef.child(teacherSnapshot.getKey()).setValue(teacher);

                                        // Notify the user that links have been uploaded
                                        Toast.makeText(Links.this, "Links uploaded successfully", Toast.LENGTH_SHORT).show();
                                        recreate();
                                    }
                                }
                            } else {
                                Toast.makeText(Links.this, "Teacher not found", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(Links.this, "Database error", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Teacher name not available", Toast.LENGTH_SHORT).show();
        }
    }



    private void loadFiles() {
        teachersRef.orderByChild("name").equalTo(teacherName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot teacherSnapshot : dataSnapshot.getChildren()) {
                                Teacher teacher = teacherSnapshot.getValue(Teacher.class);
                                if (teacher != null && teacher.getLinks() != null) {
                                    displayFiles(teacher.getLinks());
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle error
                    }
                });
    }

    private void displayFiles(List<String> files) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item_link, R.id.linkTextView, files);
        linksList.setAdapter(adapter);

        // Set a click listener on the ListView items to open links
        linksList.setOnItemClickListener((parent, view, position, id) -> {
            String selectedLink = files.get(position);
            if (isValidLink(selectedLink)) {
                openLinkInBrowser(selectedLink);
            }
        });

        // Make links in the ListView clickable
        for (int i = 0; i < linksList.getChildCount(); i++) {
            TextView textView = (TextView) linksList.getChildAt(i).findViewById(R.id.linkTextView);
            String fileText = files.get(i);
            if (isValidLink(fileText)) {
                Linkify.addLinks(textView, Linkify.WEB_URLS);
                textView.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
    }

    private boolean isValidLink(String text) {
        // Implement your logic to check if the text is a valid link
        // For simplicity, you can use android.util.Patterns.WEB_URL.matcher
        return android.util.Patterns.WEB_URL.matcher(text).matches();
    }


    private void openLinkInBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}