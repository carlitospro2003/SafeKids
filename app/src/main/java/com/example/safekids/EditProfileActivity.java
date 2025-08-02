package com.example.safekids;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.safekids.network.ApiService;
import com.example.safekids.network.ApiClient;
import com.example.safekids.network.ApiService;
import com.example.safekids.network.ChangePasswordRequest;
import com.example.safekids.network.GuardianResponse;
import com.example.safekids.storage.SessionManager;


import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EditProfileActivity extends AppCompatActivity {

    private EditText etNewPassword, etConfirmPassword;
    private Button btnConfirm;
    private SessionManager sessionManager;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);

        etNewPassword = findViewById(R.id.etNewPasswordProfile);
        etConfirmPassword = findViewById(R.id.etConfirmPasswordProfile);
        btnConfirm = findViewById(R.id.buttomConfirmEditProfile);

        sessionManager = new SessionManager(this);
        apiService = ApiClient.getApiService();

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void updatePassword() {
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        boolean hasError = false;

        if (newPassword.isEmpty()) {
            etNewPassword.setError("Este campo es requerido");
            hasError = true;
        } else {
            etNewPassword.setError(null);
        }

        if (confirmPassword.isEmpty()) {
            etConfirmPassword.setError("Este campo es requerido");
            hasError = true;
        } else {
            etConfirmPassword.setError(null);
        }

        if (!hasError && !newPassword.equals(confirmPassword)) {
            etConfirmPassword.setError("Las contraseñas no coinciden");
            hasError = true;
        }

        if (hasError) {
            return;
        }

        String token = sessionManager.getToken();
        if (token == null) {
            Toast.makeText(this, "Sin sesión", Toast.LENGTH_SHORT).show();
            return;
        }

        // Mostrar el ProgressBar
        LoadingCustom loadingCustom = new LoadingCustom(this);
        loadingCustom.startLoadingCustom();

        ChangePasswordRequest request = new ChangePasswordRequest(newPassword, confirmPassword);

        apiService.changePassword("Bearer " + token, request).enqueue(new Callback<GuardianResponse>() {
            @Override
            public void onResponse(Call<GuardianResponse> call, Response<GuardianResponse> response) {
                loadingCustom.dismissLoading();
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(EditProfileActivity.this, "Contraseña actualizada correctamente", Toast.LENGTH_LONG).show();
                    finish(); // go back to previous screen
                } else {
                    Toast.makeText(EditProfileActivity.this, "No se pudo actualizar la contraseña", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GuardianResponse> call, Throwable t) {
                loadingCustom.dismissLoading();
                Toast.makeText(EditProfileActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}