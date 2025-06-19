package com.example.btlandroid;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlandroid.adapter.ChatAdapter;
import com.example.btlandroid.model.Message;
import com.google.firebase.firestore.*;

import java.util.*;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText edtMessage;
    private ImageButton btnSend;

    private List<Message> messageList;
    private ChatAdapter chatAdapter;

    private FirebaseFirestore db;
    private CollectionReference messagesRef;

    private String currentUserId;
    private String otherUserId;
    private String otherUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.chatLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Nhận dữ liệu từ intent
        currentUserId = getIntent().getStringExtra("currentUserId");
        otherUserId = getIntent().getStringExtra("receiverId");
        otherUserName = getIntent().getStringExtra("receiverName");

        if (currentUserId == null || otherUserId == null) {
            Toast.makeText(this, "Thiếu thông tin người dùng!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Giao diện quay lại
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(ChatActivity.this, KetNoiActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        // Hiển thị tên người nhận
        TextView txtNameTop = findViewById(R.id.txtNameTop);
        TextView txtNameCenter = findViewById(R.id.txtNameCenter);
        if (otherUserName != null) {
            txtNameTop.setText(otherUserName);
            txtNameCenter.setText(otherUserName);
        }

        // Ánh xạ view
        recyclerView = findViewById(R.id.recyclerChat);
        edtMessage = findViewById(R.id.edtMessage);
        btnSend = findViewById(R.id.btnSend);

        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(this, messageList, currentUserId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);

        // Firestore
        db = FirebaseFirestore.getInstance();
        String chatId = getChatId(currentUserId, otherUserId);
        messagesRef = db.collection("chats").document(chatId).collection("messages");

        // Gửi tin nhắn
        btnSend.setOnClickListener(v -> {
            String text = edtMessage.getText().toString().trim();
            if (!TextUtils.isEmpty(text)) {
                Message message = new Message(currentUserId, text, System.currentTimeMillis());
                messagesRef.add(message)
                        .addOnSuccessListener(documentReference -> edtMessage.setText(""))
                        .addOnFailureListener(e -> Toast.makeText(this, "Lỗi gửi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });

        // Lắng nghe tin nhắn
        messagesRef.orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((snapshots, error) -> {
                    if (error != null) {
                        Toast.makeText(this, "Lỗi tải tin nhắn", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (snapshots != null) {
                        messageList.clear();
                        for (DocumentSnapshot doc : snapshots) {
                            Message msg = doc.toObject(Message.class);
                            if (msg != null) messageList.add(msg);
                        }
                        chatAdapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(messageList.size() - 1);
                    }
                });
    }

    private String getChatId(String u1, String u2) {
        return u1.compareTo(u2) < 0 ? u1 + "_" + u2 : u2 + "_" + u1;
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            view.clearFocus();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (view instanceof EditText) {
                int[] scrcoords = new int[2];
                view.getLocationOnScreen(scrcoords);
                float x = ev.getRawX() + view.getLeft() - scrcoords[0];
                float y = ev.getRawY() + view.getTop() - scrcoords[1];
                if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom()) {
                    hideKeyboard();
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}