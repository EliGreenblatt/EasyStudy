package com.example.myezstudy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {
    Button LoginButton, RegisterButton;
    TextInputLayout UsernameLog, PasswordLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        UsernameLog = (TextInputLayout) findViewById(R.id.UsernameLog);
        PasswordLog = (TextInputLayout) findViewById(R.id.PasswordLog);
        LoginButton = (Button) findViewById(R.id.LoginButton);
        RegisterButton = (Button) findViewById(R.id.RegisterButton);

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("BUTTONS", "User want to register");
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);


            }
        });
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Main", "User want to login");
                // Get the entered username and password

                String username = UsernameLog.getEditText().getText().toString();
                String password = PasswordLog.getEditText().getText().toString();
                if (username.isEmpty() || password.isEmpty())
                {
                    Log.e("Error", "Problem with username/password");
                    return;
                }
                // Check if it's a teacher or student
               EasyStudy.checkUserExists(UsernameLog.getEditText().getText().toString(), PasswordLog.getEditText().getText().toString()
                        , MainActivity.this, new EasyStudy.UserTypeCallback() {
                    @Override
                    public void onUserType(EasyStudy.UserType userType, int i) {
                        // Determine the user type based on the integer i
                        if (i == 0) {
                            // User type is teacher
                            userType = EasyStudy.UserType.TEACHER;

                        } else if (i == 1) {
                            // User type is student
                            userType = EasyStudy.UserType.STUDENT;

                        } else {
                            // Unknown user type
                            userType = EasyStudy.UserType.UNKNOWN;
                            EasyStudy.showErrorMessageDialog(MainActivity.this, "User is not registered in the system.");
                            return;
                        }

                        UserInformation.saveUserCredentials(MainActivity.this, username, password);

                        // Navigate to the appropriate page based on user type
                        EasyStudy.navigateToPage(MainActivity.this, userType);
                    }
                });
            }
        });
    }

}