package com.example.btlandroid.ui.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.btlandroid.R;
import com.example.btlandroid.models.User;
import com.example.btlandroid.ui.BaseActivity;
import com.example.btlandroid.utils.SharedPrefUtil;
import com.example.btlandroid.viewmodel.UserViewModel;
import com.google.android.material.imageview.ShapeableImageView;

import org.apache.commons.lang3.StringUtils;

public class ProfileActivity extends BaseActivity {
    private UserViewModel userViewModel;
    private NestedScrollView content;
    private ImageView loading;
    private ShapeableImageView imgAvatar;
    private TextView tvName, tvPosition, tvDepartment, tvSkill, tvSubject, tvContact;
    private User currentUser;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        setContentView(R.layout.activity_profile);
        refToViews();

        userViewModel.getUser(SharedPrefUtil.getString("userId", null));

        userViewModel.getUserResult().observe(this, liveData -> {
            if (liveData.loading) {
                content.setVisibility(View.GONE);
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
        if (!StringUtils.isBlank(currentUser.getDepartmentId())) {
            tvDepartment.setText(currentUser.getDepartmentId());
        }
        if (!StringUtils.isBlank(currentUser.getBio())) {
            tvSkill.setText(currentUser.getBio());
        }
        if (!StringUtils.isBlank(currentUser.getPhone())) {
            tvSubject.setText(currentUser.getPhone());
        }
        if (!StringUtils.isBlank(currentUser.getSkill())) {
            tvSkill.setText(currentUser.getSkill());
        }
    }

    private void setListeners() {
        btnBack.setOnClickListener(v -> {
            setResult(RESULT_OK);
            finish();
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
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }
}