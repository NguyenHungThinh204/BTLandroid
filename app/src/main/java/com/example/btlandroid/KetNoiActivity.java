package com.example.btlandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlandroid.adapter.UserAdapter;
import com.example.btlandroid.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class KetNoiActivity extends AppCompatActivity {

    private String currentUserId = "user1"; // 👈 Sửa thành người đang đăng nhập thực tế (sau này)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ket_noi);

        RecyclerView listKetNoi = findViewById(R.id.listKetNoi);
        listKetNoi.setLayoutManager(new LinearLayoutManager(this));

        // Lấy danh sách từ Firebase
        DatabaseReference chatsRef = FirebaseDatabase
                .getInstance("https://btlandroid-27983-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("chats");

        chatsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<User> connectedUsers = new ArrayList<>();

                for (DataSnapshot chatSnapshot : snapshot.getChildren()) {
                    String chatKey = chatSnapshot.getKey(); // Ví dụ: "user3_user2"
                    if (chatKey != null && chatKey.contains(currentUserId)) {
                        String[] parts = chatKey.split("_");
                        if (parts.length == 2) {
                            String otherUserId = parts[0].equals(currentUserId) ? parts[1] : parts[0];

                            // Nếu chưa có bảng users, tạm dùng ID làm tên
                            connectedUsers.add(new User(otherUserId, otherUserId));
                        }
                    }
                }

                // Gán adapter
                UserAdapter adapter = new UserAdapter(KetNoiActivity.this, connectedUsers, currentUserId);
                listKetNoi.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(KetNoiActivity.this, "Lỗi tải dữ liệu!", Toast.LENGTH_SHORT).show();
            }
        });

        // Nút trở về Trang chủ
        ImageButton btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(KetNoiActivity.this, TrangChuActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        // Nút chuyển sang màn hình "Chờ kết nối"
        Button btnChoKetNoi = findViewById(R.id.btnChoKetNoi);
        btnChoKetNoi.setOnClickListener(v -> {
            Intent intent = new Intent(KetNoiActivity.this, ChoKetNoiActivity.class);
            startActivity(intent);
        });
    }
}

