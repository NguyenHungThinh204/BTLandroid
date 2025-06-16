package com.example.btlandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ChoKetNoiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cho_ket_noi);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // chuyển sang trang kết nối
        Button btnKetNoi = findViewById(R.id.btnKetNoi);
        btnKetNoi.setOnClickListener(v -> {
            Intent intent = new Intent(ChoKetNoiActivity.this, KetNoiActivity.class);
            startActivity(intent);
            finish(); // đóng trang hiện tại để tránh back stack dài
        });
        // chuyển về trang chủ
        ImageButton btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển về Trang chủ
                Intent intent = new Intent(ChoKetNoiActivity.this, TrangChuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Xóa các activity khác nếu cần
                startActivity(intent);
                finish(); // Đóng màn hình hiện tại
            }
        });
    }
}