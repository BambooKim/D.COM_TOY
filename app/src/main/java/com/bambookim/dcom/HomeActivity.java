package com.bambookim.dcom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HomeActivity extends AppCompatActivity {
    private final static String TAG = "HomeActivity";

    TextView textView;
    Button logOut_btn, writePush;

    String sessionId;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        textView = (TextView) findViewById(R.id.sessionId);
        logOut_btn = (Button) findViewById(R.id.logOut);
        writePush = (Button) findViewById(R.id.writePush);

        sessionId = SaveSharedPreference.getUserName(getApplicationContext());
        token = SaveSharedPreference.getToken(getApplicationContext());
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
        new LogoutTask(this).execute("http://192.168.0.7:3000/api/logout");
    }

    public void onwritePushClicked(View v) {
        Intent writePush = new Intent(this, SendPushActivity.class);
        startActivity(writePush);
    }

    public class LogoutTask extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        private Context mContext;

        public LogoutTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("로그아웃 처리 중...");
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(R.style.Widget_AppCompat_ProgressBar_Horizontal);

            progressDialog.show();

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("sessionId", sessionId);
                jsonObject.accumulate("token", token);

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    //URL url = new URL("http://192.168.25.16:3000/users");
                    URL url = new URL(urls[0]);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Cache-Control", "no-cache");
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setRequestProperty("Accept", "text/html");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    con.connect();

                    OutputStream outStream = con.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();

                    InputStream stream = con.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();

                    String line = "";
                    while((line = reader.readLine()) != null){
                        buffer.append(line);
                    }

                    return buffer.toString();

                } catch (MalformedURLException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(con != null){
                        con.disconnect();
                    }
                    try {
                        if(reader != null){
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            if (result.equals("Success")) {
                Toast.makeText(getApplicationContext(), "로그아웃되었습니다.", Toast.LENGTH_SHORT).show();

                SaveSharedPreference.clearPreferences(getApplicationContext());

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FirebaseInstanceId.getInstance().deleteInstanceId();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                Intent gotoLogIn = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(gotoLogIn);

                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
