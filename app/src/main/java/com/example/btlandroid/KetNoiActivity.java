package com.example.btlandroid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlandroid.adapter.UserAdapter;
import com.example.btlandroid.model.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

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
                                    userIds.add(id);
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
                                adapter.refreshOriginalList(); // Cập nhật danh sách gốc cho tìm kiếm
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Lỗi tải users: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi tải chats: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

        // 🔍 Tìm kiếm người dùng
        EditText searchBox = findViewById(R.id.searchBox);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = removeVietnameseTones(s.toString().toLowerCase());
                List<User> fullList = adapter.getOriginalList(); // ✅ dùng danh sách gốc trong adapter
                List<User> filtered = new ArrayList<>();

                for (User user : fullList) {
                    String userName = removeVietnameseTones(user.name.toLowerCase());
                    if (userName.contains(keyword)) {
                        filtered.add(user);
                    }
                }

                if (keyword.isEmpty()) {
                    adapter.restoreOriginalList();
                } else {
                    adapter.updateList(filtered);
                }
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

    // Xử lý loại bỏ dấu tiếng Việt
    private String removeVietnameseTones(String str) {
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        str = pattern.matcher(str).replaceAll("");
        return str.replaceAll("đ", "d").replaceAll("Đ", "D");
    }

    // Tự động ẩn bàn phím khi bấm ra ngoài EditText
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (view instanceof EditText) {
                Rect outRect = new Rect();
                view.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    view.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
