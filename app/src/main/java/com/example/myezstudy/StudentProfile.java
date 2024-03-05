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

public class StudentProfile extends AppCompatActivity {

    Button logout, ProfileEditStud, Search, MeetingStud, messagesStudButton;
    private DatabaseReference studentsRef;
    private String Username;
    private int count;
    private CalendarView calendarMettings;
    private Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        calendarMettings =  findViewById(R.id.calendarView);
        messagesStudButton =findViewById(R.id.messagesStudButton);
        calendar = Calendar.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        studentsRef = database.getReference("students");
        Username = UserInformation.getSavedUsername(this);
        logout =  (Button) findViewById(R.id.logoutStud);
        // Handle "Logout" button click
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add your logic to perform logout (e.g., return to the login screen)
                //Example
                startActivity(new Intent(StudentProfile.this, MainActivity.class));
                //finish(); // Close the current activity
            }
        });
        messagesStudButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Example
                startActivity(new Intent(StudentProfile.this, Messages.class));
            }
        });
        ProfileEditStud =  (Button) findViewById(R.id.ProfileEditStud);
        // Handle "Logout" button click
        ProfileEditStud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Example
                startActivity(new Intent(StudentProfile.this, Update.class));
            }
        });
        Search =  (Button) findViewById(R.id.Search);
        // Handle "Logout" button click
        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add your logic to perform logout (e.g., return to the login screen)
                //Example
                startActivity(new Intent(StudentProfile.this, SearchTeachers.class));
                //finish(); // Close the current activity
            }
        });
        MeetingStud =  findViewById(R.id.MeetingStud);

        MeetingStud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add your logic to perform logout (e.g., return to the login screen)
                //Example
                startActivity(new Intent(StudentProfile.this, MeetingStudent.class));
                //finish(); // Close the current activity
            }
        });

        ProfileEditStud =  (Button) findViewById(R.id.ProfileEditStud);
        // Handle "Logout" button click
        ProfileEditStud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add your logic to perform logout (e.g., return to the login screen)
                //Example
                startActivity(new Intent(StudentProfile.this, Update.class));

                //finish(); // Close the current activity
            }
        });
        getCountOfNew();
        count = 0;
        calendarMettings.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                studentsRef.orderByKey().equalTo(Username)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                                        User student = studentSnapshot.getValue(User.class);
                                        if (student != null && student.getMeetings() != null) {
                                            for( Meeting M : student.getMeetings()){

                                                if(M.getDay() == dayOfMonth  &&
                                                        M.getMonth() == (month + 1) &&
                                                        M.getYear() == year){
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

        });
    }
    public void getCountOfNew() {
        studentsRef.orderByKey().equalTo(Username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                                User student = studentSnapshot.getValue(User.class);
                                if (student != null) {
                                    Toast.makeText(StudentProfile.this, "There are " + student.getNewMessage() + "   new messages"
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

