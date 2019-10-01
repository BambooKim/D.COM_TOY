package com.bambookim.dcom;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class SendPushActivity extends AppCompatActivity {

    private static final String TAG = "SendPushActivity";

    EditText title, body, http_url;
    Button sendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_push);

        title = (EditText) findViewById(R.id.pushTitle);
        body = (EditText) findViewById(R.id.pushBody);
        http_url = (EditText) findViewById(R.id.push_ip);
        sendBtn = (Button) findViewById(R.id.send_push);
    }

    public void onsendBtnClicked(View v) {
        boolean titleIsEmpty = (title.getText().toString().trim().length() == 0);
        boolean bodyIsEmpty = (title.getText().toString().trim().length() == 0);

        if (titleIsEmpty || bodyIsEmpty) {
            Toast.makeText(getApplicationContext(), "제목 또는 내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
        } else {
            new PushTask(SendPushActivity.this).execute("http://" + http_url.getText() + "/api/sendPush");
            Log.d("http_url", "http://" + http_url.getText() + "/api/sendPush");
        }
    }

    public class PushTask extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        private Context mContext;

        public PushTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("푸시 전송 처리 중...");
            progressDialog.setCancelable(true);
            progressDialog.setProgressStyle(R.style.Widget_AppCompat_ProgressBar_Horizontal);

            progressDialog.show();

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("pushTitle", title.getText().toString());
                jsonObject.accumulate("pushBody", body.getText().toString());
                jsonObject.accumulate("senderToken", SaveSharedPreference.getToken(getApplicationContext()));

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
                Toast.makeText(getApplicationContext(), "메시지가 전송되었습니다.", Toast.LENGTH_LONG).show();

                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        }
    }
}
