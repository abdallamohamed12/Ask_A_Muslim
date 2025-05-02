package com.abdallamusa.ask_a_muslim;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class login extends AppCompatActivity {

    private EditText emailOrUsernameEt, passwordEt;
    private AuthApi   authApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        emailOrUsernameEt = findViewById(R.id.Email_or_Username_edit_view);
        passwordEt        = findViewById(R.id.passwordLogin_edit_view);
        Button signInBtn = findViewById(R.id.signInBtn);
        ImageView backArrowBtn = findViewById(R.id.back_arrow_icon);

        // 1) Build Retrofit + the AuthApi
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ask-a-muslim.runasp.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        authApi = retrofit.create(AuthApi.class);

        // 2) “Back” arrow
        backArrowBtn.setOnClickListener(v ->
                startActivity(new Intent(login.this, MainActivity.class))
        );

        // 3) Sign-in button
        signInBtn.setOnClickListener(v -> {
            String email    = emailOrUsernameEt.getText().toString().trim();
            String password = passwordEt       .getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Both email & password are required",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // 4) Fire the network call
            authApi.login(new LoginRequest(email, password))
                    .enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<LoginResponse> call,
                                               @NonNull Response<LoginResponse> resp) {
                            if (resp.isSuccessful() && resp.body() != null) {
                                LoginResponse body = resp.body();

                                    Toast.makeText(login.this,
                                            "Welcome back!", Toast.LENGTH_SHORT).show();

                                    // e.g. save body.getToken() in SharedPreferences…
                                String token = body.getToken();

                            // split into header, payload, signature
                                String[] parts = token.split("\\.");
                                if (parts.length == 3) {
                                    String payload = parts[1];
                                    // decode URL-safe base64
                                    byte[] decoded = android.util.Base64.decode(payload, android.util.Base64.URL_SAFE);
                                    String json = new String(decoded, StandardCharsets.UTF_8);

                                    JSONObject obj = null;
                                    try {
                                        obj = new JSONObject(json);
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                    String userId;
                                    try {
                                        userId = obj.getString("sub");
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                    // now you have their GUID userId
                                    SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                                    prefs.edit()
                                            .putString("auth_token", token)
                                            .putString("user_id", userId)
                                            .apply();
                                    SessionManager.get().saveSession(userId, token);
                                }

                                startActivity(new Intent(login.this, Courses.class));
                                    finish();
                                }
                            else {
                                    Toast.makeText(login.this,
                                            "Login failed: " +"Code: "+resp.code()+"\n"+ resp.body(),
                                            Toast.LENGTH_LONG).show();
                                }
                            }

                        @Override
                        public void onFailure(@NonNull Call<LoginResponse> call,
                                              @NonNull Throwable t) {
                            Toast.makeText(login.this,
                                    "Network error: " + t.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
