package com.example.myezstudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SendChatMessage extends AppCompatActivity {
     Button backButtonChat, sendMsg;
     TextInputLayout inputMsg;
     DatabaseReference Ref;
     String name, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_chat_message);
        backButtonChat = findViewById(R.id.backButtonChat);
        sendMsg = findViewById(R.id.sendMsg);
        inputMsg = findViewById(R.id.inputMsg);
        name = UserInformation.getSavedUsername(this);
        username = getIntent().getStringExtra("PartnerUsername");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        if (UserInformation.KEY_Type.equals("User"))
            Ref = database.getReference("teachers");
        else
            Ref = database.getReference("students");

        backButtonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();  // Close the activity when the back button is clicked
            }
        });
        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inputMsg.getEditText().getText().toString().isEmpty()) {
                    Toast.makeText(SendChatMessage.this, "Please enter message", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendMessage( new ChatMessage(name, inputMsg.getEditText().getText().toString()));

            }
        });
    }
    private void sendMessage(ChatMessage newCM) {
        Ref.orderByKey().equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                                User user = Snapshot.getValue(User.class);
                                if (UserInformation.KEY_Type.equals("User"))
                                    user = Snapshot.getValue(Teacher.class);
                                if (user != null) {
                                    user.addChatMessage(newCM);
                                    Ref.child(username).setValue(user);
                                    Toast.makeText(SendChatMessage.this, "the message has been sent", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(SendChatMessage.this, "Error to get partner", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}