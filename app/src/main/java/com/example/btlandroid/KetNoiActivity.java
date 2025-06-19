package com.example.btlandroid;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btlandroid.adapter.UserAdapter;
import com.example.btlandroid.model.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class KetNoiActivity extends AppCompatActivity {

    private String currentUserId = "ohr35QTCTPdtCXdfmbx6fj7PDd72"; // 👉 Sửa thành người đăng nhập thực tế
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ket_noi);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        RecyclerView listKetNoi = findViewById(R.id.listKetNoi);
        listKetNoi.setLayoutManager(new LinearLayoutManager(this));

        List<User> connectedUsers = new ArrayList<>();
        UserAdapter adapter = new UserAdapter(this, connectedUsers, currentUserId);
        listKetNoi.setAdapter(adapter);
// B1: Lấy tất cả userIds từ chats
        db.collection("chats")
                .get()
                .addOnSuccessListener(chatSnap -> {
                    Set<String> userIds = new HashSet<>();

                    for (QueryDocumentSnapshot doc : chatSnap) {
                        String chatId = doc.getId(); // ví dụ: user1_user2
                        String status = doc.getString("status");
                        String[] parts = chatId.split("_");

                        // Bỏ qua nếu trạng thái là "chờ kết nối"
                        if ("chờ kết nối".equalsIgnoreCase(status)) continue;

                        // Nếu chat có sự tham gia của currentUserId thì mới xử lý
                        boolean isParticipant = false;
                        for (String id : parts) {
                            if (id.equals(currentUserId)) {
                                isParticipant = true;
                                break;
                            }
                        }

                        // Nếu có tham gia, thêm người còn lại vào danh sách
                        if (isParticipant) {
                            for (String id : parts) {
                                if (!id.equals(currentUserId)) {
                                    userIds.add(id); // chỉ thêm người còn lại
                                }
                            }
                        }
                    }
                    Log.d("DEBUG", "Tìm thấy userIds từ chats: " + userIds);

                    // B2: Truy vấn danh sách users
                    db.collection("users")
                            .get()
                            .addOnSuccessListener(userSnap -> {
                                connectedUsers.clear();
                                for (QueryDocumentSnapshot doc : userSnap) {
                                    String id = doc.getString("id");
                                    String name = doc.getString("name");

                                    if (id != null && name != null && userIds.contains(id) && !id.equals(currentUserId)) {
                                        connectedUsers.add(new User(id, name));
                                    }
                                }
                                Log.d("DEBUG", "Hiển thị users: " + connectedUsers.size());
                                adapter.notifyDataSetChanged();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Lỗi tải users: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi tải chats: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        RecyclerView listKetNoi = findViewById(R.id.listKetNoi);
//        listKetNoi.setLayoutManager(new LinearLayoutManager(this));
//
//        List<User> connectedUsers = new ArrayList<>();
//        UserAdapter adapter = new UserAdapter(this, connectedUsers, currentUserId);
//        listKetNoi.setAdapter(adapter);
//
//        db.collection("users")
//                .get()
//                .addOnSuccessListener(querySnapshot -> {
//                    for (QueryDocumentSnapshot doc : querySnapshot) {
//                        String id = doc.getString("id");
//                        String name = doc.getString("name");
//                        if (id != null && name != null && !id.equals(currentUserId)) {
//                            connectedUsers.add(new User(id, name));
//                        }
//                    }
//                    adapter.notifyDataSetChanged();
//                })
//                .addOnFailureListener(e -> {
//                    Toast.makeText(KetNoiActivity.this, "Lỗi lấy người dùng: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                });

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
