package com.example.myapplication;

import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ViewFilesActivity extends AppCompatActivity {

    private ListView filesListView;
    private DatabaseReference teachersRef;
    private String teacherName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_files);

        filesListView = findViewById(R.id.filesListView);

        // Initialize Firebase database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        teachersRef = database.getReference("teachers");

        // Retrieve teacher name directly without checking the intent
        teacherName = getIntent().getStringExtra("TeacherName");

        if (teacherName == null || teacherName.isEmpty()) {
            // Handle the case where teacherName is not provided
            finish(); // Close the activity if teacherName is not available
        }

        loadFiles();
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
        filesListView.setAdapter(adapter);

        // Set a click listener on the ListView items to open links
        filesListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedLink = files.get(position);
            openLinkInBrowser(selectedLink);
        });

        // Make links in the ListView clickable
        for (int i = 0; i < filesListView.getChildCount(); i++) {
            TextView textView = (TextView) filesListView.getChildAt(i).findViewById(R.id.linkTextView);
            Linkify.addLinks(textView, Linkify.WEB_URLS);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    private void openLinkInBrowser(String url) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        }
}
