package com.bambookim.loginpractice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class HomeActivity extends AppCompatActivity {
    private final static String TAG = "HomeActivity";

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


        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(
                new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.d(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and Toast
                        // String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, "token : " + token);
                        Toast.makeText(getApplicationContext(), token, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    // 로그아웃 버튼 클릭 - 로그아웃 및 홈 액티비티 종료.
    public void onLogOutBtnClicked(View v) {
        SaveSharedPreference.clearUserName(getApplicationContext());

        Intent gotoLogIn = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(gotoLogIn);

        finish();
    }
}
