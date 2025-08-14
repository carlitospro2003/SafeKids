package com.example.safekids;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.safekids.models.Children;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.safekids.storage.ExtraDataManager;
import com.example.safekids.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChildrenDetailActivity extends AppCompatActivity {

    private TextView tvName, tvBirth, tvSchool;
    private CircleImageView imgChild;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_children_detail);

        // Inicializar vistas
        tvName = findViewById(R.id.txtNameChildren);
        tvBirth = findViewById(R.id.txtDateChildren);
        tvSchool = findViewById(R.id.txtSchoolChildren);
        imgChild = findViewById(R.id.photoDetailChildren);

        // Recupera los extras
        // Recupera los extras
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String birthDate = intent.getStringExtra("birthDate");
        String school = intent.getStringExtra("school");
        String photoUrl = intent.getStringExtra("photo");

        // Muestra los datos
        tvName.setText(name != null ? name : "Nombre no disponible");
        tvBirth.setText(birthDate != null ? birthDate : "Fecha no disponible");
        tvSchool.setText(school != null ? school : "Escuela no disponible");

        // Muestra los datos
        // Cargar la imagen con Glide
        if (photoUrl != null && !photoUrl.isEmpty()) {
            Glide.with(this)
                    .load(photoUrl)
                    .placeholder(R.drawable.iconosafekids)
                    .error(R.drawable.iconosafekids)
                    .into(imgChild);
        } else {
            imgChild.setImageResource(R.drawable.iconosafekids);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}