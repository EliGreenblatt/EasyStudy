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
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MeetingTeacher extends AppCompatActivity {
    Button createMeeting, backButton;
    private ListView meetingsList;

    private DatabaseReference teachersRef;
    private String teacherName;
    private CalendarView calendarCreate;
    private int dayCreate, monthCreate, yearCreate;

    TextInputLayout startCreate, endCreate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_techer);
        createMeeting =  findViewById(R.id.createMeeting);
        backButton =  findViewById(R.id.backButton);
        startCreate =  findViewById(R.id.startCreate);
        endCreate = findViewById(R.id.endCreate);
        meetingsList = findViewById(R.id.meetingsList);
        teacherName =UserInformation.getSavedUsername(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        teachersRef = database.getReference("teachers");
        loadMettings();

    backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();  // Close the activity when the back button is clicked
            }
        });
        calendarCreate =  findViewById(R.id.calendarCreate);
        calendarCreate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                yearCreate = year;
                monthCreate = month + 1;
                dayCreate = dayOfMonth;
            }

        });
        createMeeting.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isNumeric(startCreate.getEditText().getText().toString()) &&
                    isNumeric(endCreate.getEditText().getText().toString())
            ){
                if(checkDate())
                    uploadMeeting();
                else
                    Toast.makeText(MeetingTeacher.this, "The date has passed or wrong hours", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(MeetingTeacher.this, "Enter only numbers", Toast.LENGTH_SHORT).show();


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

        ArrayAdapter<Meeting> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, MettingsList);
        meetingsList.setAdapter(adapter);
        // Set a click listener on the ListView items to open links
        meetingsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MeetingTeacher.this);
                builder.setTitle("Meeting");
                builder.setMessage(teacher.getMeetings().get(position).toString());
                builder.setPositiveButton("Delete Meeting", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        teacher.NotifyToDeleteMeeting(teacher.getMeetings().get(position));
                        teacherSnapshot.getRef().child("meetings").setValue(teacher.getMeetings());
                        adapter.notifyDataSetChanged();
                        Toast.makeText(MeetingTeacher.this, "Meeting delete successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
    private void uploadMeeting() {
        Meeting newMeeting = new Meeting(Integer.toString(dayCreate),
                Integer.toString(monthCreate),
                Integer.toString(yearCreate),
                startCreate.getEditText().getText().toString(),
                endCreate.getEditText().getText().toString());        // Check if at least one link is provided
        if (newMeeting == null ) {
            Toast.makeText(MeetingTeacher.this, "Please enter all the details", Toast.LENGTH_SHORT).show();
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
                                    if (teacher != null ) {
                                        if(teacher.getMeetings() != null){
                                            for (Meeting meeting : teacher.getMeetings()) {
                                                if (meeting.CompareTo(newMeeting)) {
                                                    Toast.makeText(MeetingTeacher.this, "There is a meeting at this time", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }
                                            }
                                        }

                                        teacher.addMeeting(newMeeting);
                                        teachersRef.child(teacherSnapshot.getKey()).setValue(teacher);
                                        Toast.makeText(MeetingTeacher.this, "Meeting created successfully", Toast.LENGTH_SHORT).show();
                                        recreate();

                                    }
                                }
                            }
                            else {
                                Toast.makeText(MeetingTeacher.this, "Cant create meeting", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(MeetingTeacher.this, "Database error", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Teacher name not available", Toast.LENGTH_SHORT).show();
        }
    }
    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
    public boolean checkDate() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        if(Integer.parseInt(startCreate.getEditText().getText().toString()) <= 0 ||
                Integer.parseInt(startCreate.getEditText().getText().toString()) >24 )
            return false;
        if(Integer.parseInt(endCreate.getEditText().getText().toString()) <= 0 ||
                Integer.parseInt(endCreate.getEditText().getText().toString()) >24 )
            return false;
        if(Integer.parseInt(endCreate.getEditText().getText().toString()) <=
                Integer.parseInt( startCreate.getEditText().getText().toString()) )
            return false;
        if (year > yearCreate)
            return false;
        else if (year == yearCreate && month > monthCreate)
            return false;
        else if (year == yearCreate && month == monthCreate &&
                day > dayCreate)
            return false;
        return true;
    }
}