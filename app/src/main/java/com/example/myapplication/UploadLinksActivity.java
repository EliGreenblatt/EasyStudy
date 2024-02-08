package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UploadLinksActivity extends AppCompatActivity {

    private EditText linkEditText1, linkEditText2, linkEditText3;
    private Button uploadLinkButton;
    private Button backButton;

    private DatabaseReference teachersRef;
    private String teacherName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_links);

        // Initialize Firebase database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        teachersRef = database.getReference("teachers");

        // Initialize UI components
        linkEditText1 = findViewById(R.id.linkEditText1);
        linkEditText2 = findViewById(R.id.linkEditText2);
        linkEditText3 = findViewById(R.id.linkEditText3);
        uploadLinkButton = findViewById(R.id.uploadLinkButton);
        backButton = findViewById(R.id.backButton);

        // Retrieve teacher name directly without checking the intent
        teacherName = getIntent().getStringExtra("TeacherName");

        if (teacherName == null || teacherName.isEmpty()) {
            // Handle the case where teacherName is not provided
            Toast.makeText(this, "Teacher name not provided", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity if teacherName is not available
        }

        // Set click listener for the upload link button
        uploadLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadLinks();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();  // Close the activity when the back button is clicked
            }
        });
    }

    private void uploadLinks() {
        String link1 = linkEditText1.getText().toString().trim();
        String link2 = linkEditText2.getText().toString().trim();
        String link3 = linkEditText3.getText().toString().trim();

        // Check if at least one link is provided
        if (link1.isEmpty() && link2.isEmpty() && link3.isEmpty()) {
            Toast.makeText(this, "Please enter at least one link", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a list to store non-empty links
        List<String> linksToUpdate = new ArrayList<>();

        // Add non-empty links to the list
        if (!link1.isEmpty()) {
            linksToUpdate.add(link1);
        }
        if (!link2.isEmpty()) {
            linksToUpdate.add(link2);
        }
        if (!link3.isEmpty()) {
            linksToUpdate.add(link3);
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
                                        teacher.setLinks(linksToUpdate);

                                        // Push the updated teacher object back to Firebase
                                        teachersRef.child(teacherSnapshot.getKey()).setValue(teacher);

                                        // Notify the user that links have been uploaded
                                        Toast.makeText(UploadLinksActivity.this, "Links uploaded successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                Toast.makeText(UploadLinksActivity.this, "Teacher not found", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(UploadLinksActivity.this, "Database error", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Teacher name not available", Toast.LENGTH_SHORT).show();
        }
    }




//    private void uploadLinks() {
//        String link1 = linkEditText1.getText().toString().trim();
//        String link2 = linkEditText2.getText().toString().trim();
//        String link3 = linkEditText3.getText().toString().trim();
//
//        // Check if at least one link is provided
//        if (link1.isEmpty() && link2.isEmpty() && link3.isEmpty()) {
//            Toast.makeText(this, "Please enter at least one link", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Assuming you have the teacher's name, search for the teacher in the database
//        if (teacherName != null && !teacherName.isEmpty()) {
//            teachersRef.orderByChild("name").equalTo(teacherName)
//                    .addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            if (dataSnapshot.exists()) {
//                                for (DataSnapshot teacherSnapshot : dataSnapshot.getChildren()) {
//                                    String teacherId = teacherSnapshot.getKey();
//                                    updateLinksForTeacher(teacherId, link1, link2, link3);
//                                }
//                            } else {
//                                Toast.makeText(UploadLinksActivity.this, "Teacher not found", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//                            Toast.makeText(UploadLinksActivity.this, "Database error", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        } else {
//            Toast.makeText(this, "Teacher name not available", Toast.LENGTH_SHORT).show();
//        }
//    }

    private void updateLinksForTeacher(String teacherId, String link1, String link2, String link3) {
        List<String> links = new ArrayList<>();
        if (!link1.isEmpty()) links.add(link1);
        if (!link2.isEmpty()) links.add(link2);
        if (!link3.isEmpty()) links.add(link3);

        // Update links in the Firebase database for the specific teacher
        teachersRef.child(teacherId).child("links").setValue(links);

        // Notify the user that links have been uploaded
        Toast.makeText(UploadLinksActivity.this, "Links uploaded successfully", Toast.LENGTH_SHORT).show();
    }

}
