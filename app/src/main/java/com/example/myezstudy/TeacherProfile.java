package com.example.myezstudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TeacherProfile extends AppCompatActivity {
    private Button logout ,meetings, profileEdit, links, messagesTeachButton;
    private String Username;
    private DatabaseReference teachersRef;
    private int count;
    private CalendarView calendarMettings;
    private Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_profile);
        logout = findViewById(R.id.logout);
        meetings = findViewById(R.id.meetings);
        links = findViewById(R.id.links);
        messagesTeachButton = findViewById(R.id.messagesTeachButton);
        profileEdit = findViewById(R.id.profileEdit);
        calendarMettings =  findViewById(R.id.calendarMettings);
        calendar = Calendar.getInstance();
        Username = UserInformation.getSavedUsername(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        teachersRef = database.getReference("teachers");
        getCountOfNew();
        count = 0;

        calendarMettings.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                teachersRef.orderByKey().equalTo(Username)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot teacherSnapshot : dataSnapshot.getChildren()) {
                                        Teacher teacher = teacherSnapshot.getValue(Teacher.class);
                                        if (teacher != null && teacher.getMeetings() != null) {
                                            for( Meeting M : teacher.getMeetings()){
                                                if(M.getDay()== dayOfMonth &&
                                                    M.getMonth()==( month + 1) &&
                                                    M.getYear()== year){
                                                    count++;
                                                }
                                            }
                                            Toast.makeText(TeacherProfile.this, Integer.toString(count) + " Meetings", Toast.LENGTH_SHORT).show();
                                            count = 0;
                                        }
                                    }
                                }
                            }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        count = 0;
                    }
                });
            }

        });
        messagesTeachButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Example
                startActivity(new Intent(TeacherProfile.this, Messages.class));
            }
        });
        profileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Example
                startActivity(new Intent(TeacherProfile.this, Update.class));
            }
        });
        // Handle "Logout" button click
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add your logic to perform logout (e.g., return to the login screen)
                //Example
                startActivity(new Intent(TeacherProfile.this, MainActivity.class));
                //finish(); // Close the current activity
            }
        });
        links.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add your logic to perform logout (e.g., return to the login screen)
                //Example
                startActivity(new Intent(TeacherProfile.this, Links.class));
                //finish(); // Close the current activity
            }
        });
        meetings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add your logic to perform logout (e.g., return to the login screen)
                //Example
                startActivity(new Intent(TeacherProfile.this, MeetingTeacher.class));
            }
        });
    }

    public void getCountOfNew() {
        teachersRef.orderByKey().equalTo(Username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot teacherSnapshot : dataSnapshot.getChildren()) {
                                Teacher teacher = teacherSnapshot.getValue(Teacher.class);
                                if (teacher != null) {
                                    Toast.makeText(TeacherProfile.this, "There are " + teacher.getNewMessage() + "   new messages"
                                            , Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        count = 0;
                    }
                });
    }
}