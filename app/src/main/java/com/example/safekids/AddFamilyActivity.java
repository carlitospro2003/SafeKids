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
    import com.example.safekids.network.AddFamilyResponse;
    import com.example.safekids.network.ApiClient;
    import com.example.safekids.network.ApiService;
    import com.example.safekids.network.*;
    import com.example.safekids.storage.SessionManager;
    import com.example.safekids.utils.FileUtils;
    import com.google.mlkit.vision.common.InputImage;
    import com.google.mlkit.vision.face.Face;
    import com.google.mlkit.vision.face.FaceDetection;
    import com.google.mlkit.vision.face.FaceDetector;
    import com.google.mlkit.vision.face.FaceDetectorOptions;

    import java.io.File;
    import java.io.FileOutputStream;
    import java.io.IOException;
    import java.io.InputStream;
    import java.util.ArrayList;
    import java.util.List;
    import okhttp3.*;

    import com.example.safekids.fregments.TutorsFragment;

    import de.hdodenhof.circleimageview.CircleImageView;
    import okhttp3.MediaType;
    import okhttp3.MultipartBody;
    import okhttp3.RequestBody;
    import retrofit2.Call;
    import retrofit2.Response;

    public class AddFamilyActivity extends AppCompatActivity {

        private static final int REQUEST_CODE_PERMISSION = 100;

        private CircleImageView photoNewFamily;
        private Button buttomAddNewFamily;
        private EditText edtNameFamily, edtLastNameFamily, edtPhoneFamily, edtRelationFamily;
        private TextView tvErrorPhotoFamily, tvErrorNameFamily, tvErrorLastNameFamily, tvErrorPhoneFamily, tvErrorRelationshipFamily;

        private Uri selectedImageUri;

        private ApiService apiService;
        private SessionManager sessionManager;

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

        // ...existing code...
        // ...existing code...
        private File compressImageFromUri(Uri imageUri) throws IOException {
            // 1. Obtener el Bitmap desde el Uri
            InputStream input = getContentResolver().openInputStream(imageUri);
            Bitmap originalBitmap = android.graphics.BitmapFactory.decodeStream(input);
            input.close();

            // 2. Comprimir el Bitmap a JPEG con calidad reducida
            File compressedFile = new File(getCacheDir(), "compressed_photo.jpg");
            int quality = 80; // Puedes bajar a 70, 60, etc. si sigue pesando mucho

            do {
                FileOutputStream out = new FileOutputStream(compressedFile);
                originalBitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
                out.flush();
                out.close();

                if (compressedFile.length() <= 2 * 1024 * 1024) break; // Menor a 2MB
                quality -= 10; // Baja la calidad si sigue pesando mucho
            } while (quality > 30); // No bajes de 30 para no perder mucha calidad

            return compressedFile;
        }
        // ...existing code...
        private void uploadData() {
            File file;
            try {
                file = compressImageFromUri(selectedImageUri);
            } catch (IOException e) {
                Toast.makeText(this, "No se pudo procesar la imagen seleccionada", Toast.LENGTH_SHORT).show();
                return;
            }

            if (file == null || !file.exists() || file.length() == 0) {
                Toast.makeText(this, "No se pudo procesar la imagen seleccionada", Toast.LENGTH_SHORT).show();
                return;
            }
            if (file.length() > 2 * 1024 * 1024) {
                Toast.makeText(this, "La imagen sigue siendo demasiado grande (máx 2MB)", Toast.LENGTH_SHORT).show();
                return;
            }

            String mimeType = "image/jpeg"; // Siempre será JPEG después de comprimir
            RequestBody reqFile = RequestBody.create(MediaType.parse(mimeType), file);
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
                                // Laravel OK, ahora llama a Python
                                int schoolId = sessionManager.getSchoolId();
                                int authorizedId = response.body().getData().getId();
                                String firstNameStr = edtNameFamily.getText().toString().trim();
                                String lastNameStr = edtLastNameFamily.getText().toString().trim();

                                // Prepara los RequestBody para Python
                                RequestBody schoolIdBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(schoolId));
                                RequestBody idBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(authorizedId));
                                RequestBody firstNameBody = RequestBody.create(MediaType.parse("text/plain"), firstNameStr);
                                RequestBody lastNameBody = RequestBody.create(MediaType.parse("text/plain"), lastNameStr);
                                RequestBody reqFilePython = RequestBody.create(MediaType.parse("image/jpeg"), file);
                                MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), reqFilePython);

                                ApiService apiServicePython = ApiClientPython.getApiService();
                                String tokenPython = "Bearer " + sessionManager.getToken();

                                apiServicePython.uploadAuthorizedPhoto(tokenPython, schoolIdBody, idBody, firstNameBody, lastNameBody, filePart)
                                                .enqueue(new retrofit2.Callback<GenericResponse>() {

                                                    @Override
                                                    public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                                                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                                                            Toast.makeText(AddFamilyActivity.this, "Imagen guardada en Python correctamente", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            String errorMsg = "Error al guardar imagen en Python";
                                                            if (response.errorBody() != null) {
                                                                try {
                                                                    errorMsg = response.errorBody().string();
                                                                } catch (IOException e) {}
                                                            }
                                                            Toast.makeText(AddFamilyActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<GenericResponse> call, Throwable t) {
                                                        Toast.makeText(AddFamilyActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();


                                                    }
                                                });

                                Toast.makeText(AddFamilyActivity.this, "Responsable agregado correctamente", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(AddFamilyActivity.this, MainActivity.class).putExtra("openFragment", "tutors"));
                                finish();
                            } else {
                                String errorMsg = "Error al guardar responsable";
                                if (response.errorBody() != null) {
                                    try {
                                        errorMsg = response.errorBody().string();
                                        android.util.Log.e("API_ERROR", errorMsg);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                Toast.makeText(AddFamilyActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(retrofit2.Call<AddFamilyResponse> call, Throwable t) {
                            Toast.makeText(AddFamilyActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
// ...existing code...


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