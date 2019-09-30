package com.bambookim.loginpractice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MsgActivity extends AppCompatActivity {

    private TextView MsgTitle, MsgMessage;
    private String title, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);


        MsgTitle = (TextView) findViewById(R.id.MsgTitle);
        MsgMessage = (TextView) findViewById(R.id.MsgMessage);

        Intent receivedIntent = getIntent();
        title = receivedIntent.getStringExtra("title");
        message = receivedIntent.getStringExtra("message");

        MsgTitle.setText(title);
        MsgMessage.setText(message);
    }


}
