package com.example.safekids;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
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
import com.example.safekids.network.GenericResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText editTextResetPass, editTextResetPassword;
    private TextView textViewResetPassError;
    private Button buttonResetPassord2;

    private ApiService apiService;
    private String resetToken;

    private boolean isPasswordVisible1 = false;
    private boolean isPasswordVisible2 = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);

        editTextResetPass = findViewById(R.id.editTextResetPass);
        editTextResetPassword = findViewById(R.id.editTextResetPassword);
        textViewResetPassError = findViewById(R.id.textViewResetPassError);
        buttonResetPassord2 = findViewById(R.id.buttonResetPassord2);

        apiService = ApiClient.getApiService();
        resetToken = getIntent().getStringExtra("resetToken");


        buttonResetPassord2.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View v) {
                Intent intent = new Intent(ResetPasswordActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        editTextResetPass.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (editTextResetPass.getRight() - editTextResetPass.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    togglePasswordVisibility(editTextResetPass, 1);
                    return true;
                }
            }
            return false;
        });

        editTextResetPassword.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (editTextResetPassword.getRight() - editTextResetPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    togglePasswordVisibility(editTextResetPassword, 2);
                    return true;
                }
            }
            return false;
        });


        buttonResetPassord2.setOnClickListener(v -> {
            String password = editTextResetPass.getText().toString().trim();
            String confirmPassword = editTextResetPassword.getText().toString().trim();

            if (password.isEmpty() || confirmPassword.isEmpty()) {
                showError("Todos los campos son obligatorios");
                return;
            }

            if (!password.equals(confirmPassword)) {
                showError("Las contraseñas no coinciden");
                return;
            }

            textViewResetPassError.setVisibility(View.GONE);

            apiService.changePassword(resetToken, password, confirmPassword)
                    .enqueue(new Callback<GenericResponse>() {
                        @Override
                        public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                GenericResponse body = response.body();
                                if (body.isSuccess()) {
                                    Toast.makeText(ResetPasswordActivity.this, "Contraseña actualizada exitosamente", Toast.LENGTH_SHORT).show();
                                    // Ir al login
                                    Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    showError(body.getMessage());
                                }
                            } else {
                                showError("Error al cambiar la contraseña");
                            }
                        }

                        @Override
                        public void onFailure(Call<GenericResponse> call, Throwable t) {
                            showError("Error de red: " + t.getMessage());
                        }
                    });
        });

    }
    private void togglePasswordVisibility(EditText editText, int field) {
        boolean isVisible = (field == 1) ? isPasswordVisible1 : isPasswordVisible2;
        if (isVisible) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
        editText.setSelection(editText.getText().length());
        if (field == 1) {
            isPasswordVisible1 = !isPasswordVisible1;
        } else {
            isPasswordVisible2 = !isPasswordVisible2;
        }
    }

    private void showError(String message) {
        textViewResetPassError.setText(message);
        textViewResetPassError.setVisibility(View.VISIBLE);
    }

    /*private void setupPasswordVisibilityToggle(EditText editText) {
        final boolean[] isPasswordVisible = {false};

        editText.setOnTouchListener((v, event) -> {
            final int DRAWABLE_END = 2;

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (editText.getRight()
                        - editText.getCompoundDrawables()[DRAWABLE_END].getBounds().width())) {

                    if (isPasswordVisible[0]) {
                        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        editText.setCompoundDrawablesWithIntrinsicBounds(
                                null, null,
                                getDrawable(R.drawable.closedeye),
                                null
                        );
                    } else {
                        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        editText.setCompoundDrawablesWithIntrinsicBounds(
                                null, null,
                                getDrawable(R.drawable.eye),
                                null
                        );
                    }

                    // Coloca el cursor al final del texto
                    editText.setSelection(editText.getText().length());
                    isPasswordVisible[0] = !isPasswordVisible[0];
                    return true;
                }
            }
            return false;
        });

    }*/
}