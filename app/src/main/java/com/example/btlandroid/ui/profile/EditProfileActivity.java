package com.example.btlandroid.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;

import com.example.btlandroid.R;
import com.example.btlandroid.dto.UserDetail;
import com.example.btlandroid.models.User;
import com.example.btlandroid.ui.BaseActivity;
import com.example.btlandroid.viewmodel.UserViewModel;
import com.google.android.material.button.MaterialButton;

import org.apache.commons.lang3.StringUtils;

public class EditProfileActivity extends BaseActivity {
    private ImageButton btnBack;
    private EditText edtSkill, edtSubject, edtContact, edtBio;
    private MaterialButton btnUpdate;
    private UserViewModel userViewModel;
    private UserDetail user;
    private NestedScrollView content;
    private ImageView loading;
    private RadioGroup radioStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        user = (UserDetail) getIntent().getSerializableExtra("user");
        refToViews();
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        setListeners();
        fillData();

        userViewModel.getUpdateProfileResult().observe(this, liveData -> {
            if (liveData.loading) {
                loading.setVisibility(View.VISIBLE);
                return;
            }
            if (liveData.isSuccess) {
                Toast.makeText(this, liveData.message, Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.GONE);
                user = liveData.data;

                Intent resultIntent = new Intent();
                resultIntent.putExtra("updated_user", user);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            } else {
                loading.setVisibility(View.GONE);
                Toast.makeText(this, liveData.message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setListeners() {
        btnBack.setOnClickListener(v -> {
            finish();
        });

        btnUpdate.setOnClickListener(v -> {
            updateProfile();
        });
    }

    private void updateProfile() {
        String skill = edtSkill.getText().toString().strip();
        String subject = edtSubject.getText().toString().strip();
        String contact = edtContact.getText().toString().strip();
        String bio = edtBio.getText().toString().strip();
        boolean isAvailable = radioStatus.getCheckedRadioButtonId() == R.id.radioAvailable;

        UserDetail u = new UserDetail();
        u.setId(user.getId());
        u.setName(user.getName());
        u.setEmail(user.getEmail());
        u.setAvtURL(user.getAvtURL());
        u.setPhone(contact);
        u.setAvgRating(user.getAvgRating());
        u.setBio(bio);
        u.setTutorAvailable(isAvailable);
        u.setPosition(user.getPosition());
        u.setDepartment(user.getDepartment());
        u.setSkill(skill);
        u.setSubject(subject);

        userViewModel.updateProfile(u);
    }


    private void fillData() {
        if (user == null) return;
        if (!StringUtils.isBlank(user.getSkill())) {
            edtSkill.setText(user.getSkill());
        }
        if (!StringUtils.isBlank(user.getBio())) {
            edtSubject.setText(user.getBio());
        }
        if (!StringUtils.isBlank(user.getPhone())) {
            edtContact.setText(user.getPhone());
        }
        if (user.isTutorAvailable()) {
            radioStatus.check(R.id.radioAvailable);
        } else {
            radioStatus.check(R.id.radioNotAvailable);
        }
        if (!StringUtils.isBlank(user.getBio())) {
            edtBio.setText(user.getBio());
        }
    }

    private void refToViews() {
        btnBack = findViewById(R.id.btnBack);
        edtSkill = findViewById(R.id.edtSkill);
        edtSubject = findViewById(R.id.edtSubject);
        edtContact = findViewById(R.id.edtContact);
        btnUpdate = findViewById(R.id.btnUpdate);
        content = findViewById(R.id.content);
        loading = findViewById(R.id.loading);
        edtBio = findViewById(R.id.edtBio);
        radioStatus = findViewById(R.id.radioStatus);
    }
}