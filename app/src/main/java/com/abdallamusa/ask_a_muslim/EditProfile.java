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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public class EditProfile extends AppCompatActivity {

    // Views
    private ImageView profileEditIcon;
    private EditText firstNameEt, lastNameEt, emailEt, passwordOldEt, passwordNewEt;
    private Button confirmEditBtn, deleteAccountBtn, logOutBtn;

    // SharedPrefs
    private SharedPreferences prefs;

    // Retrofit API
    private AuthApi authApi;
    private String bearerToken, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);

        // 1) bind views
        profileEditIcon = findViewById(R.id.profileEditIcon);
        firstNameEt = findViewById(R.id.editFirstName_EditView);
        lastNameEt = findViewById(R.id.editLastName_EditView);
        emailEt = findViewById(R.id.editEmail_editView);
        passwordOldEt = findViewById(R.id.editOldPassword_editView);
        passwordNewEt = findViewById(R.id.editNewPassword_editView);

        confirmEditBtn = findViewById(R.id.ConfirmEditBtn);
        deleteAccountBtn = findViewById(R.id.deleteAccountBtn);
        logOutBtn = findViewById(R.id.logOutBtn);

        // 2) prefs + session
        prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        bearerToken = "Bearer " + prefs.getString("auth_token", "");
        userId = SessionManager.get().getUserId();

        // 3) Retrofit setup
        Retrofit rf = new Retrofit.Builder()
                .baseUrl("https://ask-a-muslim.runasp.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        authApi = rf.create(AuthApi.class);

        // 4) Log out
        logOutBtn.setOnClickListener(v -> {
            authApi.logout(bearerToken)
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> resp) {
                            if (resp.isSuccessful()) {
                                // 1) clear everything
                                prefs.edit().clear().apply();

                                // 2) go back to login, clearing the back‐stack
                                startActivity(new Intent(EditProfile.this, login.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                finish();
                            } else {
                                Toast.makeText(EditProfile.this,
                                        "Logout failed: " + resp.code(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                            Toast.makeText(EditProfile.this,
                                    "Network error: " + t.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });  // <-- don’t forget this semicolon!
        });

        // 5) Confirm all updates
        confirmEditBtn.setOnClickListener(v -> {
            String fn = firstNameEt.getText().toString().trim();
            String ln = lastNameEt.getText().toString().trim();
            String em = emailEt.getText().toString().trim();
            String oldPw = passwordOldEt.getText().toString().trim();
            String newPw = passwordNewEt.getText().toString().trim();

            authApi.updateEmail(bearerToken,
                            new AuthApi.UpdateEmailRequest(userId, em))
                    .enqueue(new Callback<Void>() {
                        @Override public void onResponse(Call<Void> call,
                                                         Response<Void> resp) {
                            if (!resp.isSuccessful()) {
                                Toast.makeText(EditProfile.this,
                                        "Email update failed: " + resp.code(),
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                            // second: update password
                            authApi.updatePassword(bearerToken,
                                            new AuthApi.UpdatePasswordRequest(userId, oldPw, newPw))
                                    .enqueue(new Callback<Void>() {
                                        @Override public void onResponse(Call<Void> call,
                                                                         Response<Void> resp2) {
                                            if (!resp2.isSuccessful()) {
                                                Toast.makeText(EditProfile.this,
                                                        "Password update failed: " + resp2.code(),
                                                        Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            // third: update name
                                            authApi.updateName(bearerToken,
                                                            new AuthApi.UpdateNameRequest(userId, fn, ln))
                                                    .enqueue(new Callback<Void>() {
                                                        @Override public void onResponse(Call<Void> call,
                                                                                         Response<Void> resp3) {
                                                            if (!resp3.isSuccessful()) {
                                                                Toast.makeText(EditProfile.this,
                                                                        "Name update failed: " + resp3.code(),
                                                                        Toast.LENGTH_SHORT).show();
                                                                return;
                                                            }
                                                            // all three succeeded!
                                                            Toast.makeText(EditProfile.this,
                                                                    "Profile updated!", Toast.LENGTH_SHORT).show();
                                                            startActivity(new Intent(EditProfile.this,
                                                                    Profile.class));
                                                            finish();
                                                        }
                                                        @Override public void onFailure(Call<Void> call,
                                                                                        Throwable t) {
                                                            Toast.makeText(EditProfile.this,
                                                                    "Name update error: " + t.getMessage(),
                                                                    Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                        @Override public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                                            Toast.makeText(EditProfile.this,
                                                    "Password update error: " + t.getMessage(),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        @Override public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(EditProfile.this,
                                    "Email update error: " + t.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });


        // 6) Delete account
        deleteAccountBtn.setOnClickListener(v -> {
            authApi.deleteUser(userId)
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> resp) {
                            if (resp.isSuccessful()) {
                                // 1) clear everything
                                prefs.edit().clear().apply();

                                // 2) go back to login, clearing the back‐stack
                                startActivity(new Intent(EditProfile.this, signUp.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                finish();
                            } else {
                                Toast.makeText(EditProfile.this,
                                        "Delete failed: " + resp.code(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                            Toast.makeText(EditProfile.this,
                                    "Network error: " + t.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });  // <-- don’t forget this semicolon!
        });

    /**
     * Simple generic callback that toasts on success or failure.
     */
        }
    }

