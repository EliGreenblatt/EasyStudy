package com.example.myezstudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Update extends AppCompatActivity {
    private Button backUp;
    private Button updateButton;
    private TextInputLayout emailEditText, phoneEditText, bioEditText, ageEditText;
    private DatabaseReference studentsRef;
    private DatabaseReference teachersRef;
    private String username;
    private String password;

    public Update() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);


        // Grab the reference to the firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        studentsRef = database.getReference("students");
        teachersRef = database.getReference("teachers");
        // Grab the references to the back and update button
        backUp = findViewById(R.id.backUp);
        updateButton = findViewById(R.id.updateButton);
        // Get references to the TextInputEditTexts
        emailEditText = findViewById(R.id.emailUp);
        bioEditText = findViewById(R.id.bioUp);
        phoneEditText = findViewById(R.id.phoneUp);
        ageEditText = findViewById(R.id.ageUp);

        // Set click functionality for back button
        backUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Set update button functionality
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
    }



    private void updateProfile() {
        getDetails();
        Log.i("Update", "Updating profile");
        Toast.makeText(Update.this, "Updating profile...", Toast.LENGTH_SHORT).show();
        String updatedAge = ageEditText.getEditText().getText().toString();
        String updatedEmail = emailEditText.getEditText().getText().toString();
        String updatedBio = bioEditText.getEditText().getText().toString();
        String updatedPhone = phoneEditText.getEditText().getText().toString();
        if(UserInformation.KEY_Type.equals("User"))
            updateStudent(username, updatedEmail, updatedAge, updatedBio, updatedPhone );
        else
            updateTeacher(username, updatedEmail, updatedAge, updatedBio, updatedPhone );

    }

    public void updateStudent(String username, String email, String age,
                              String bio, String phone) {
        // Search in students table
        studentsRef.orderByKey().equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                            User student = studentSnapshot.getValue(User.class);
                            if (student != null ) {
                                if(!age.isEmpty())
                                    student.setAge(age);
                                if(!email.isEmpty())
                                    student.setEmail(email);
                                if(!bio.isEmpty())
                                    student.setBio(bio);
                                if(!phone.isEmpty())
                                    student.setPhone(phone);
                                studentsRef.child(username).setValue(student);
                                Toast.makeText(Update.this, "The data has been updated", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    // Method to update teacher
    public void updateTeacher(String username, String email, String age, String bio, String phone) {
        // Search in teachers table
        teachersRef.orderByKey().equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot teacherSnapshot : dataSnapshot.getChildren()) {
                            Teacher teacher = teacherSnapshot.getValue(Teacher.class);
                            if (teacher != null ) {
                                if(!age.isEmpty())
                                    teacher.setAge(age);
                                if(!email.isEmpty())
                                    teacher.setEmail(email);
                                if(!bio.isEmpty())
                                    teacher.setBio(bio);
                                if(!phone.isEmpty())
                                    teacher.setPhone(phone);
                                teachersRef.child(username).setValue(teacher);
                                Toast.makeText(Update.this, "The data has been updated", Toast.LENGTH_SHORT).show();

                                return;
                            }
                        }
                        // If not found in teachers table
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }


    private void getDetails() {
        // Load saved username and password
        password = UserInformation.getSavedPassword(this);
        username = UserInformation.getSavedUsername(this);
    }
}
