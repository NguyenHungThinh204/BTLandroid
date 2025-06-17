package com.example.btlandroid.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.btlandroid.R;
import com.example.btlandroid.dto.UserDetail;
import com.example.btlandroid.models.User;
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

        editProfileLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        UserDetail updatedUser = (UserDetail) result.getData().getSerializableExtra("updated_user");
                        if (updatedUser != null) {
                            currentUser = updatedUser;
                            fillData();
                        }
                    }
                }
        );

        userViewModel.getUserDetail(SharedPrefUtil.getString("userId", null));

        userViewModel.getUserResult().observe(this, liveData -> {
            if (liveData.loading) {
                content.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                return;
            }
            if (liveData.isSuccess) {
                loading.setVisibility(View.GONE);
                currentUser = liveData.data;
                fillData();
                content.setVisibility(View.VISIBLE);
            } else {
                content.setVisibility(View.GONE);
                loading.setVisibility(View.GONE);
                Toast.makeText(this, liveData.message, Toast.LENGTH_SHORT).show();
            }
        });

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

        btnLogout.setOnClickListener(v -> {
            userViewModel.logout();
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            intent.putExtra("user", currentUser);
            editProfileLauncher.launch(intent);
        });
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