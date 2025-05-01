package com.abdallamusa.ask_a_muslim;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class signUp extends AppCompatActivity {

    private EditText firstNameEt, lastNameEt, emailEt, passwordEt;
    private Button createAccountButton;
    private TextView signInTx;
    private ImageView backArrowBtn;
    private ApiService apiService;
    public String msg_signUpID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        firstNameEt         = findViewById(R.id.firstName_edit_view);
        lastNameEt          = findViewById(R.id.lastName_edit_view);
        emailEt             = findViewById(R.id.E_mail_edit_view);
        passwordEt          = findViewById(R.id.Password_edit_view);
        createAccountButton = findViewById(R.id.Create_account_button);
        signInTx            = findViewById(R.id.signIn_tx);
        backArrowBtn        = findViewById(R.id.arrow_left);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ask-a-muslim.runasp.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        signInTx.setOnClickListener(v ->
                startActivity(new Intent(signUp.this, login.class))
        );
        backArrowBtn.setOnClickListener(v ->
                startActivity(new Intent(signUp.this, MainActivity.class))
        );

        createAccountButton.setOnClickListener(v -> {
            String fn = firstNameEt.getText().toString().trim();
            String ln = lastNameEt.getText().toString().trim();
            String em = emailEt.getText().toString().trim();
            String pw = passwordEt.getText().toString().trim();

            if (fn.isEmpty() || ln.isEmpty() || em.isEmpty() || pw.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            // fill in phone & profilePicture with defaults
            Call<RegisterResponse> call = apiService.registerUser(
                    fn, ln, em, pw,
                    "0",          // PhoneNumber
                    "Student",    // Role
                    ""            // ProfilePicture
            );

            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(@NonNull Call<RegisterResponse> call,
                                       @NonNull Response<RegisterResponse> resp) {
                    if (resp.isSuccessful() && resp.body() != null ) {

                         msg_signUpID = resp.body().getMessage();
                        Toast.makeText(signUp.this,
                                "Registration successful!\n"+msg_signUpID,
                                Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(signUp.this, Courses.class));
                        finish();
                    }
                    else {
                        String msg = (resp.body()!=null)
                                ? resp.body().getMessage()
                                : "Code " + resp.code();
                        Toast.makeText(signUp.this,
                                "Registration failed: " + msg,
                                Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(@NonNull Call<RegisterResponse> call,
                                      @NonNull Throwable t) {
                    Toast.makeText(signUp.this,
                            "Network error: " + t.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
