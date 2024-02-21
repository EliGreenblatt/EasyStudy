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
    private Button logout ,meetings, profileEdit, links;
    private String name;
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
        profileEdit = findViewById(R.id.profileEdit);
        calendarMettings =  findViewById(R.id.calendarMettings);
        calendar = Calendar.getInstance();
        name = UserInformation.getSavedUsername(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        teachersRef = database.getReference("teachers");
        getDate();
        count = 0;
        calendarMettings.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                teachersRef.orderByChild("name").equalTo(name)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot teacherSnapshot : dataSnapshot.getChildren()) {
                                        Teacher teacher = teacherSnapshot.getValue(Teacher.class);
                                        if (teacher != null && teacher.getMeetings() != null) {
                                            for( Meeting M : teacher.getMeetings()){
                                                if(M.getDay().equals(Integer.toString(dayOfMonth)) &&
                                                    M.getMonth().equals(Integer.toString(month + 1)) &&
                                                    M.getYear().equals(Integer.toString(year))){
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

        public void getDate(){
        long date = calendarMettings.getDate();
        SimpleDateFormat SimpleDate = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
        calendar.setTimeInMillis(date);
        String selected_date = SimpleDate.format(calendar.getTime());
        Toast.makeText(this,selected_date, Toast.LENGTH_SHORT ).show();
    }

}