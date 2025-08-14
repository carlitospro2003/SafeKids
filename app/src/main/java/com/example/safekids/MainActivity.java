package com.example.safekids;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.safekids.fregments.ChildrenFragment;
import com.example.safekids.fregments.NotificationsFragment;
import com.example.safekids.fregments.ProfileFragment;
import com.example.safekids.fregments.TutorsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private static final int NOTIFICATION_PERMISSION_CODE = 100;

    ChildrenFragment navChildren = new ChildrenFragment();
    TutorsFragment navTutors = new TutorsFragment();
    NotificationsFragment navNotifications = new NotificationsFragment();
    ProfileFragment navProfile = new ProfileFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnItemSelectedListener(mOnNavigationItemSelectedListener);

        // Solicitar permiso de notificaciones para Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_CODE);
            }
        }

        // Manejar el extra del Intent para abrir el fragmento correcto
        String openFragment = getIntent().getStringExtra("fragment");
        if (openFragment != null) {
            if (openFragment.equals("tutors")) {
                loadFragment(navTutors);
                navigation.setSelectedItemId(R.id.navTutors);
            } else if (openFragment.equals("NotificationsFragment")) {
                loadFragment(navNotifications);
                navigation.setSelectedItemId(R.id.navNotifications);
            } else {
                loadFragment(navChildren);
                navigation.setSelectedItemId(R.id.navChildren);
            }
        } else {
            loadFragment(navChildren);
            navigation.setSelectedItemId(R.id.navChildren);
        }
    }

    private final NavigationBarView.OnItemSelectedListener mOnNavigationItemSelectedListener =
            new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int itemId = item.getItemId();
                    if (itemId == R.id.navChildren) {
                        loadFragment(navChildren);
                        return true;
                    } else if (itemId == R.id.navTutors) {
                        loadFragment(navTutors);
                        return true;
                    } else if (itemId == R.id.navNotifications) {
                        loadFragment(navNotifications);
                        return true;
                    } else if (itemId == R.id.navProfile) {
                        loadFragment(navProfile);
                        return true;
                    }
                    return false;
                }
            };

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, puedes mostrar notificaciones
            } else {
                // Permiso denegado, opcionalmente informar al usuario
                // Toast.makeText(this, "Se requieren permisos de notificación para mostrar alertas", Toast.LENGTH_LONG).show();
            }
        }
    }
}