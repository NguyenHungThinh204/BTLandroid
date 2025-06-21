package com.example.btlandroid.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;

import com.example.btlandroid.R;
import com.example.btlandroid.dto.UserDetail;
import com.example.btlandroid.models.Department;
import com.example.btlandroid.models.User;
import com.example.btlandroid.services.department.DepartmentService;
import com.example.btlandroid.ui.BaseActivity;
import com.example.btlandroid.utils.SharedPrefUtil;
import com.example.btlandroid.viewmodel.UserViewModel;
import com.google.android.material.button.MaterialButton;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class EditProfileActivity extends BaseActivity {
    private ImageButton btnBack;
    private EditText edtName, edtSkill, edtSubject, edtContact, edtBio;
    private MaterialButton btnUpdate;
    private UserViewModel userViewModel;
    private UserDetail user;
    private NestedScrollView content;
    private ImageView loading;
    private RadioGroup rgPosition;
    private AutoCompleteTextView selectDepartment;
    private Department departmentSelected = null;
    private DepartmentService departmentService;
    private ArrayAdapter<Department> departmentAdapter;

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

                if (SharedPrefUtil.getInt("firstLogin", -1) == 1) {
                    SharedPrefUtil.putInt("firstLogin", -1);
                    SharedPrefUtil.putObject("user", user);
                } else {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("updated_user", user);
                    setResult(Activity.RESULT_OK, resultIntent);
                }
                finish();
            } else {
                loading.setVisibility(View.GONE);
                Toast.makeText(this, liveData.message, Toast.LENGTH_SHORT).show();
            }
        });

        departmentService = new DepartmentService();
        departmentService.getDepartments(new DepartmentService.DepartmentsCallback() {

            @Override
            public void onSuccess(List<Department> departments) {
                departmentAdapter = new ArrayAdapter<>(
                        getApplicationContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        departments
                );
                selectDepartment.setAdapter(departmentAdapter);
            }

            @Override
            public void onFailure(Exception e) {
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

        selectDepartment.setOnItemClickListener((parent, view, position, id) -> {
            departmentSelected = (Department) parent.getItemAtPosition(position);
        });
    }

    private void updateProfile() {
        String name = edtName.getText().toString().strip();
        String position = rgPosition.getCheckedRadioButtonId() == R.id.rbStudent ? "Sinh viên" : "Giảng viên";
        String skill = edtSkill.getText().toString().strip();
        String subject = edtSubject.getText().toString().strip();
        String contact = edtContact.getText().toString().strip();
        String bio = edtBio.getText().toString().strip();

        UserDetail u = new UserDetail();
        u.setId(SharedPrefUtil.getString("userId", null));
        u.setName(name);
        u.setEmail(SharedPrefUtil.getString("email", null));
        u.setAvtURL(null);
        u.setPhone(contact);
        u.setAvgRating(5);
        u.setBio(bio);
        u.setTutorAvailable(true);
        u.setPosition(position);
        u.setDepartment(departmentSelected);
        u.setSkill(skill);
        u.setSubject(subject);

        if (validate(u)) {
            userViewModel.updateProfile(u);
        }
    }

    private boolean validate(UserDetail user) {
        if (StringUtils.isBlank(user.getName())) {
            Toast.makeText(this, "Vui lòng nhập tên", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (StringUtils.isBlank(user.getPosition())) {
            Toast.makeText(this, "Vui lòng chọn chức vụ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (user.getDepartment() == null) {
            Toast.makeText(this, "Vui lòng chọn lớp/đơn vị công tác", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void fillData() {
        if (user == null) return;
        edtName.setText(user.getName());
        if (!StringUtils.isBlank(user.getPosition())) {
            if (user.getPosition().equals("Sinh viên")) {
                rgPosition.check(R.id.rbStudent);
            } else {
                rgPosition.check(R.id.rbTeacher);
            }
        }
        if (user.getDepartment() != null) {
            selectDepartment.setText(user.getDepartment().getName());
        }
        if (!StringUtils.isBlank(user.getSubject())) {
            edtSubject.setText(user.getSubject());
        }
        if (!StringUtils.isBlank(user.getPhone())) {
            edtContact.setText(user.getPhone());
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
        edtName = findViewById(R.id.edtName);
        rgPosition = findViewById(R.id.rgPosition);
        selectDepartment = findViewById(R.id.selectDepartment);
    }
}