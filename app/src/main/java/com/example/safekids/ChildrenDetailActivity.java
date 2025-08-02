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

public class ChildrenDetailActivity extends AppCompatActivity {

    private TextView txtNameChildren, txtLastNameChildren, txtDateChildren, txtSchoolChildren;
    private ImageView imgChildren;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_children_detail);

        TextView tvName = findViewById(R.id.txtNameChildren);
        ImageView imgChild = findViewById(R.id.photoDetailChildren);
        TextView tvBirth = findViewById(R.id.txtDateChildren);
        TextView tvSchool = findViewById(R.id.txtSchoolChildren);

        // Recupera los extras
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        int imageResId = intent.getIntExtra("imageResId", R.drawable.iconosafekids);
        String birthDate = intent.getStringExtra("birthDate");
        String school = intent.getStringExtra("school");

        // Muestra los datos
        tvName.setText(name);
        imgChild.setImageResource(imageResId);
        tvBirth.setText(birthDate);
        tvSchool.setText(school);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}