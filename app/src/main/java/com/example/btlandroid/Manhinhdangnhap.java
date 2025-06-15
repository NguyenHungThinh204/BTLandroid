package com.example.btlandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Manhinhdangnhap extends AppCompatActivity {

    private TextView tvRegisterLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manhinhdangnhap);  // Sử dụng đúng tên layout

        tvRegisterLink = findViewById(R.id.tvRegisterLink);  // Lấy tham chiếu TextView

        // Thiết lập sự kiện khi nhấn vào TextView
        tvRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển tới màn hình đăng ký
                Intent intent = new Intent(Manhinhdangnhap.this, Manhinhdangky.class); // Đảm bảo đúng tên class
                startActivity(intent);
            }
        });
    }
}
