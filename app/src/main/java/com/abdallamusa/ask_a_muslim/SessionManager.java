package com.abdallamusa.ask_a_muslim;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREFS       = "MyAppPrefs";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_TOKEN   = "auth_token";

    private static SessionManager instance;
    private final SharedPreferences prefs;

    private SessionManager(Context ctx) {
        prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public static void init(Context ctx) {
        if (instance == null) instance = new SessionManager(ctx.getApplicationContext());
    }
    public static SessionManager get() {
        if (instance == null) throw new IllegalStateException("SessionManager.init() not called");
        return instance;
    }

    public void saveSession(String userId, String token) {
        prefs.edit()
                .putString(KEY_USER_ID, userId)
                .putString(KEY_TOKEN, token)
                .apply();
    }

    public String getUserId() {
        return prefs.getString(KEY_USER_ID, "");
    }

    public String getToken() {
        return prefs.getString(KEY_TOKEN, "");
    }

    public void clear() {
        prefs.edit().clear().apply();
    }
}
