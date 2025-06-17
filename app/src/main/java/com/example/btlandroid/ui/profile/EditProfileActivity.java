package com.example.btlandroid.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;

import com.example.btlandroid.R;
import com.example.btlandroid.models.User;
import com.example.btlandroid.ui.BaseActivity;
import com.example.btlandroid.viewmodel.UserViewModel;
import com.google.android.material.button.MaterialButton;

import org.apache.commons.lang3.StringUtils;

public class EditProfileActivity extends BaseActivity {
    private ImageButton btnBack;
    private EditText edtName, edtPosition, edtDepartment, edtSkill, edtSubject, edtContact;
    private MaterialButton btnUpdate;
    private UserViewModel userViewModel;
    private User user;
    private NestedScrollView content;
    private ImageView loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        user = (User) getIntent().getSerializableExtra("user");
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
        String name = edtName.getText().toString().strip();
        String position = edtPosition.getText().toString().strip();
        String department = edtDepartment.getText().toString().strip();
        String skill = edtSkill.getText().toString().strip();
        String subject = edtSubject.getText().toString().strip();
        String contact = edtContact.getText().toString().strip();

//        if (name.isEmpty() || position.isEmpty() || department.isEmpty() || skill.isEmpty() || subject.isEmpty() || contact.isEmpty()) {
//            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
//            return;
//        }

        User u = new User(user.getId());
        u.setName(name);
        u.setPosition(position);
        u.setDepartmentId(department);
//        u.setSkill(skill);
//        u.setSubject(subject);
//        u.setContact(contact);

        userViewModel.updateProfile(u);
    }


    private void fillData() {
        if (user == null) return;
        if (!StringUtils.isBlank(user.getName())) {
            edtName.setText(user.getName());
        }
        if (!StringUtils.isBlank(user.getPosition())) {
            edtPosition.setText(user.getPosition());
        }
        if (!StringUtils.isBlank(user.getDepartmentId())) {
            edtDepartment.setText(user.getDepartmentId());
        }
//        if (!StringUtils.isBlank(user.getSkill())) {
//            edtSkill.setText(user.getSkill());
//        }
        if (!StringUtils.isBlank(user.getBio())) {
            edtSubject.setText(user.getBio());
        }
        if (!StringUtils.isBlank(user.getPhone())) {
            edtContact.setText(user.getPhone());
        }
    }

    private void refToViews() {
        btnBack = findViewById(R.id.btnBack);
        edtName = findViewById(R.id.edtName);
        edtPosition = findViewById(R.id.edtPosition);
        edtDepartment = findViewById(R.id.edtDepartment);
        edtSkill = findViewById(R.id.edtSkill);
        edtSubject = findViewById(R.id.edtSubject);
        edtContact = findViewById(R.id.edtContact);
        btnUpdate = findViewById(R.id.btnUpdate);
        content = findViewById(R.id.content);
        loading = findViewById(R.id.loading);
    }
}