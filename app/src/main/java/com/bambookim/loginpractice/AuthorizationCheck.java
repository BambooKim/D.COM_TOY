package com.bambookim.loginpractice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class AuthorizationCheck extends Service {
    private static final String TAG = "AuthorizationCheck";
    boolean no_session;

    public AuthorizationCheck() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() called");

        processCommand(intent);

        // return super.onStartCommand(intent, flags, startId);
        return Service.START_NOT_STICKY;
    }

    public void processCommand(Intent intent) {
        Log.d(TAG, "processCommand() 호출됨");

        no_session = SaveSharedPreference.getUserName(getApplicationContext()).length() == 0;

        if (no_session) {
            // Call Login Activity
            Intent callLoginIntent = new Intent(getApplicationContext(), LoginActivity.class);
            callLoginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(callLoginIntent);
        } else {
            // Call Home Activity
            Intent callHomeIntent = new Intent(getApplicationContext(), HomeActivity.class);
            callHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(callHomeIntent);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
