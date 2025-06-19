package com.example.btlandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlandroid.adapter.UserAdapter;
import com.example.btlandroid.model.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChoKetNoiActivity extends AppCompatActivity {
    private String currentUserId = "ohr35QTCTPdtCXdfmbx6fj7PDd72"; // 👉 Sửa thành người đăng nhập thực tế

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

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        RecyclerView listKetNoi = findViewById(R.id.recyclerChoKetNoi);
        listKetNoi.setLayoutManager(new LinearLayoutManager(this));

        List<User> connectedUsers = new ArrayList<>();
        UserAdapter adapter = new UserAdapter(this, connectedUsers, currentUserId);
        listKetNoi.setAdapter(adapter);

        // B1: Lấy userIds từ chats có trạng thái "chờ kết nối"
        db.collection("chats")
                .get()
                .addOnSuccessListener(chatSnap -> {
                    Set<String> userIds = new HashSet<>();

                    for (QueryDocumentSnapshot doc : chatSnap) {
                        String chatId = doc.getId(); // ví dụ: user1_user2
                        String status = doc.getString("status");
                        if (!"chờ kết nối".equalsIgnoreCase(status)) continue;

                        String[] parts = chatId.split("_");

                        // Nếu currentUserId tham gia vào chat
                        boolean isParticipant = false;
                        for (String id : parts) {
                            if (id.equals(currentUserId)) {
                                isParticipant = true;
                                break;
                            }
                        }

                        if (isParticipant) {
                            for (String id : parts) {
                                if (!id.equals(currentUserId)) {
                                    userIds.add(id); // chỉ thêm người còn lại
                                }
                            }
                        }
                    }

                    Log.d("DEBUG", "Tìm thấy userIds trong trạng thái 'chờ kết nối': " + userIds);

                    // B2: Truy vấn thông tin từ bảng users
                    db.collection("users")
                            .get()
                            .addOnSuccessListener(userSnap -> {
                                connectedUsers.clear();
                                for (QueryDocumentSnapshot doc : userSnap) {
                                    String id = doc.getString("id");
                                    String name = doc.getString("name");

                                    if (id != null && name != null && userIds.contains(id)) {
                                        connectedUsers.add(new User(id, name));
                                    }
                                }
                                Log.d("DEBUG", "Số người đang chờ kết nối hiển thị: " + connectedUsers.size());
                                adapter.notifyDataSetChanged();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Lỗi tải users: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi tải chats: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

        // Nút chuyển sang trang kết nối
        Button btnKetNoi = findViewById(R.id.btnKetNoi);
        btnKetNoi.setOnClickListener(v -> {
            Intent intent = new Intent(ChoKetNoiActivity.this, KetNoiActivity.class);
            startActivity(intent);
            finish();
        });

        // Nút về trang chủ
        ImageButton btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(ChoKetNoiActivity.this, TrangChuActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }
}
