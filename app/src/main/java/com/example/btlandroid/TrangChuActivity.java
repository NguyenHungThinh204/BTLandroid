package com.example.btlandroid;

import com.example.btlandroid.ui.connect.KetNoiCongDongActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TrangChuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trang_chu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
//        chuyển sang trang kết nối
        ImageButton btnChat = findViewById(R.id.btnChat);
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(TrangChuActivity.this, KetNoiActivity.class);
//                startActivity(intent);
            }
        });

        // Nút Cần hỗ trợ
        ImageButton btnCanHoTro = findViewById(R.id.btnCanHoTro);
        btnCanHoTro.setOnClickListener(v -> {
            Intent intent = new Intent(TrangChuActivity.this, CanHoTroActivity.class);
            startActivity(intent);
        });

        // Nút 'see more' - bài đăng cần người hỗ trợ
        TextView txtSeeMore = findViewById(R.id.txtSeeMoreCanHoTro);  // Đảm bảo bạn gán ID trong XML
        txtSeeMore.setOnClickListener(v -> {
            Intent intent = new Intent(TrangChuActivity.this, CanHoTroActivity.class);
            startActivity(intent);
        });

        // Nút Hỗ trợ
        ImageButton btnHoTro = findViewById(R.id.btnHoTro);
        btnHoTro.setOnClickListener(v -> {
            Intent intent = new Intent(TrangChuActivity.this, HoTroActivity.class);
            startActivity(intent);
        });

        // See more - Bài đăng muốn hỗ trợ người cần
        TextView txtSeeMoreHoTro = findViewById(R.id.txtSeeMoreHoTro); // phải có ID trong XML
        txtSeeMoreHoTro.setOnClickListener(v -> {
            Intent intent = new Intent(TrangChuActivity.this, HoTroActivity.class);
            startActivity(intent);
        });
        // chuyển sang trang kết nối cộng đồng
        ImageButton btnKetNoi = findViewById(R.id.btnKetNoi);
        btnKetNoi.setOnClickListener(v -> {
            Intent intent = new Intent(TrangChuActivity.this, KetNoiCongDongActivity.class);
            startActivity(intent);
        });

        // popup menu
        ImageButton btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(TrangChuActivity.this, v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu_popup, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.menu_lich_su_dang_bai) {
                    startActivity(new Intent(TrangChuActivity.this, LichSuDangBaiActivity.class));
                    return true;
                } else if (id == R.id.menu_lich_su_tuong_tac) {
                    startActivity(new Intent(TrangChuActivity.this, LichSuTuongTacActivity.class));
                    return true;
                } else if (id == R.id.menu_lich_su_danh_gia) {
                    startActivity(new Intent(TrangChuActivity.this, LichSuDanhGiaActivity.class));
                    return true;
                }
                return false;
            });

            popup.show();
        });
    }

}