package com.example.btlandroid.ui;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btlandroid.R;

public class CommingSoonActivity extends AppCompatActivity {
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comming_soon);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }
}