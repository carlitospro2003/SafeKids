package com.example.safekids;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.safekids.models.Family;
import com.example.safekids.network.ApiClient;
import com.example.safekids.network.ApiService;
import com.example.safekids.network.UpdateFamilyResponse;
import com.example.safekids.storage.SessionManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditFamilyActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName, etPhone, etRelationship;
    private Button btnSave;
    private Family family;
    private ApiService apiService;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_family);

        etFirstName = findViewById(R.id.editTextEditNameFamily);
        etLastName = findViewById(R.id.editTextEditLastNameFamily);
        etPhone = findViewById(R.id.editTextEditPhoneFamily);
        etRelationship = findViewById(R.id.editTextEditRelationFamily);
        btnSave = findViewById(R.id.buttomEditFamily);

        apiService = ApiClient.getApiService();
        sessionManager = new SessionManager(this);

        // Recibir datos
        family = (Family) getIntent().getSerializableExtra("family");
        if (family != null) {
            etFirstName.setText(family.getFirstName());
            etLastName.setText(family.getLastName());
            etPhone.setText(family.getPhone());
            etRelationship.setText(family.getRelationship());
        }

        btnSave.setOnClickListener(v -> updateFamily());

    }
    private void updateFamily() {
        String token = "Bearer " + sessionManager.getToken();

        Family updatedFamily = new Family(
                etFirstName.getText().toString().trim(),
                etLastName.getText().toString().trim(),
                etPhone.getText().toString().trim(),
                etRelationship.getText().toString().trim(),
                family.getPhoto()

        );
        apiService.updateFamily(token, family.getId(), updatedFamily)
                .enqueue(new Callback<UpdateFamilyResponse>() {
                    @Override
                    public void onResponse(Call<UpdateFamilyResponse> call, Response<UpdateFamilyResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {


                            // 🔹 Mostrar tu dialogo de éxito
                            SuccesDialog dialog = new SuccesDialog();
                            dialog.show(EditFamilyActivity.this, "Familiar actualizado correctamente");

                            // 🔹 Cerrar la activity después de 2.5 segundos
                            new android.os.Handler().postDelayed(() -> {
                                dialog.dismiss();
                                finish();
                            }, 2500);
                        } else {
                            Toast.makeText(EditFamilyActivity.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateFamilyResponse> call, Throwable t) {
                        Toast.makeText(EditFamilyActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}