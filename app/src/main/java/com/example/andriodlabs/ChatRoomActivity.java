package com.example.andriodlabs;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class ChatRoomActivity extends AppCompatActivity {

    public static EditText editText;
    public static ChatListAdapter chatListAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listy);
        chatListAdapter = new ChatListAdapter(this, new ArrayList<Message>());
        ListView listView = findViewById(R.id.listview);
        listView.setAdapter(chatListAdapter);


    }

    public void receiveMessage(View view){
        editText = findViewById(R.id.message);
        chatListAdapter.getArray().add(new Message(false, editText.getText().toString()));
        chatListAdapter.notifyDataSetChanged();
        editText.setText(new String(""));

    }

    public void sendMessage(View view){
        editText = findViewById(R.id.message);
        chatListAdapter.getArray().add(new Message(true, editText.getText().toString()));
        chatListAdapter.notifyDataSetChanged();
        editText.setText(new String(""));

    }


}