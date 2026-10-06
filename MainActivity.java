package com.example.project1;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottomNavigation);

        if (savedInstanceState == null) {
            openFragment(new HomeFragment());
        }

        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                openFragment(new HomeFragment());
                return true;
            } else if (id == R.id.nav_add) {
                openFragment(new AddItemFragment());
                return true;
            } else if (id == R.id.nav_items) {
                openFragment(new QueriesFragment());
                return true;
            } else if (id == R.id.nav_profile) {
                openFragment(new ProfileFragment());
                return true;
            }
            return false;
        });
    }

    private void openFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    public void showAdd() {
        bottomNavigation.setSelectedItemId(R.id.nav_add);
        openFragment(new AddItemFragment());
    }

    public void showHome() {
        bottomNavigation.setSelectedItemId(R.id.nav_home);
    }

    public void showQueries(String filter) {
        QueriesFragment fragment = new QueriesFragment();
        Bundle bundle = new Bundle();
        bundle.putString("filter", filter);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
        bottomNavigation.setSelectedItemId(R.id.nav_items);
    }
}
