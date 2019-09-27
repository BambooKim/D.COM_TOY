package com.bambookim.loginpractice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    TextView textView;
    Button logOut_btn;

    String sessionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        textView = (TextView) findViewById(R.id.sessionId);
        logOut_btn = (Button) findViewById(R.id.logOut);

        sessionId = SaveSharedPreference.getUserName(getApplicationContext());
        textView.setText(sessionId);
    }

    // 로그아웃 버튼 클릭 - 로그아웃 및 홈 액티비티 종료.
    public void onLogOutBtnClicked(View v) {
        SaveSharedPreference.clearUserName(getApplicationContext());

        Intent gotoLogIn = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(gotoLogIn);

        finish();
    }
}
