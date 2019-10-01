package com.bambookim.dcom;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";
    static boolean isKeyboardShowing = false;

    EditText signup_name, signup_numId, signup_dept, signup_pw, signup_pwchk, signup_email, signup_http_url;
    Button send_signup;
    TextView pwchk_matches;
    LinearLayout layout, marginLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signup_name = (EditText) findViewById(R.id.signup_name);
        signup_numId = (EditText) findViewById(R.id.signup_numId);
        signup_dept = (EditText) findViewById(R.id.signup_dept);
        signup_pw = (EditText) findViewById(R.id.signup_pw);
        signup_pwchk = (EditText) findViewById(R.id.signup_pwchk);
        signup_email = (EditText) findViewById(R.id.signup_email);
        signup_http_url = (EditText) findViewById(R.id.signup_http_url);
        send_signup = (Button) findViewById(R.id.send_signup);
        pwchk_matches = (TextView) findViewById(R.id.pwchk_matches);
        pwchk_matches.setVisibility(View.GONE);
        layout = (LinearLayout) findViewById(R.id.signupLayout);
        marginLayout = (LinearLayout) findViewById(R.id.marginLayout);

        signup_pwchk.addTextChangedListener(new TextWatcher() {
            boolean matches;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                pwchk_matches.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = signup_pw.getText().toString();
                String passwordchk = s.toString();

                matches = password.equals(passwordchk);

                if (matches) {
                    pwchk_matches.setVisibility(View.GONE);
                } else {
                    pwchk_matches.setVisibility(View.VISIBLE);
                    pwchk_matches.setText("비밀번호가 일치하지 않습니다.");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (matches) {
                    pwchk_matches.setVisibility(View.GONE);
                } else {
                    pwchk_matches.setVisibility(View.VISIBLE);
                    pwchk_matches.setText("비밀번호가 일치하지 않습니다.");
                }
            }
        });

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
                                marginLayout.setVisibility(View.GONE);
                            }
                        } else {
                            if (isKeyboardShowing) {
                                isKeyboardShowing = false;
                                marginLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
        );
    }

    public void onSendSignupBtnClicked(View v) {
        new SignupTask(SignupActivity.this).execute("http://" + signup_http_url.getText() + "/api/signup");
        Log.d(TAG, "onSendSignupBtnClicked() called");
    }

    public class SignupTask extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        private Context mContext;

        public SignupTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("회원가입 처리중...");
            progressDialog.setCancelable(true);
            progressDialog.setProgressStyle(R.style.Widget_AppCompat_ProgressBar_Horizontal);

            progressDialog.show();

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("name", signup_name.getText().toString().trim());
                jsonObject.accumulate("numId", signup_numId.getText().toString().trim());
                jsonObject.accumulate("dept", signup_dept.getText().toString().trim());
                jsonObject.accumulate("pw", signup_pw.getText().toString());
                jsonObject.accumulate("email", signup_email.getText().toString());

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

            if (result.equals("test")) {     // Success
                Toast.makeText(getApplicationContext(), signup_name.getText().toString() + "님, 가입을 환영합니다.", Toast.LENGTH_SHORT).show();

                Intent resultIntent = new Intent(SignupActivity.this, LoginActivity.class);

                startActivity(resultIntent);

                finish();
            } else {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
