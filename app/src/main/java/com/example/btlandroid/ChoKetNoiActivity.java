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

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class ChoKetNoiActivity extends AppCompatActivity {
    private String currentUserId = "ohr35QTCTPdtCXdfmbx6fj7PDd72";

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
                        String chatId = doc.getId();
                        String status = doc.getString("status");
                        if (!"chờ kết nối".equalsIgnoreCase(status)) continue;

                        String[] parts = chatId.split("_");

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

                    Log.d("DEBUG", "Tìm thấy userIds trong trạng thái 'chờ kết nối': " + userIds);

                    // B2: Truy vấn người dùng từ userIds
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
                                adapter.refreshOriginalList(); // cập nhật danh sách gốc

                                // B3: Tìm kiếm
                                EditText searchBox = findViewById(R.id.searchBox);
                                searchBox.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {}

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        String keyword = removeVietnameseTones(s.toString().toLowerCase());
                                        List<User> fullList = adapter.getOriginalList(); // lấy từ danh sách gốc
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

    // Bỏ dấu tiếng Việt để tìm kiếm không dấu
    private String removeVietnameseTones(String str) {
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        str = pattern.matcher(str).replaceAll("");
        return str.replaceAll("đ", "d").replaceAll("Đ", "D");
    }

    // Ẩn bàn phím khi chạm ra ngoài EditText
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