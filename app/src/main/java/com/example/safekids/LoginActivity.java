package com.example.safekids;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.InputType;
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
import com.example.safekids.network.LoginRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.util.Log;


public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmailLogin, editTextPasswordLogin;
    private Button buttonLogin;
    private TextView textViewLoginError;
    private ApiService apiService;
    private TextView textViewForgotPassword;

    private LoadingCustom loadingCustom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        editTextEmailLogin = findViewById(R.id.editTextEmailLogin);
        editTextPasswordLogin = findViewById(R.id.editTextPasswordLogin);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewLoginError = findViewById(R.id.textViewLoginError);
        textViewLoginError.setVisibility(View.GONE);

        apiService = ApiClient.getApiService();
        loadingCustom = new LoadingCustom(this);

        // visibilidad de la contraseña
        final boolean[] isPasswordVisible = {false};

        editTextPasswordLogin.setOnTouchListener((v, event) -> {
            final int DRAWABLE_END = 2; // índice del drawableEnd

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (editTextPasswordLogin.getRight()
                        - editTextPasswordLogin.getCompoundDrawables()[DRAWABLE_END].getBounds().width())) {

                    if (isPasswordVisible[0]) {
                        // Ocultar contraseña
                        editTextPasswordLogin.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        editTextPasswordLogin.setCompoundDrawablesWithIntrinsicBounds(
                                null, null,
                                getDrawable(R.drawable.closedeye), // 👁 cerrado
                                null
                        );
                    } else {
                        // Mostrar contraseña
                        editTextPasswordLogin.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        editTextPasswordLogin.setCompoundDrawablesWithIntrinsicBounds(
                                null, null,
                                getDrawable(R.drawable.eye), // 👁 abierto
                                null
                        );
                    }

                    // Mueve el cursor al final
                    editTextPasswordLogin.setSelection(editTextPasswordLogin.getText().length());
                    isPasswordVisible[0] = !isPasswordVisible[0];
                    return true;
                }
            }
            return false;
        });

        buttonLogin.setOnClickListener(v -> loginUser());

        textViewForgotPassword = findViewById(R.id.textViewForgotPassword);

        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });


    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
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





    private void loginUser() {
        String email = editTextEmailLogin.getText().toString().trim();
        String password = editTextPasswordLogin.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            textViewLoginError.setText("Los Campos son requeridos.");
            textViewLoginError.setVisibility(View.VISIBLE);
            return;
        }
        LoginRequest request = new LoginRequest(email, password);

        loadingCustom.startLoadingCustom();

        apiService.loginGuardian(request).enqueue(new Callback<GuardianResponse>() {
            @Override
            public void onResponse(Call<GuardianResponse> call, Response<GuardianResponse> response) {
                loadingCustom.dismissLoading();

                if (response.isSuccessful() && response.body() != null) {
                    GuardianResponse res = response.body();

                    if (res.isSuccess()) {
                        String temporaryToken = res.getTemporaryToken();

                        Log.d("TEMP_TOKEN", "Temporary Token: " + temporaryToken);


                        // Ocultar errores si los hubiera
                        textViewLoginError.setVisibility(View.GONE);

                        // Enviar a TwoFAActivity con el token
                        Intent intent = new Intent(LoginActivity.this, TwoFAActivity.class);
                        intent.putExtra("temporaryToken", temporaryToken);
                        startActivity(intent);
                    } else {
                        textViewLoginError.setText(res.getMessage());
                        textViewLoginError.setVisibility(View.VISIBLE);
                    }
                } else {
                    textViewLoginError.setText("Credenciales Incorrectas.");
                    textViewLoginError.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<GuardianResponse> call, Throwable t) {

                loadingCustom.dismissLoading();
                textViewLoginError.setText("Connection error: " + t.getMessage());
                textViewLoginError.setVisibility(View.VISIBLE);

            }
        });
    }

}