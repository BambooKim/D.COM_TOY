package com.bambookim.loginpractice;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class FirstAuthActivity extends AppCompatActivity {

    private Intent intent;
    boolean login_status;
    String session_id;

    TextView textView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_auth);

        textView3 = (TextView) findViewById(R.id.textView3);

        if (SaveSharedPreference.getUserName(FirstAuthActivity.this).length() == 0) {
            // Call Login Activity
            intent = new Intent(FirstAuthActivity.this, LoginActivity.class);

            startActivityForResult(intent, 3000);
        } else {
            // Call Next Activity
            intent = new Intent(FirstAuthActivity.this, MainActivity.class);
            //intent.putExtra("STD_NUM", SaveSharedPreference.getUserName(this).toString());
            //startActivity(intent);
            //this.finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 3000:
                    login_status = data.getBooleanExtra("login_result", false);
                    session_id = data.getStringExtra("session_id");

                    textView3.setText(session_id);

                    finish();

                    break;
            }
        }
    }
}
