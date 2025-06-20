package com.example.btlandroid;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlandroid.adapter.UserCardAdapter;
import com.example.btlandroid.model.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class KetNoiCongDongActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText searchBox;
    private UserCardAdapter adapter;
    private String currentUserId = "ohr35QTCTPdtCXdfmbx6fj7PDd72"; // TODO: thay bằng Firebase Auth nếu cần

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ket_noi_cong_dong);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerKetNoi);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        searchBox = findViewById(R.id.searchBox);
        adapter = new UserCardAdapter(this, new ArrayList<>(), currentUserId);
        recyclerView.setAdapter(adapter);

        loadUsers();

        // Tìm kiếm người dùng
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }
        });

        // Bottom nav
        ImageButton btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(v -> {
            startActivity(new Intent(this, TrangChuActivity.class));
            finish();
        });

        ImageButton btnChat = findViewById(R.id.btnChat);
        btnChat.setOnClickListener(v -> {
            startActivity(new Intent(this, KetNoiActivity.class));
        });
    }

    private void loadUsers() {
        FirebaseFirestore.getInstance().collection("users")
                .get()
                .addOnSuccessListener(query -> {
                    List<User> fetchedList = new ArrayList<>();
                    for (var doc : query) {
                        String id = doc.getString("id");
                        String name = doc.getString("name");
                        if (id != null && !id.equals(currentUserId)) {
                            fetchedList.add(new User(id, name));
                        }
                    }
                    adapter.setUserList(fetchedList); // cập nhật danh sách và gốc
                })
                .addOnFailureListener(e -> Log.e("KetNoiCongDong", "Lỗi load users: " + e.getMessage()));
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (view instanceof EditText) {
                int[] coords = new int[2];
                view.getLocationOnScreen(coords);
                float x = ev.getRawX() + view.getLeft() - coords[0];
                float y = ev.getRawY() + view.getTop() - coords[1];

                if (x < view.getLeft() || x > view.getRight() ||
                        y < view.getTop() || y > view.getBottom()) {
                    // Ẩn bàn phím
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    view.clearFocus();
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}