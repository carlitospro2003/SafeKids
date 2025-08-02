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

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.io.IOException;
import java.util.List;
import com.example.safekids.fregments.TutorsFragment;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddFamilyActivity extends AppCompatActivity {

    Button buttomAddNewFamily;
    private CircleImageView photoNewFamily;
    private static final int REQUEST_CODE_PERMISSION = 100;

    private Uri selectedImageUri;

    // Configuración de ML Kit: modo rápido y con landmarks básicos
    private final FaceDetectorOptions realTimeOpts =
            new FaceDetectorOptions.Builder()
                    .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                    .enableTracking()
                    .build();

    private final FaceDetector detector = FaceDetection.getClient(realTimeOpts);

    // Nuevo launcher para abrir galería (solo imágenes)
    private final ActivityResultLauncher<String> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    selectedImageUri = uri; // Mostrar la imagen seleccionada
                    detectFace(uri);
                } else {
                    Toast.makeText(this, "No se seleccionó imagen", Toast.LENGTH_SHORT).show();
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_family);

        photoNewFamily = findViewById(R.id.photoNewFamily);
        buttomAddNewFamily = findViewById(R.id.buttomAddNewFamily);


        // Click en la imagen para abrir galería
        photoNewFamily.setOnClickListener(v -> checkPermissionAndOpenGallery());

        // Al dar clic en el botón, regresar al MainActivity y mostrar TutorsFragment
        buttomAddNewFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddFamilyActivity.this, MainActivity.class);
                intent.putExtra("openFragment", "tutors");
                startActivity(intent);
                finish(); // opcional, cierra esta pantalla para que no vuelva al presionar "atrás"
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void checkPermissionAndOpenGallery() {
        // Solo necesitamos permiso en Android 12 o menor
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
            // Android 13+ ya no requiere permiso
            pickImageLauncher.launch("image/*");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImageLauncher.launch("image/*");
            } else {
                Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void detectFace(Uri uri) {
        try {
            Bitmap bitmap;

            if (Build.VERSION.SDK_INT >= 28) {
                bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), uri));
            } else {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            }

            InputImage image = InputImage.fromBitmap(bitmap, 0);

            detector.process(image)
                    .addOnSuccessListener(faces -> handleDetectionResult(faces, uri))
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Error analizando la imagen", Toast.LENGTH_SHORT).show()
                    );

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "No se pudo procesar la imagen", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleDetectionResult(List<Face> faces, Uri uri) {

        if (faces.size() != 1) {
            Toast.makeText(this, "Debe haber exactamente un rostro", Toast.LENGTH_LONG).show();
            return;
        }

        Face face = faces.get(0);

        //  Validar que no esté de perfil
        float rotY = face.getHeadEulerAngleY(); // Yaw
        float rotZ = face.getHeadEulerAngleZ(); // Roll
        if (Math.abs(rotY) > 20 || Math.abs(rotZ) > 15) {
            Toast.makeText(this, "La foto debe ser de frente y sin girar la cabeza", Toast.LENGTH_LONG).show();
            return;
        }

        //  Validar que la cara esté suficientemente cerca (grande en la imagen)
        android.graphics.Rect box = face.getBoundingBox();
        int imageWidth = photoNewFamily.getWidth();  // o del bitmap si lo guardas
        int imageHeight = photoNewFamily.getHeight();

        float faceArea = box.width() * box.height();
        float imageArea = imageWidth * imageHeight;

        if (faceArea < imageArea * 0.1) { // menos del 10% de la imagen
            Toast.makeText(this, "Acércate más a la cámara, la cara está muy lejos", Toast.LENGTH_LONG).show();
            return;
        }

        //  Si pasa todas las validaciones, mostrar la imagen
        photoNewFamily.setImageURI(uri);
        Toast.makeText(this, "Foto aceptada ✅", Toast.LENGTH_SHORT).show();
    }

}