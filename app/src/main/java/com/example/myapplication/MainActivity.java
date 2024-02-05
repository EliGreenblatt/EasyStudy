package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.CheckBox;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = "MainActivity";
    CheckBox teacher;
    Button loginButton;
    Button registerButton;
    Button login;
    Button register;
    TextInputLayout usernameInputLayout;
    TextInputLayout userPassInputLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        teacher = findViewById(R.id.checkBox); // Replace with your CheckBox ID
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        loginButton = findViewById(R.id.loginButton); // Grab the login button reference
        registerButton = findViewById(R.id.registerButton); // Grab the register button reference
        usernameInputLayout = findViewById(R.id.UsernameText); // Grab the input username text
        userPassInputLayout = findViewById(R.id.PasswordText); // Grab the input user password text

        teacher.setVisibility(View.GONE);
        register.setVisibility(View.GONE);
        hideTextFields();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Login button click
                Log.i(TAG,"Login button clicked");
                hideButtons();
                showTextFields();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // Handle Login button click
                Log.i(TAG,"Second Login Pressed");
            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Login button click
                Log.i(TAG,"Register button clicked");
                hideButtons();
                showTextFields();
                loginButton.setVisibility(View.GONE);
                login.setVisibility(View.GONE);
                teacher.setVisibility(View.VISIBLE);
                register.setVisibility(View.VISIBLE);
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Login button click
                Log.i(TAG,"Register button clicked second time");
               // Add logic that registers do the database
            }
        });
        
    }

    private void hideTextFields()
    {
        usernameInputLayout.setVisibility(View.GONE); // Hide the password field
        userPassInputLayout.setVisibility((View.GONE));// Hide the password field
        login.setVisibility(View.GONE);

    }

    private void showTextFields()
    {
        usernameInputLayout.setVisibility(View.VISIBLE); // Hide the password field
        userPassInputLayout.setVisibility(View.VISIBLE);//  Hide the password field
        login.setVisibility(View.VISIBLE);
    }

    private void hideButtons()
    {
        loginButton.setVisibility(View.GONE);
        registerButton.setVisibility(View.GONE);
    }
}