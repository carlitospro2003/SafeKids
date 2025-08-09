package com.example.safekids;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;

import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.safekids.network.ApiClient;
import com.example.safekids.network.ApiService;
import com.example.safekids.network.GuardianResponse;
import com.example.safekids.network.Verify2FARequest;
import com.example.safekids.storage.SessionManager;
import com.chaos.view.PinView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.SharedPreferences;


public class TwoFAActivity extends AppCompatActivity {

    private PinView pinView2FA;
    private Button button2FA, buttonResend;
    private TextView textView2FAError;

    private String temporaryToken;
    private ApiService apiService;
    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_two_faactivity);
        pinView2FA = findViewById(R.id.pinView2FA);
        button2FA = findViewById(R.id.button2FA);
        textView2FAError = findViewById(R.id.textView2FAError);
        textView2FAError.setVisibility(View.GONE);
        apiService = ApiClient.getApiService();
        sessionManager = new SessionManager(this);
        // Recibe el temporaryToken desde LoginActivity
        temporaryToken = getIntent().getStringExtra("temporaryToken");

        button2FA.setOnClickListener(v -> verify2FA());

    }

    private void verify2FA() {
        String code = pinView2FA.getText().toString().trim();

        if (code.length() != 6) {
            textView2FAError.setText("Please enter a 6-digit code.");
            textView2FAError.setVisibility(View.VISIBLE);
            return;
        }

        textView2FAError.setVisibility(View.GONE); // ocultar error al intentar

        Verify2FARequest request = new Verify2FARequest(temporaryToken, code);

        apiService.verify2FA(request).enqueue(new Callback<GuardianResponse>() {
            @Override
            public void onResponse(Call<GuardianResponse> call, Response<GuardianResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GuardianResponse res = response.body();
                    if (res.isSuccess()) {
                        Log.d("JWT_TOKEN", "Token: " + res.getToken());
                        Toast.makeText(TwoFAActivity.this, "Login exitoso", Toast.LENGTH_SHORT).show();

                        //Guardar toke y datos del usuario
                        sessionManager.saveToken(res.getToken());
                        sessionManager.saveGuardian(res.getData());
                        sessionManager.saveStudents(res.getStudents());
                        sessionManager.saveSchool(res.getSchool()); // 🔹 Guardar la escuela



                        // Ir a MainActivity
                        Intent intent = new Intent(TwoFAActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        textView2FAError.setText(res.getMessage());
                        textView2FAError.setVisibility(View.VISIBLE);
                    }
                } else {
                    textView2FAError.setText("Codigo invalido o token epirado");
                    textView2FAError.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<GuardianResponse> call, Throwable t) {
                textView2FAError.setText("Connection error: " + t.getMessage());
                textView2FAError.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText || v instanceof com.chaos.view.PinView) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }



}