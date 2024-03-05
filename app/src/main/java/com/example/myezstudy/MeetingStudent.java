package com.example.myezstudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
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
        studentsRef.orderByKey().equalTo(studentName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                                User student = studentSnapshot.getValue(User.class);
                                if (student != null && student.getMeetings() != null) {
                                    displayMeetings(student.getMeetings(),student);
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
    private void displayMeetings(List<Meeting> MettingsList, User student) {

        ArrayAdapter<Meeting> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, MettingsList);
        MeetingListStudent.setAdapter(adapter);
        // Set a click listener on the ListView items to open links
        MeetingListStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MeetingStudent.this);
                builder.setTitle("Meeting Details");
                builder.setMessage(student.getMeetings().get(position).printDetails());
                builder.setPositiveButton("Delete Meeting", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(!student.getMeetings().get(position).checkIfCanCancel()){
                            Toast.makeText(MeetingStudent.this,
                                    "It is not possible to cancel 4 hours before the meeting"
                                    , Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            return;
                        }
                        student.Notify(student.getMeetings().get(position));
                        studentsRef.child(studentName).setValue(student);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(MeetingStudent.this, "Meeting delete successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Send Message", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MeetingStudent.this, SendChatMessage.class);
                        intent.putExtra("PartnerUsername", student.getMeetings().get(position).getPartnerUsername());
                        startActivity(intent);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
}