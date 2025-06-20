package com.example.btlandroid.ui;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btlandroid.dto.UserDetail;
import com.example.btlandroid.ui.auth.LoginActivity;
import com.example.btlandroid.utils.SharedPrefUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onStart() {
        super.onStart();

        if (getIntent().getBooleanExtra("SKIP_LOGIN_CHECK", false)) {
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserDetail userDetail = SharedPrefUtil.getObject("user", UserDetail.class);
        if (user == null || userDetail == null) {
            // Người dùng chưa đăng nhập, chuyển hướng đến màn hình đăng nhập
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}