package com.example.safekids;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.safekids.network.ApiClient;
import com.example.safekids.network.ApiService;
import com.example.safekids.network.GenericResponse;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText editTextEmailResetPass;
    private Button buttonResetPassword;
    private TextView textViewResetPassError;

    private ApiService apiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);

        editTextEmailResetPass = findViewById(R.id.editTextEmailResetPass);
        buttonResetPassword = findViewById(R.id.buttonResetPassord);
        textViewResetPassError = findViewById(R.id.textViewResetPassError);

        apiService = ApiClient.getApiService();

        buttonResetPassword.setOnClickListener(v -> {
            String email = editTextEmailResetPass.getText().toString().trim();

            if (email.isEmpty()) {
                showError("El email es obligatorio");
                return;
            }

            textViewResetPassError.setVisibility(View.GONE);

            apiService.resetPassword(email)
                    .enqueue(new Callback<GenericResponse>() {
                        @Override
                        public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                GenericResponse body = response.body();
                                if (body.isSuccess()) {
                                    Toast.makeText(ForgotPasswordActivity.this, body.getMessage(), Toast.LENGTH_SHORT).show();

                                    // Ir a VerifyCodeActivity
                                    Intent intent = new Intent(ForgotPasswordActivity.this, VerifyCodeActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    showError(body.getMessage());

                                }
                            } else {
                                showError("Credenciales Incorrectas");

                            }
                        }

                        @Override
                        public void onFailure(Call<GenericResponse> call, Throwable t) {
                            showError("Error de red: " + t.getMessage());

                        }
                    });
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void showError(String message) {
        textViewResetPassError.setText(message);
        textViewResetPassError.setVisibility(View.VISIBLE);
    }
}