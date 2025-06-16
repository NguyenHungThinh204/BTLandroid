package com.example.btlandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class KetNoiActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ket_noi);  // Giao diện bạn đã tạo
        // // Chuyển về Trang chủ
        ImageButton btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển về Trang chủ
                Intent intent = new Intent(KetNoiActivity.this, TrangChuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Xóa các activity khác nếu cần
                startActivity(intent);
                finish(); // Đóng màn hình hiện tại
            }
        });
        // chuyển sang trang chờ kết nối
        Button btnChoKetNoi = findViewById(R.id.btnChoKetNoi);
        btnChoKetNoi.setOnClickListener(v -> {
            Intent intent = new Intent(KetNoiActivity.this, ChoKetNoiActivity.class);
            startActivity(intent);
        });

    }
}
