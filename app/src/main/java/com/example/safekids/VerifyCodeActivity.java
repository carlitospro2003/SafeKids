package com.example.safekids;

import android.content.Intent;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyCodeActivity extends AppCompatActivity {

    private PinView pinViewVerifyCode;
    private Button buttonVerifyCode;
    private TextView tvViewVerifyErro;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verify_code);

        pinViewVerifyCode = findViewById(R.id.pinViewVerifyCode);
        buttonVerifyCode = findViewById(R.id.buttonVerifyCode);
        tvViewVerifyErro = findViewById(R.id.tvViewVerifyError);

        apiService = ApiClient.getApiService();

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
                                    Toast.makeText(VerifyCodeActivity.this, body.getMessage(), Toast.LENGTH_SHORT).show();

                                    // Pasar el resetToken a ResetPasswordActivity
                                    Intent intent = new Intent(VerifyCodeActivity.this, ResetPasswordActivity.class);
                                    intent.putExtra("resetToken", body.getResetToken());
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