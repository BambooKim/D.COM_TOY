package com.bambookim.dcom;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference {
    static final String PREF_USER_NAME = "username";
    static final String PREF_TOKEN = "token";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    // 계정 정보 및 토큰 저장
    public static void setPreferences(int id, Context ctx, String s) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();

        switch (id) {
            case 1:
                editor.putString(PREF_USER_NAME, s);
                break;
            case 2:
                editor.putString(PREF_TOKEN, s);
                break;
        }

        editor.commit();
    }

    // 저장된 정보 가져오기
    public static String getUserName(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }

    // 저장된 토큰 가져오기
    public static String getToken(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_TOKEN, "");
    }

    // 로그아웃
    public static void clearPreferences(Context ctx) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear();
        editor.commit();
    }
}
