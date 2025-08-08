    package com.example.safekids;

    import android.Manifest;
    import android.content.Intent;
    import android.content.pm.PackageManager;
    import android.graphics.Bitmap;
    import android.graphics.ImageDecoder;
    import android.net.Uri;
    import android.os.Build;
    import android.os.Bundle;
    import android.provider.MediaStore;
    import android.view.View;
    import android.widget.*;
    import android.widget.Button;
    import android.widget.Toast;

    import androidx.activity.EdgeToEdge;
    import androidx.activity.result.ActivityResultLauncher;
    import androidx.activity.result.contract.ActivityResultContracts;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.core.app.ActivityCompat;
    import androidx.core.content.ContextCompat;
    import androidx.core.graphics.Insets;
    import androidx.core.view.ViewCompat;
    import androidx.core.view.WindowInsetsCompat;

    import com.example.safekids.models.Children;
    import com.example.safekids.network.*;
    import com.example.safekids.storage.SessionManager;
    import com.example.safekids.utils.FileUtils;
    import com.google.mlkit.vision.common.InputImage;
    import com.google.mlkit.vision.face.Face;
    import com.google.mlkit.vision.face.FaceDetection;
    import com.google.mlkit.vision.face.FaceDetector;
    import com.google.mlkit.vision.face.FaceDetectorOptions;

    import java.io.File;
    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.List;
    import okhttp3.*;

    import com.example.safekids.fregments.TutorsFragment;

    import de.hdodenhof.circleimageview.CircleImageView;

    public class AddFamilyActivity extends AppCompatActivity {

        private static final int REQUEST_CODE_PERMISSION = 100;

        private CircleImageView photoNewFamily;
        private Button buttomAddNewFamily;
        private EditText edtNameFamily, edtLastNameFamily, edtPhoneFamily, edtRelationFamily;
        private TextView tvErrorPhotoFamily, tvErrorNameFamily, tvErrorLastNameFamily, tvErrorPhoneFamily, tvErrorRelationshipFamily;

        private Uri selectedImageUri;

        // Detector de rostros con configuración rápida y tracking habilitado
        private final FaceDetector detector = FaceDetection.getClient(
                new FaceDetectorOptions.Builder()
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                        .enableTracking()
                        .build()
        );

        private final ActivityResultLauncher<String> pickImageLauncher =
                registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                    if (uri != null) {
                        selectedImageUri = uri;
                        detectFace(uri);
                    }
                });

        private ApiService apiService;
        private SessionManager sessionManager;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_add_family);

            sessionManager = new SessionManager(this);
            apiService = ApiClient.getApiService();

            photoNewFamily = findViewById(R.id.photoNewFamily);
            buttomAddNewFamily = findViewById(R.id.buttomAddNewFamily);
            edtNameFamily = findViewById(R.id.edtNameFamily);
            edtLastNameFamily = findViewById(R.id.edtLastNameFamily);
            edtPhoneFamily = findViewById(R.id.edtPhoneFamily);
            edtRelationFamily = findViewById(R.id.edtRelationFamily);

            tvErrorPhotoFamily = findViewById(R.id.tvErrorPhotoFamily);
            tvErrorNameFamily = findViewById(R.id.tvErrorNameFamily);
            tvErrorLastNameFamily = findViewById(R.id.tvErrorLastNameFamily);
            tvErrorPhoneFamily = findViewById(R.id.tvErrorPhoneFamily);
            tvErrorRelationshipFamily = findViewById(R.id.tvErrorRelationshipFamily);

            photoNewFamily.setOnClickListener(v -> checkPermissionAndOpenGallery());

            buttomAddNewFamily.setOnClickListener(v -> {
                if (validateFields()) {
                    uploadData();
                }
            });
        }

        private boolean validateFields() {
            boolean valid = true;

            if (selectedImageUri == null) {
                tvErrorPhotoFamily.setVisibility(View.VISIBLE);
                valid = false;
            } else tvErrorPhotoFamily.setVisibility(View.GONE);

            if (edtNameFamily.getText().toString().trim().isEmpty()) {
                tvErrorNameFamily.setVisibility(View.VISIBLE);
                valid = false;
            } else tvErrorNameFamily.setVisibility(View.GONE);

            if (edtLastNameFamily.getText().toString().trim().isEmpty()) {
                tvErrorLastNameFamily.setVisibility(View.VISIBLE);
                valid = false;
            } else tvErrorLastNameFamily.setVisibility(View.GONE);

            if (edtPhoneFamily.getText().toString().trim().length() != 10) {
                tvErrorPhoneFamily.setVisibility(View.VISIBLE);
                valid = false;
            } else tvErrorPhoneFamily.setVisibility(View.GONE);

            if (edtRelationFamily.getText().toString().trim().isEmpty()) {
                tvErrorRelationshipFamily.setVisibility(View.VISIBLE);
                valid = false;
            } else tvErrorRelationshipFamily.setVisibility(View.GONE);

            return valid;
        }

        private void uploadData() {
            File file = FileUtils.getFileFromUri(this, selectedImageUri);
            if (file == null) {
                Toast.makeText(this, "Error con la imagen", Toast.LENGTH_SHORT).show();
                return;
            }

            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part photoPart = MultipartBody.Part.createFormData("photo", file.getName(), reqFile);

            RequestBody firstName = RequestBody.create(MediaType.parse("text/plain"), edtNameFamily.getText().toString().trim());
            RequestBody lastName = RequestBody.create(MediaType.parse("text/plain"), edtLastNameFamily.getText().toString().trim());
            RequestBody phone = RequestBody.create(MediaType.parse("text/plain"), edtPhoneFamily.getText().toString().trim());
            RequestBody relationship = RequestBody.create(MediaType.parse("text/plain"), edtRelationFamily.getText().toString().trim());

            List<Children> childrenList = sessionManager.getStudents();
            List<RequestBody> studentIds = new ArrayList<>();
            for (Children student : childrenList) {
                studentIds.add(RequestBody.create(MediaType.parse("text/plain"), String.valueOf(student.getId())));
            }

            String token = "Bearer " + sessionManager.getToken();

            apiService.addAuthorizedPerson(token, photoPart, firstName, lastName, phone, relationship, studentIds)
                    .enqueue(new retrofit2.Callback<AddFamilyResponse>() {
                        @Override
                        public void onResponse(retrofit2.Call<AddFamilyResponse> call, retrofit2.Response<AddFamilyResponse> response) {
                            if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                                Toast.makeText(AddFamilyActivity.this, "Responsable agregado correctamente", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(AddFamilyActivity.this, MainActivity.class).putExtra("openFragment", "tutors"));
                                finish();
                            } else {
                                Toast.makeText(AddFamilyActivity.this, "Error al guardar responsable", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(retrofit2.Call<AddFamilyResponse> call, Throwable t) {
                            Toast.makeText(AddFamilyActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        private void checkPermissionAndOpenGallery() {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_PERMISSION);
                } else {
                    pickImageLauncher.launch("image/*");
                }
            } else {
                pickImageLauncher.launch("image/*");
            }
        }

        private void detectFace(Uri uri) {
            try {
                Bitmap bitmap = Build.VERSION.SDK_INT >= 28
                        ? ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), uri))
                        : MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                InputImage image = InputImage.fromBitmap(bitmap, 0);

                detector.process(image)
                        .addOnSuccessListener(faces -> {
                            if (faces.size() != 1) {
                                showErrorDialog("Debe haber exactamente un rostro en la foto.");
                                return;
                            }

                            Face face = faces.get(0);

                            // Validación de orientación de cabeza
                            float rotY = face.getHeadEulerAngleY(); // Yaw
                            float rotZ = face.getHeadEulerAngleZ(); // Roll
                            if (Math.abs(rotY) > 20 || Math.abs(rotZ) > 15) {
                                showErrorDialog("La foto debe ser de frente y sin girar la cabeza.");
                                return;
                            }

                            // Validación del tamaño del rostro
                            android.graphics.Rect box = face.getBoundingBox();
                            int imageWidth = bitmap.getWidth();
                            int imageHeight = bitmap.getHeight();

                            float faceArea = box.width() * box.height();
                            float imageArea = imageWidth * imageHeight;

                            if (faceArea < imageArea * 0.1) {
                                showErrorDialog("Acércate más a la cámara, la cara está muy lejos.");
                                return;
                            }

                            // Si pasa todas las validaciones
                            photoNewFamily.setImageURI(uri);
                            Toast.makeText(this, "Foto aceptada ✅", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(this, "Error procesando la imagen", Toast.LENGTH_SHORT).show()
                        );

            } catch (IOException e) {
                Toast.makeText(this, "Error leyendo imagen", Toast.LENGTH_SHORT).show();
            }
        }

        private void showErrorDialog(String message) {
            new PhotoErrorDialog(this, message,
                    v -> checkPermissionAndOpenGallery(),
                    v -> {}).show();
        }

    }