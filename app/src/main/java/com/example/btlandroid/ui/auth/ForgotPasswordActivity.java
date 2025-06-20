package com.example.btlandroid.ui.auth;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btlandroid.R;
import com.example.btlandroid.services.email.EmailService;
import com.google.android.material.button.MaterialButton;

import org.apache.commons.lang3.StringUtils;

public class ForgotPasswordActivity extends AppCompatActivity {
    private ImageButton btnBack;
    private MaterialButton btnSend;
    private EditText edtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        refToViews();
        setListeners();
    }

    private void setListeners() {
        btnBack.setOnClickListener(v -> {
            finish();
        });

        btnSend.setOnClickListener(v -> {
            sendResetPasswordEmail();
        });
    }

    private void sendResetPasswordEmail() {
        String email = edtEmail.getText().toString().strip();
        if (StringUtils.isBlank(email)) {
            Toast.makeText(this, "Email không được để trống!", Toast.LENGTH_SHORT).show();
            return;
        }
        EmailService emailService = new EmailService();
        emailService.sendResetPasswordEmail(email);
        showPopup();
    }

    private void showPopup() {
        View popupView = LayoutInflater.from(this).inflate(R.layout.popup_forgot_password, null);

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

        MaterialButton btnYes = popupView.findViewById(R.id.btnYes);

        btnYes.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        dialog.show();
    }

    private void refToViews() {
        btnBack = findViewById(R.id.btnBack);
        btnSend = findViewById(R.id.btnSend);
        edtEmail = findViewById(R.id.edtEmail);
    }

}