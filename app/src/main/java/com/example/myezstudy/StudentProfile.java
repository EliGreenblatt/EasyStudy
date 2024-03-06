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

/**
 * student's profile.
 */
public class StudentProfile extends AppCompatActivity {

    Button logout, ProfileEditStud, Search, MeetingStud, messagesStudButton;
    private DatabaseReference studentsRef;
    private String Username;
    private int count;
    private CalendarView calendarMettings;
    private Calendar calendar;

    //Initializes the StudentProfile activity.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        // Initialize UI components
        calendarMettings = findViewById(R.id.calendarView);
        messagesStudButton = findViewById(R.id.messagesStudButton);
        calendar = Calendar.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        studentsRef = database.getReference("students");
        Username = UserInformation.getSavedUsername(this);
        logout = findViewById(R.id.logoutStud);
        ProfileEditStud = findViewById(R.id.ProfileEditStud);
        Search = findViewById(R.id.Search);
        MeetingStud = findViewById(R.id.MeetingStud);

        // Handle "Logout" button click
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to the login screen
                startActivity(new Intent(StudentProfile.this, MainActivity.class));
            }
        });

        // Handle "View Messages" button click
        messagesStudButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentProfile.this, Messages.class));
            }
        });

        // Handle "Edit Profile" button click
        ProfileEditStud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentProfile.this, Update.class));
            }
        });

        // Handle "Search Teachers" button click
        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentProfile.this, SearchTeachers.class));
            }
        });

        // Handle "View Meetings" button click
        MeetingStud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentProfile.this, MeetingStudent.class));
            }
        });

        // Count meetings for the selected date on the calendar
        calendarMettings.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                countMeetings(dayOfMonth, month, year);
            }
        });

        // Display the count of new messages
        getCountOfNew();
        count = 0;
    }

    /**
     * Count meetings for the selected date on the calendar.
     *
     * @param dayOfMonth The selected day.
     * @param month      The selected month.
     * @param year       The selected year.
     */
    private void countMeetings(int dayOfMonth, int month, int year) {
        studentsRef.orderByKey().equalTo(Username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                        User student = studentSnapshot.getValue(User.class);
                        if (student != null && student.getMeetings() != null) {
                            for (Meeting M : student.getMeetings()) {
                                if (M.getDay() == dayOfMonth &&
                                        M.getMonth() == (month + 1) &&
                                        M.getYear() == year) {
                                    count++;
                                }
                            }
                            Toast.makeText(StudentProfile.this, Integer.toString(count) + " Meetings", Toast.LENGTH_SHORT).show();
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

    /**
     * Get the count of new messages and display a toast.
     */
    public void getCountOfNew() {
        studentsRef.orderByKey().equalTo(Username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                        User student = studentSnapshot.getValue(User.class);
                        if (student != null) {
                            Toast.makeText(StudentProfile.this, "There are " + student.getNewMessage() + " new messages", Toast.LENGTH_SHORT).show();
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
