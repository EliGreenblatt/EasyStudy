package com.example.myezstudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Messages extends AppCompatActivity {
    Button backButtonMes;
    private ListView MessageList, ChatList;

    private DatabaseReference Ref;
    private String name;
    private String studentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        backButtonMes = findViewById(R.id.backButtonMes);
        MessageList = findViewById(R.id.MessageList);
        ChatList = findViewById(R.id.ChatList);
        name = UserInformation.getSavedUsername(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        if (UserInformation.KEY_Type.equals("User"))
            Ref = database.getReference("students");
        else
            Ref = database.getReference("teachers");
        loadMessages();
        Ref.child(name).child("newMessage").setValue(0);
        backButtonMes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();  // Close the activity when the back button is clicked
            }
        });
    }
    private void loadMessages() {
        Ref.orderByKey().equalTo(name)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                                    User user = Snapshot.getValue(User.class);
                                    if(UserInformation.KEY_Type.equals("Teacher"))
                                        user = Snapshot.getValue(Teacher.class);
                                if (user != null && user.getMessages() != null)
                                    displayMessages(user.getMessages(),user);
                                if(user != null && user.getChatMessages() != null)
                                    displayChatMessages(user.getChatMessages(),user);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle error
                    }
                });
    }
    private void displayChatMessages(List<ChatMessage> ChatMessagesList, User user) {

        ArrayAdapter<ChatMessage> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ChatMessagesList);
        ChatList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        // Set a click listener on the ListView items to open links
        ChatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Messages.this);
                builder.setTitle("Message");
                builder.setMessage(user.getChatMessages().get(position).prineMsg());
                builder.setPositiveButton("Delete Message?", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        user.getChatMessages().remove(user.getChatMessages().get(position));
                        Ref.child(name).setValue(user);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(Messages.this, "Message delete successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Reply", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Messages.this, SendChatMessage.class);
                        intent.putExtra("PartnerUsername", user.getChatMessages().get(position).getPartner());
                        startActivity(intent);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
    private void displayMessages(List<Message> MessagesList, User user) {

        ArrayAdapter<Message> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, MessagesList);
        MessageList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        // Set a click listener on the ListView items to open links
        MessageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Messages.this);
                builder.setTitle("Message");
                builder.setMessage(user.getMessages().get(position).toString());
                builder.setPositiveButton("Delete Message?", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        user.getMessages().remove(user.getMessages().get(position));
                        Ref.child(name).setValue(user);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(Messages.this, "Message delete successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
}