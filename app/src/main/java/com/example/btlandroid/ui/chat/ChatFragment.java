package com.example.btlandroid.ui.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.core.content.ContextCompat;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.btlandroid.R;
import com.example.btlandroid.adapter.UserAdapter;
import com.example.btlandroid.models.User;
import com.example.btlandroid.utils.SharedPrefUtil;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class ChatFragment extends Fragment {
    private EditText searchBox;
    private Button btnKetNoi, btnChoKetNoi;
    private RecyclerView recyclerKetNoi, recyclerChoKetNoi;
    private boolean isConnectTab = true;
    private String currentUserId = SharedPrefUtil.getString("userId", null);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        recyclerKetNoi = getView().findViewById(R.id.recyclerKetNoi);
        recyclerKetNoi.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerChoKetNoi = getView().findViewById(R.id.recyclerChoKetNoi);
        recyclerChoKetNoi.setLayoutManager(new LinearLayoutManager(getContext()));
        searchBox = getView().findViewById(R.id.searchBox);
        btnKetNoi = getView().findViewById(R.id.btnKetNoi);
        btnChoKetNoi = getView().findViewById(R.id.btnChoKetNoi);

        btnKetNoi.setOnClickListener(v -> {
            isConnectTab = true;
            recyclerKetNoi.setVisibility(View.VISIBLE);
            recyclerChoKetNoi.setVisibility(View.GONE);
            btnKetNoi.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.red)));
            btnChoKetNoi.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.gray)));
        });

        btnChoKetNoi.setOnClickListener(v -> {
            isConnectTab = false;
            recyclerKetNoi.setVisibility(View.GONE);
            recyclerChoKetNoi.setVisibility(View.VISIBLE);
            btnKetNoi.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.gray)));
            btnChoKetNoi.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.red)));
        });

        List<User> connectedUsers = new ArrayList<>();
        UserAdapter adapterConnected = new UserAdapter(getContext(), connectedUsers, currentUserId);
        recyclerKetNoi.setAdapter(adapterConnected);

        List<User> waitingUsers = new ArrayList<>();
        UserAdapter adapterWaiting = new UserAdapter(getContext(), waitingUsers, currentUserId);
        recyclerChoKetNoi.setAdapter(adapterWaiting);

        db.collection("chats")
                .get()
                .addOnSuccessListener(chatSnap -> {
                    Set<String> connectedUserIds = new HashSet<>();
                    Set<String> waitingUserIds = new HashSet<>();

                    for (QueryDocumentSnapshot doc : chatSnap) {
                        String chatId = doc.getId(); // ví dụ: user1_user2
                        String status = doc.getString("status");
                        String[] parts = chatId.split("_");

                        if ("chờ kết nối".equalsIgnoreCase(status)) {
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
                                        waitingUserIds.add(id);
                                    }
                                }
                            }
                        } else {
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
                                        connectedUserIds.add(id);
                                    }
                                }
                            }
                        }
                    }
//                    Log.d("DEBUG", "Tìm thấy userIds từ chats: " + userIds);

                    // B2: Truy vấn danh sách users
                    db.collection("users")
                            .get()
                            .addOnSuccessListener(userSnap -> {
                                connectedUsers.clear();
                                waitingUsers.clear();
                                for (QueryDocumentSnapshot doc : userSnap) {
                                    User user = doc.toObject(User.class);
                                    if (user.getId() != null && user.getName() != null) {
                                        if (connectedUserIds.contains(user.getId())) {
                                            connectedUsers.add(user);
                                        } else if (waitingUserIds.contains(user.getId())) {
                                            waitingUsers.add(user);
                                        }
                                    }
                                }
//                                Log.d("DEBUG", "Hiển thị users: " + connectedUsers.size());
                                adapterConnected.notifyDataSetChanged();
                                adapterConnected.refreshOriginalList(); // Cập nhật danh sách gốc cho tìm kiếm
                                adapterWaiting.notifyDataSetChanged();
                                adapterWaiting.refreshOriginalList(); // Cập nhật danh sách gốc cho tìm kiếm
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getContext(), "Lỗi tải users: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Lỗi tải chats: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = removeVietnameseTones(s.toString().toLowerCase());
                if (isConnectTab) {
                    List<User> fullList = adapterConnected.getOriginalList(); // ✅ dùng danh sách gốc trong adapter
                    List<User> filtered = new ArrayList<>();

                    for (User user : fullList) {
                        String userName = removeVietnameseTones(user.getName().toLowerCase());
                        if (userName.contains(keyword)) {
                            filtered.add(user);
                        }
                    }

                    if (keyword.isEmpty()) {
                        adapterConnected.restoreOriginalList();
                    } else {
                        adapterConnected.updateList(filtered);
                    }
                } else {
                    List<User> fullList = adapterWaiting.getOriginalList(); // ✅ dùng danh sách gốc trong adapter
                    List<User> filtered = new ArrayList<>();

                    for (User user : fullList) {
                        String userName = removeVietnameseTones(user.getName().toLowerCase());
                        if (userName.contains(keyword)) {
                            filtered.add(user);
                        }
                    }

                    if (keyword.isEmpty()) {
                        adapterWaiting.restoreOriginalList();
                    } else {
                        adapterWaiting.updateList(filtered);
                    }
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    private String removeVietnameseTones(String str) {
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        str = pattern.matcher(str).replaceAll("");
        return str.replaceAll("đ", "d").replaceAll("Đ", "D");
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            View view = getCurrentFocus();
//            if (view instanceof EditText) {
//                Rect outRect = new Rect();
//                view.getGlobalVisibleRect(outRect);
//                if (!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
//                    view.clearFocus();
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    if (imm != null) {
//                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//                    }
//                }
//            }
//        }
//        return super.dispatchTouchEvent(ev);
//    }
}