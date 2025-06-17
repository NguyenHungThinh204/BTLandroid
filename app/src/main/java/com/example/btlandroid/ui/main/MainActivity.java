package com.example.btlandroid.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.example.btlandroid.R;
import com.example.btlandroid.databinding.ActivityMainBinding;
import com.example.btlandroid.fragment.HomeFragmentUpdated;
import com.example.btlandroid.ui.BaseActivity;
import com.example.btlandroid.ui.profile.ProfileActivity;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;
    private int currentPage = R.id.navHome;
    private ActivityResultLauncher<Intent> profileLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khôi phục trạng thái fragment đang chọn
        if (savedInstanceState != null) {
            currentPage = savedInstanceState.getInt("SELECTED_ITEM", R.id.navHome);
        }

        // Nếu fragment chưa tồn tại (chỉ lần đầu vào app), thì mới load fragment
        if (savedInstanceState == null && getSupportFragmentManager().findFragmentById(R.id.container) == null) {
            loadFragmentById(currentPage);
        }

        setupBottomNavigation();
        profileLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        // Người dùng nhấn back từ ProfileActivity
                        loadFragmentById(R.id.navChat);
                        binding.bottomNav.setSelectedItemId(R.id.navChat);
                    }
                }
        );
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("SELECTED_ITEM", currentPage);
    }

    private void setupBottomNavigation() {
        binding.bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() != currentPage) {
                currentPage = item.getItemId();
                loadFragmentById(currentPage);
            }
            return true;
        });
        binding.bottomNav.setSelectedItemId(currentPage);
    }

    private void loadFragmentById(int id) {
        Fragment fragment = new HomeFragmentUpdated(); // mặc định là Home
        if (id == R.id.navChat) {
            fragment = new HomeFragmentUpdated();
        } else if (id == R.id.navProfile) {
            profileLauncher.launch(new Intent(this, ProfileActivity.class));
            return;
        }

        // Chỉ replace nếu fragment khác với fragment hiện tại
        Fragment current = getSupportFragmentManager().findFragmentById(R.id.container);
        if (current == null || !current.getClass().equals(fragment.getClass())) {
            loadFragment(fragment);
        }
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }
}