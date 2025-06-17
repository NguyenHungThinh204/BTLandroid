package com.example.btlandroid.ui.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.btlandroid.R;
import com.example.btlandroid.viewmodel.UserViewModel;
import com.google.android.material.button.MaterialButton;

public class RegisterActivity extends AppCompatActivity {
    private EditText edtEmail, edtPassword, edtConfirmPassword;
    private MaterialButton btnRegister;
    private TextView tvLogin;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        refToViews();
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        setListener();

        userViewModel.getRegisterResult().observe(this, liveData -> {
            if (liveData.loading) {
                btnRegister.setEnabled(false);
                btnRegister.setText("Đang đăng ký...");
                return;
            }
            Toast.makeText(this, liveData.message, Toast.LENGTH_SHORT).show();
            if (liveData.isSuccess) {
                login();
                finish();
            }
        });
    }

    private void setListener() {
        btnRegister.setOnClickListener(v -> register());
        tvLogin.setOnClickListener(v -> login());
    }

    private void login() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    private void register() {
        String email = edtEmail.getText().toString().strip();
        String password = edtPassword.getText().toString().strip();
        String confirmPassword = edtConfirmPassword.getText().toString().strip();
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Email hoặc mật khẩu không được để trống!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
            return;
        }
        // Gọi service đăng ký
        userViewModel.register(email, password);
    }

    private void refToViews() {
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);
    }
}