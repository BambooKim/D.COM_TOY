package com.bambookim.dcom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    protected static int next_clicked = 0;
    protected static boolean isEndFragment = false;

    protected String info_id, info_pw, name, numId, dept, userId, pw, pwchk, email;

    EditText signup_info_id, signup_info_pw, signup_userid, signup_email,
            signup_http_url, signup_pw, signup_pwchk;
    Button send_signup, signup_next;
    LinearLayout layout;

    /**
     * Fragment Class In SignupActivity
     */
    SignupWelcome welComeFragment = new SignupWelcome();
    SignupInfo21 info21Fragment = new SignupInfo21();
    SignupUserInfo userInfoFragment = new SignupUserInfo();
    SignupPassword passwordFragment = new SignupPassword();
    SignupEmail emailFragment = new SignupEmail();

    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        send_signup = (Button) findViewById(R.id.send_signup);
        signup_next = (Button) findViewById(R.id.signup_next);

        send_signup.setVisibility(View.GONE);

        transaction = fragmentManager.beginTransaction();
        transaction
                .replace(R.id.signup_frame, welComeFragment)
                .commit();

        // signup_info_id = (EditText) findViewById(R.id.signup_info_id);
        // signup_info_pw = (EditText) findViewById(R.id.signup_info_pw);



        signup_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (next_clicked) {
                    case 0:
                        InputMethodManager inputMethodManager
                                = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager
                                .toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

                        transaction = fragmentManager.beginTransaction();
                        transaction
                                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                        R.anim.enter_from_right, R.anim.exit_to_left)
                                .replace(R.id.signup_frame, info21Fragment)
                                .addToBackStack(null)
                                .commit();

                        next_clicked++;

                        break;
                    case 1:
                        signup_info_id = (EditText) findViewById(R.id.signup_info_id);
                        signup_info_pw = (EditText) findViewById(R.id.signup_info_pw);

                        info_id = signup_info_id.getText().toString().trim();
                        info_pw = signup_info_pw.getText().toString().trim();

                        if (info_id.equals("")) {
                            showDialogMessage("인포21 아이디를 입력해 주세요.");
                        } else if (info_pw.equals("")) {
                            showDialogMessage("인포21 비밀번호를 입력해 주세요.");
                        } else {

                            new Info21Task(SignupActivity.this)
                                    .execute("http://192.168.0.7:3000/api/info21");
                        }

                        break;
                    case 2:
                        signup_userid = (EditText) findViewById(R.id.signup_userid);
                        userId = signup_userid.getText().toString().trim();

                        if (userId.equals("")) {
                            showDialogMessage("아이디를 입력해 주세요.");
                        } else if (!Pattern.matches("^[a-zA-Z]{1}[a-zA-Z0-9_]{5,15}$", userId)) {
                            showDialogMessage("올바르지 않은 아이디입니다.\n" +
                                    "아이디는 영문과 숫자로만 이루어진\n" +
                                    "5 ~ 15자로 이루어집니다.");
                        } else {
                            transaction = fragmentManager.beginTransaction();
                            transaction
                                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                            R.anim.enter_from_right, R.anim.exit_to_left)
                                    .replace(R.id.signup_frame, passwordFragment)
                                    .addToBackStack(null)
                                    .commit();

                            next_clicked++;
                        }
                        break;
                    case 3:
                        signup_pw = (EditText) findViewById(R.id.signup_pw);
                        signup_pwchk = (EditText) findViewById(R.id.signup_pwchk);
                        pw = signup_pw.getText().toString().trim();
                        pwchk = signup_pwchk.getText().toString().trim();

                        Pattern p = Pattern.compile("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[!@#$%^&*?,./\\\\<>|_-[+]=\\`~\\(\\)\\[\\]\\{\\}])[A-Za-z[0-9]!@#$%^&*?,./\\\\<>|_-[+]=\\`~\\(\\)\\[\\]\\{\\}]{8,20}$");
                        Matcher m = p.matcher(pw);

                        if (pw.equals("")) {
                            showDialogMessage("비밀번호를 입력해 주세요.");
                        } else if (!pw.equals(pwchk)) {
                            showDialogMessage("비밀번호를 확인해 주세요.");
                        } else if (!m.matches()) {
                            showDialogMessage("올바르지 않은 비밀번호입니다.\n" +
                                    "비밀번호는 특수문자가 포함된\n" +
                                    "8 ~ 20자로 이루어집니다.");
                        } else {
                            send_signup.setVisibility(View.VISIBLE);
                            signup_next.setVisibility(View.GONE);

                            transaction = fragmentManager.beginTransaction();
                            transaction
                                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                            R.anim.enter_from_right, R.anim.exit_to_left)
                                    .replace(R.id.signup_frame, emailFragment)
                                    .addToBackStack(null)
                                    .commit();

                            isEndFragment = true;
                            next_clicked++;
                        }
                        break;
                }
            }
        });
    }

    public void onSendSignupBtnClicked(View v) {
        signup_email = (EditText) findViewById(R.id.signup_email);
        signup_http_url = (EditText) findViewById(R.id.signup_http_url);

        email = signup_email.getText().toString().trim();

        if (email.equals("")) {
            showDialogMessage("이메일을 입력해 주세요.");
        } else if (!Pattern.matches("^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$", email)) {
            showDialogMessage("올바르지 않은 이메일입니다.");
        } else {


            new SignupTask(SignupActivity.this)
                    .execute("http://" + signup_http_url.getText() + "/api/signup");
            Log.d(TAG, "onSendSignupBtnClicked() called");
        }
    }

    public void showDialogMessage(String message) {
        new AlertDialog.Builder(SignupActivity.this)
                .setTitle("안내").setMessage(message)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setCancelable(false)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        next_clicked = 0;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (next_clicked != 0) {
            next_clicked--;
        }

        if (next_clicked == 3) {
            send_signup.setVisibility(View.GONE);
            signup_next.setVisibility(View.VISIBLE);
        }
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
                jsonObject.accumulate("name", name);
                jsonObject.accumulate("numId", numId);
                jsonObject.accumulate("dept", dept);
                jsonObject.accumulate("userId", userId);
                jsonObject.accumulate("pw", pw);
                jsonObject.accumulate("email", email);

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
                InputMethodManager inputMethodManager
                        = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager
                        .toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                Toast.makeText(SignupActivity.this, name + "님, 가입을 환영합니다.",
                        Toast.LENGTH_SHORT).show();

                Intent resultIntent
                        = new Intent(SignupActivity.this, LoginActivity.class);

                startActivity(resultIntent);

                finish();
            } else {
                Toast.makeText(SignupActivity.this, result, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class Info21Task extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        private Context mContext;

        public Info21Task(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("Info21 재학생 인증 처리중...");
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(R.style.Widget_AppCompat_ProgressBar_Horizontal);

            progressDialog.show();

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("info_id", info_id);
                jsonObject.accumulate("info_pw", info_pw);

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

            if (result.equals("fail")) {
                showDialogMessage("재학생 인증 실패...\n다시 시도해 주십시오.");
            } else {
                String[] array = result.split(", ");
                name = array[0];
                numId = array[1];
                dept = array[2];

                new AlertDialog.Builder(SignupActivity.this)
                        .setTitle("본인인증 성공")
                        .setMessage("이름 : " + name + "\n" +
                                "학번 : " + numId + "\n" +
                                "학과 : " + dept)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                transaction = fragmentManager.beginTransaction();
                                transaction
                                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                                R.anim.enter_from_right, R.anim.exit_to_left)
                                        .replace(R.id.signup_frame, userInfoFragment)
                                        .addToBackStack(null)
                                        .commit();

                                next_clicked++;
                            }
                        })
                        .setCancelable(false)
                        .show();
            }
        }
    }
}
