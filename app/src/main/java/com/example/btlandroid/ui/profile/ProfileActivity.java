package com.example.btlandroid.ui.profile;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.btlandroid.R;
import com.example.btlandroid.dto.UserDetail;
import com.example.btlandroid.ui.BaseActivity;
import com.example.btlandroid.ui.auth.LoginActivity;
import com.example.btlandroid.utils.SharedPrefUtil;
import com.example.btlandroid.viewmodel.UserViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

import org.apache.commons.lang3.StringUtils;

public class ProfileActivity extends BaseActivity {
    private UserViewModel userViewModel;
    private NestedScrollView content;
    private ImageView loading;
    private ShapeableImageView imgAvatar;
    private TextView tvName, tvPosition, tvDepartment, tvSkill, tvSubject, tvContact, tvAvailable, tvBio;
    private UserDetail currentUser;
    private ImageButton btnBack;
    private MaterialButton btnEdit, btnLogout;
    private ActivityResultLauncher<Intent> editProfileLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        setContentView(R.layout.activity_profile);
        refToViews();
        currentUser = SharedPrefUtil.getObject("user", UserDetail.class);
        fillData();

        editProfileLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        UserDetail updatedUser = (UserDetail) result.getData().getSerializableExtra("updated_user");
                        if (updatedUser != null) {
                            currentUser = updatedUser;
                            SharedPrefUtil.putObject("user", currentUser);
                            fillData();
                        }
                    }
                }
        );

        setListeners();
    }

    private void fillData() {
        if (!StringUtils.isBlank(currentUser.getAvtURL())) {
            Glide.with(this).load(currentUser.getAvtURL()).into(imgAvatar);
        }
        if (!StringUtils.isBlank(currentUser.getName())) {
            tvName.setText(currentUser.getName());
        }
        if (!StringUtils.isBlank(currentUser.getPosition())) {
            tvPosition.setText(currentUser.getPosition());
        }
        if (!StringUtils.isBlank(currentUser.getDepartment().getName())) {
            tvDepartment.setText(currentUser.getDepartment().getName());
        }
        if (!StringUtils.isBlank(currentUser.getSubject())) {
            tvSubject.setText(currentUser.getSubject());
        }
        if (!StringUtils.isBlank(currentUser.getPhone())) {
            tvContact.setText(currentUser.getPhone());
        }
        if (!StringUtils.isBlank(currentUser.getSkill())) {
            tvSkill.setText(currentUser.getSkill());
        }
        if (currentUser.isTutorAvailable()) {
            tvAvailable.setText("Sẵn sàng");
        } else {
            tvAvailable.setText("Không sẵn sàng");
        }
        if (!StringUtils.isBlank(currentUser.getBio())) {
            tvBio.setText(currentUser.getBio());
        }
    }

    private void setListeners() {
        btnBack.setOnClickListener(v -> {
            setResult(RESULT_OK);
            finish();
        });

        btnLogout.setOnClickListener(v -> showLogoutPopup());

        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            intent.putExtra("user", currentUser);
            editProfileLauncher.launch(intent);
        });
    }

    private void showLogoutPopup() {
        // Inflate layout popup_logout.xml
        View popupView = LayoutInflater.from(this).inflate(R.layout.popup_logout, null);

        // Tạo dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(popupView);

        AlertDialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }

        // Gán sự kiện cho nút "Có" và "Không"
        MaterialButton btnYes = popupView.findViewById(R.id.btnYes);
        MaterialButton btnNo = popupView.findViewById(R.id.btnNo);

        btnYes.setOnClickListener(v -> {
            logout();
            dialog.dismiss();
        });

        btnNo.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    private void logout() {
        userViewModel.logout();
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void refToViews() {
        content = findViewById(R.id.content);
        loading = findViewById(R.id.loading);
        tvName = findViewById(R.id.tvName);
        tvPosition = findViewById(R.id.tvPosition);
        tvDepartment = findViewById(R.id.tvDepartment);
        tvSkill = findViewById(R.id.tvSkill);
        tvSubject = findViewById(R.id.tvSubject);
        tvContact = findViewById(R.id.tvContact);
        imgAvatar = findViewById(R.id.imgAvatar);
        btnBack = findViewById(R.id.btnBack);
        btnEdit = findViewById(R.id.btnEdit);
        btnLogout = findViewById(R.id.btnLogout);
        tvBio = findViewById(R.id.tvBio);
        tvAvailable = findViewById(R.id.tvAvailable);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }
}