package com.example.myezstudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import android.content.DialogInterface;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;


public class ViewTeacher extends AppCompatActivity {

    private ListView FilesList, MeetingListView;
    private DatabaseReference teachersRef, studentRef;
    private String teacherName;
    private Button BackBut;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_teacher);
        MeetingListView = findViewById(R.id.MeetingListView);

        FilesList = findViewById(R.id.FilesList);
        BackBut = findViewById(R.id.BackBut);  // Initialize back button

        // Initialize Firebase database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        teachersRef = database.getReference("teachers");
        studentRef = database.getReference("students");

        // Retrieve teacher name directly without checking the intent
        teacherName = getIntent().getStringExtra("TeacherName");

        if (teacherName == null || teacherName.isEmpty()) {
            // Handle the case where teacherName is not provided
            finish(); // Close the activity if teacherName is not available
        }

        loadFiles();
        loadMettings();
        BackBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();  // Close the activity when the back button is clicked
            }
        });


    }
    private void loadMettings() {
        teachersRef.orderByChild("name").equalTo(teacherName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot teacherSnapshot : dataSnapshot.getChildren()) {
                                Teacher teacher = teacherSnapshot.getValue(Teacher.class);
                                if (teacher != null && teacher.getMeetings() != null) {
                                    displayMeetings(teacher.getMeetings(),teacher, teacherSnapshot);

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
    private void displayMeetings(List<Meeting> MettingsList, Teacher teacher, DataSnapshot teacherSnapshot) {
        List<Meeting> AvailabileMeetings = MettingsList;
        for(Meeting M : MettingsList){
            if(!M.ifAvailable()){
                AvailabileMeetings.remove(M);
            }
        }
        
        String nameStudent = UserInformation.getSavedUsername(this);
        ArrayAdapter<Meeting> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, AvailabileMeetings);
        MeetingListView.setAdapter(adapter);
        // Set a click listener on the ListView items to open links
        MeetingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewTeacher.this);
                builder.setTitle("Make a Meeting");
                builder.setMessage("Do u want to choose that meeting?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        studentRef.orderByChild("name").equalTo(nameStudent)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            for (DataSnapshot studentSnapShot : dataSnapshot.getChildren()) {
                                                User student = studentSnapShot.getValue(User.class);
                                                if (student != null) {
                                                    student.Notify( new Meeting( teacher,teacher.getMeetings().get(position) ));
                                                    teacher.Notify(new Meeting( student,teacher.getMeetings().get(position) ));
                                                    studentRef.child(studentSnapShot.getKey()).setValue(student);
                                                    teacherSnapshot.getRef().child("meetings").setValue(teacher.getMeetings());
                                                    Toast.makeText(ViewTeacher.this, "Meeting created successfully", Toast.LENGTH_SHORT).show();

                                                    // Notify the user that links have been uploaded
                                                }
                                            }
                                            // Push the updated teacher object back to Firebase
                                            dialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }

            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
        FilesList.setAdapter(adapter);

        // Set a click listener on the ListView items to open links
        FilesList.setOnItemClickListener((parent, view, position, id) -> {
            String selectedLink = files.get(position);
            if (isValidLink(selectedLink)) {
                openLinkInBrowser(selectedLink);
            }
        });

        // Make links in the ListView clickable
        for (int i = 0; i < FilesList.getChildCount(); i++) {
            TextView textView = (TextView) FilesList.getChildAt(i).findViewById(R.id.linkTextView);
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
