package com.example.myezstudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MeetingStudent extends AppCompatActivity {

    Button backButtonStudMet;
    private ListView MeetingListStudent;

    private DatabaseReference studentsRef;
    private String studentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_student);
        backButtonStudMet =  findViewById(R.id.backButtonStudMet);
        MeetingListStudent = findViewById(R.id.MeetingListStudent);
        studentName =UserInformation.getSavedUsername(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        studentsRef = database.getReference("students");
        loadMettings();

        backButtonStudMet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();  // Close the activity when the back button is clicked
            }
        });
    }
    private void loadMettings() {
        studentsRef.orderByChild("name").equalTo(studentName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                                User student = studentSnapshot.getValue(User.class);
                                if (student != null && student.getMeetings() != null) {
                                    displayMeetings(student.getMeetings(),student, studentSnapshot);
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
    private void displayMeetings(List<Meeting> MettingsList, User student, DataSnapshot studentSnapshot) {

        ArrayAdapter<Meeting> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, MettingsList);
        MeetingListStudent.setAdapter(adapter);
        // Set a click listener on the ListView items to open links
        MeetingListStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MeetingStudent.this);
                builder.setTitle("Meeting");
                builder.setMessage(student.getMeetings().get(position).toString());
                builder.setPositiveButton("Delete Meeting", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        student.Notify(student.getMeetings().get(position));
                        studentSnapshot.getRef().child("meetings").setValue(student.getMeetings());
                        adapter.notifyDataSetChanged();
                        Toast.makeText(MeetingStudent.this, "Meeting delete successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
}