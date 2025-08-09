package com.example.safekids;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.chaos.view.PinView;
import com.example.safekids.network.ApiClient;
import com.example.safekids.network.ApiService;
import com.example.safekids.network.VerifyCodeResponse;
import com.example.safekids.network.Resend2FAResponse;
import com.example.safekids.network.GuardianData;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyCodeActivity extends AppCompatActivity {

    private PinView pinViewVerifyCode;
    private Button buttonVerifyCode, buttonResendCodeVerifyCode;
    private TextView tvViewVerifyErro;

    private ApiService apiService;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verify_code);

        pinViewVerifyCode = findViewById(R.id.pinViewVerifyCode);
        buttonVerifyCode = findViewById(R.id.buttonVerifyCode);
        buttonResendCodeVerifyCode = findViewById(R.id.buttonResendCodeVerifyCode);
        tvViewVerifyErro = findViewById(R.id.tvViewVerifyError);

        apiService = ApiClient.getApiService();
        SharedPreferences prefs = getSharedPreferences("SafeKidsPrefs", Context.MODE_PRIVATE);
        email = prefs.getString("reset_email", null);

        buttonVerifyCode.setOnClickListener(v -> {
            String code = pinViewVerifyCode.getText().toString().trim();

            if (code.length() != 6) {
                showError("Debe ingresar los 6 dígitos");
                return;
            }

            tvViewVerifyErro.setVisibility(View.GONE);

            apiService.verifyCode(code)
                    .enqueue(new Callback<VerifyCodeResponse>() {
                        @Override
                        public void onResponse(Call<VerifyCodeResponse> call, Response<VerifyCodeResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                VerifyCodeResponse body = response.body();
                                if (body.isSuccess()) {
                                    // Guardar resetToken
                                    prefs.edit().putString("reset_token", body.getResetToken()).apply();

                                    Toast.makeText(VerifyCodeActivity.this, body.getMessage(), Toast.LENGTH_SHORT).show();

                                    // Ir a ResetPasswordActivity
                                    Intent intent = new Intent(VerifyCodeActivity.this, ResetPasswordActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    showError(body.getMessage());
                                }
                            } else {
                                showError("Código inválido");
                            }
                        }

                        @Override
                        public void onFailure(Call<VerifyCodeResponse> call, Throwable t) {
                            showError("Error de red: " + t.getMessage());
                        }
                    });
        });

        // Reenviar código 2FA
        buttonResendCodeVerifyCode.setOnClickListener(v -> {
            if (email == null) {
                showError("No se encontró el email, regrese a la pantalla anterior");
                return;
            }

            apiService.resend2FA(email)
                    .enqueue(new Callback<Resend2FAResponse>() {
                        @Override
                        public void onResponse(Call<Resend2FAResponse> call, Response<Resend2FAResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                Resend2FAResponse body = response.body();
                                Toast.makeText(VerifyCodeActivity.this, body.getMessage(), Toast.LENGTH_SHORT).show();

                                if (body.isSuccess()) {
                                    // Mensaje adicional confirmando envío
                                    Toast.makeText(VerifyCodeActivity.this, " Código reenviado correctamente", Toast.LENGTH_SHORT).show();

                                    if (body.getData() != null) {
                                        GuardianData tutor = body.getData();
                                        prefs.edit()
                                                .putString("guardian_email", tutor.getEmail())
                                                .putString("guardian_name", tutor.getFirstName() + " " + tutor.getLastName())
                                                .apply();
                                    }
                                }
                            } else {
                                showError("No se pudo reenviar el código");
                            }
                        }

                        @Override
                        public void onFailure(Call<Resend2FAResponse> call, Throwable t) {
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
        tvViewVerifyErro.setText(message);
        tvViewVerifyErro.setVisibility(View.VISIBLE);
    }
}