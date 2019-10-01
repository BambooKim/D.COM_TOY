package com.bambookim.dcom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent auth_check_intent = new Intent(getApplicationContext(), AuthorizationCheck.class);
        startService(auth_check_intent);
    }


    @Override
    protected void onResume() {
        super.onResume();

        finish();
    }
}
