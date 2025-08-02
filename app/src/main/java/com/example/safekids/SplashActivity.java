package com.example.safekids;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.safekids.storage.SessionManager;


public class SplashActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        sessionManager = new SessionManager(this); // Inicializamos SessionManager


        //Agregar animaciones
        Animation animationOne = AnimationUtils.loadAnimation(this, R.anim.scroll_up);
        Animation animationTwo = AnimationUtils.loadAnimation(this, R.anim.scroll_down);

        TextView safekids = findViewById(R.id.tvSafeKids);
        ImageView logo = findViewById(R.id.imvSafeKids);

        safekids.setAnimation(animationTwo);
        logo.setAnimation(animationOne);

        new Handler().postDelayed(() -> {
            if (sessionManager.isLoggedIn()) {
                // Ir al menú principal
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            } else {
                // Ir al login
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
            finish(); // Cerrar SplashActivity
        }, 2000);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}