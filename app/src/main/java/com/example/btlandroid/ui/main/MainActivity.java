package com.example.btlandroid.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.example.btlandroid.R;
import com.example.btlandroid.databinding.ActivityMainBinding;
import com.example.btlandroid.ui.BaseActivity;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;
    private int currentPage = R.id.navHome;

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
    }

    private void loadFragmentById(int id) {
        Fragment fragment;
        switch (id) {
            case R.id.navHome -> fragment = new HomeFragment();
            case R.id.navChat -> fragment = new ChatFragment();
            case R.id.navAccount -> fragment = new AccountFragment();
            default -> fragment = new HomeFragment();
        }

        // Chỉ replace nếu fragment khác với fragment hiện tại
        Fragment current = getSupportFragmentManager().findFragmentById(R.id.container);
        if (current != null && current.getClass() != fragment.getClass()) {
            loadFragment(fragment);
        }
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }
}