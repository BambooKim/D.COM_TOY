package com.bambookim.loginpractice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button btn_login, btn_sign, btn_logout;
    TextView textView;

    boolean logined;
    String current_session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_sign = (Button) findViewById(R.id.btn_sign);
        btn_logout = (Button) findViewById(R.id.btn_logout);
        textView = (TextView) findViewById(R.id.textView);

        current_session = SaveSharedPreference.getUserName(getApplicationContext());
        logined = !(current_session.equals(""));

        if (logined) {
            textView.setText(current_session);

            btn_sign.setVisibility(View.GONE);
            btn_login.setVisibility(View.GONE);
        } else {
            btn_logout.setVisibility(View.GONE);
        }

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FirstAuthActivity.class);

                startActivity(intent);
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveSharedPreference.clearUserName(getApplicationContext());

                btn_sign.setVisibility(View.VISIBLE);
                btn_login.setVisibility(View.VISIBLE);
                btn_logout.setVisibility(View.GONE);

                textView.setText("");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_sign = (Button) findViewById(R.id.btn_sign);
        btn_logout = (Button) findViewById(R.id.btn_logout);
        textView = (TextView) findViewById(R.id.textView);

        current_session = SaveSharedPreference.getUserName(getApplicationContext());
        logined = !(current_session.equals(""));

        if (logined) {
            textView.setText(current_session);

            btn_sign.setVisibility(View.GONE);
            btn_login.setVisibility(View.GONE);

            btn_logout.setVisibility(View.VISIBLE);
        } else {
            btn_logout.setVisibility(View.GONE);
        }

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FirstAuthActivity.class);

                startActivity(intent);
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveSharedPreference.clearUserName(getApplicationContext());

                btn_sign.setVisibility(View.VISIBLE);
                btn_login.setVisibility(View.VISIBLE);
                btn_logout.setVisibility(View.GONE);

                textView.setText("");
            }
        });
    }
}
