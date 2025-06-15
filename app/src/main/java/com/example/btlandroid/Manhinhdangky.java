package com.example.btlandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Manhinhdangky extends AppCompatActivity {

    private TextView tvLoginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manhinhdangky);  // Sử dụng đúng tên layout

        tvLoginLink = findViewById(R.id.tvLoginLink);  // Lấy tham chiếu TextView

        // Thiết lập sự kiện khi nhấn vào TextView
        tvLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển tới màn hình đăng nhập
                Intent intent = new Intent(Manhinhdangky.this, Manhinhdangnhap.class); // Đảm bảo đúng tên class
                startActivity(intent);
            }
        });
    }
}
