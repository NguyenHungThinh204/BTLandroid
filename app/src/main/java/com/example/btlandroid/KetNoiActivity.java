package com.example.btlandroid;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlandroid.adapter.UserAdapter;
import com.example.btlandroid.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class KetNoiActivity extends AppCompatActivity {

    private String currentUserId = "user1"; // 👉 Sửa thành người đăng nhập thực tế

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ket_noi);

        RecyclerView listKetNoi = findViewById(R.id.listKetNoi);
        listKetNoi.setLayoutManager(new LinearLayoutManager(this));

        List<User> connectedUsers = new ArrayList<>();
        UserAdapter adapter = new UserAdapter(this, connectedUsers, currentUserId);
        listKetNoi.setAdapter(adapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference chatsRef = db.collection("chats");

        chatsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                connectedUsers.clear();
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    String chatId = doc.getId(); // ex: user1_user2

                    if (chatId.contains(currentUserId)) {
                        String[] parts = chatId.split("_");
                        if (parts.length == 2) {
                            String otherUserId = parts[0].equals(currentUserId) ? parts[1] : parts[0];
                            connectedUsers.add(new User(otherUserId, otherUserId)); // 🔁 Tên thật nếu có
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(KetNoiActivity.this, "Lỗi tải dữ liệu từ Firestore", Toast.LENGTH_SHORT).show();
            }
        });

        // Nút về trang chủ
        ImageButton btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(KetNoiActivity.this, TrangChuActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        // Nút chuyển sang "Chờ kết nối"
        Button btnChoKetNoi = findViewById(R.id.btnChoKetNoi);
        btnChoKetNoi.setOnClickListener(v -> {
            Intent intent = new Intent(KetNoiActivity.this, ChoKetNoiActivity.class);
            startActivity(intent);
        });
    }
}
