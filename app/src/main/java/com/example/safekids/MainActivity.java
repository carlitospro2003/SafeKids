package com.example.safekids;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.safekids.fregments.ChildrenFragment;
import com.example.safekids.fregments.NotificationsFragment;
import com.example.safekids.fregments.ProfileFragment;
import com.example.safekids.fregments.TutorsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

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


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 👇 Aquí detectas si vienes desde AddFamilyActivity para abrir el fragmento de tutores
        String openFragment = getIntent().getStringExtra("openFragment");
        if (openFragment != null && openFragment.equals("tutors")) {
            loadFragment(navTutors);
            navigation.setSelectedItemId(R.id.navTutors); // <- asegúrate que este ID coincida con tu item
        } else {
            loadFragment(navChildren);
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
}