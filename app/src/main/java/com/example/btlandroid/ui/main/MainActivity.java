package com.example.btlandroid.ui.main;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.btlandroid.R;
import com.example.btlandroid.databinding.ActivityMainBinding;
import com.example.btlandroid.fragment.HomeFragmentUpdated;
import com.example.btlandroid.ui.BaseActivity;
import com.example.btlandroid.ui.auth.LoginActivity;
import com.example.btlandroid.ui.chat.ChatFragment;
import com.example.btlandroid.ui.profile.EditProfileActivity;
import com.example.btlandroid.ui.profile.ProfileActivity;
import com.example.btlandroid.utils.SharedPrefUtil;
import com.example.btlandroid.viewmodel.UserViewModel;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;
    private int currentPage = R.id.navHome;
    private ActivityResultLauncher<Intent> profileLauncher;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        setContentView(binding.getRoot());

        // Lấy fragmentId được truyền vào nếu có
        if (getIntent() != null && getIntent().hasExtra("fragment_id")) {
            currentPage = getIntent().getIntExtra("fragment_id", R.id.navHome);
        } else if (savedInstanceState != null) {
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
                        // Gọi lại Home sau khi quay từ Profile
                        currentPage = R.id.navHome;
                        loadFragmentById(currentPage);
                        binding.bottomNav.setSelectedItemId(currentPage);
                    }
                }
        );

        userViewModel.getUserDetail(SharedPrefUtil.getString("userId", null));
        userViewModel.getUserResult().observe(this, liveData -> {
            if (liveData.loading) return;
            if (liveData.isSuccess) {
                SharedPrefUtil.putObject("user", liveData.data);
                if (liveData.getData() == null) {
                    requireProfile();
                }
            } else {
                Toast.makeText(this, "Đã có lỗi xảy ra. Vui lòng đăng nhập lại!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
        });
    }

    private void requireProfile() {
        // Inflate layout
        View popupView = LayoutInflater.from(this).inflate(R.layout.popup_require_profile, null);

        // Tạo dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(popupView);

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }

        MaterialButton btnYes = popupView.findViewById(R.id.btnYes);
        btnYes.setOnClickListener(v -> {
            startActivity(new Intent(this, EditProfileActivity.class));
            dialog.dismiss();
        });

        dialog.show();
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
        Fragment fragment = new HomeFragmentUpdated();
        if (id == R.id.navChat) {
            fragment = new ChatFragment();
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent); // Cập nhật intent mới

        if (intent.hasExtra("fragment_id")) {
            currentPage = intent.getIntExtra("fragment_id", R.id.navHome);
            loadFragmentById(currentPage);
            binding.bottomNav.setSelectedItemId(currentPage);
        }
    }
}