package com.bambookim.dcom;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

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

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    ImageView logo;
    EditText user_id;
    EditText user_pw;
    EditText http_url;
    Button send_login;
    Button login_signup;

    LinearLayout layout;

    boolean isKeyboardShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logo = (ImageView) findViewById(R.id.loginLogo);
        user_id = (EditText) findViewById(R.id.user_id);
        user_pw = (EditText) findViewById(R.id.user_pw);
        http_url = (EditText) findViewById(R.id.http_url);
        send_login = (Button) findViewById(R.id.send_login);
        login_signup = (Button) findViewById(R.id.login_signup);

        layout = (LinearLayout) findViewById(R.id.login_layout);

        layout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        Rect r = new Rect();
                        layout.getWindowVisibleDisplayFrame(r);
                        int screenHeight = layout.getRootView().getHeight();

                        int keypadHeight = screenHeight - r.bottom;

                        Log.d(TAG, "keypadHeight = " + keypadHeight);

                        if (keypadHeight > screenHeight * 0.15) {
                            if (!isKeyboardShowing) {
                                isKeyboardShowing = true;
                                logo.setVisibility(View.GONE);
                            }
                        } else {
                            if (isKeyboardShowing) {
                                isKeyboardShowing = false;
                                logo.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
        );
    }

    // 로그인 버튼을 눌렀을 때 리스너
    public void onLoginClicked(View v) {
        new LoginTask(LoginActivity.this).execute("http://" + http_url.getText() + "/api/login");
        Log.d("http_url", "http://" + http_url.getText() + "/api/login");
    }

    public void onLoginSignupClicked(View v) {
        Intent loginToSignup = new Intent(getApplicationContext(), SignupActivity.class);
        startActivity(loginToSignup);
    }

    public class LoginTask extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        private Context mContext;

        public LoginTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("로그인 정보 가져오는 중...");
            progressDialog.setCancelable(true);
            progressDialog.setProgressStyle(R.style.Widget_AppCompat_ProgressBar_Horizontal);

            progressDialog.show();

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("user_id", user_id.getText().toString().trim());
                jsonObject.accumulate("user_pw", user_pw.getText().toString());
                jsonObject.accumulate("token", SaveSharedPreference.getToken(getApplicationContext()));

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

            if (!result.equals("Login Fail")) {     // Success
                Toast.makeText(getApplicationContext(), result + " : " + user_id.getText().toString(), Toast.LENGTH_SHORT).show();

                SaveSharedPreference.setPreferences(1, getApplicationContext(), user_id.getText().toString());

                Intent resultIntent = new Intent(LoginActivity.this, HomeActivity.class);
                resultIntent.putExtra("login_result", true);
                resultIntent.putExtra("session_id", user_id.getText().toString());

                startActivity(resultIntent);

                finish();
            } else {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            }

        }
    }
}
