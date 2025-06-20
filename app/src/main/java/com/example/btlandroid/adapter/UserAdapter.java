package com.example.btlandroid.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlandroid.ChatActivity;
import com.example.btlandroid.R;
import com.example.btlandroid.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final Context context;
    private final List<User> originalList; // Danh sách gốc đầy đủ
    private final List<User> displayList;  // Danh sách hiển thị (sau khi tìm kiếm)
    private String currentUserId;

    public UserAdapter(Context context, List<User> userList, String currentUserId) {
        this.context = context;
        this.originalList = new ArrayList<>(userList);  // copy từ danh sách ban đầu
        this.displayList = userList; // tham chiếu tới danh sách ban đầu
        this.currentUserId = currentUserId;
    }

    public void updateList(List<User> newList) {
        displayList.clear();
        displayList.addAll(newList);
        notifyDataSetChanged();
    }

    public void restoreOriginalList() {
        displayList.clear();
        displayList.addAll(originalList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_connection, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = displayList.get(position);
        holder.txtUserName.setText(user.name);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("currentUserId", currentUserId);
            intent.putExtra("receiverId", user.id);
            intent.putExtra("receiverName", user.name);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return displayList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView txtUserName;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUserName = itemView.findViewById(R.id.txtUserName);
        }
    }
    public void refreshOriginalList() {
        originalList.clear();
        originalList.addAll(displayList);
    }
    public List<User> getOriginalList() {
        return new ArrayList<>(originalList);
    }

}
