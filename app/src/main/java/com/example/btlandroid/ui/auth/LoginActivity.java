package com.example.btlandroid.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.btlandroid.ForgotPasswordActivity;
import com.example.btlandroid.R;
import com.example.btlandroid.ui.main.MainActivity;
import com.example.btlandroid.utils.SharedPrefUtil;
import com.example.btlandroid.viewmodel.UserViewModel;
import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity {
    private EditText edtEmail, edtPassword;
    private MaterialButton btnLogin;
    private TextView tvRegister, btnForgotPassword;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        refToViews();
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        setListener();

        userViewModel.getLoginResult().observe(this, liveData -> {
            if (liveData.loading) {
                btnLogin.setEnabled(false);
                btnLogin.setText("Đang đăng nhập...");
                return;
            }
            if (liveData.isSuccess) {
                SharedPrefUtil.putString("userId", liveData.data.getId());
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, liveData.message, Toast.LENGTH_SHORT).show();
                btnLogin.setEnabled(true);
                btnLogin.setText("Đăng nhập");
            }
        });
    }

    private void setListener() {
        btnLogin.setOnClickListener(v -> login());
        tvRegister.setOnClickListener(v -> register());
        btnForgotPassword.setOnClickListener(v -> forgotPassword());
    }

    private void register() {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    private void forgotPassword() {
        startActivity(new Intent(this, ForgotPasswordActivity.class));
    }

    private void login() {
        String email = edtEmail.getText().toString().strip();
        String password = edtPassword.getText().toString().strip();
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email hoặc mật khẩu không được để trống!", Toast.LENGTH_SHORT).show();
            return;
        }
        userViewModel.login(email, password);
    }

    private void refToViews() {
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);
    }
}