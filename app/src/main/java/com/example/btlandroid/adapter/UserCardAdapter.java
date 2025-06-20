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

public class UserCardAdapter extends RecyclerView.Adapter<UserCardAdapter.UserViewHolder> {

    private Context context;
    private List<User> fullUserList;
    private List<User> filteredList;
    private String currentUserId;

    public UserCardAdapter(Context context, List<User> userList, String currentUserId) {
        this.context = context;
        this.fullUserList = new ArrayList<>(userList);
        this.filteredList = new ArrayList<>(userList);
        this.currentUserId = currentUserId;
    }

    public void setUserList(List<User> newList) {
        fullUserList.clear();
        fullUserList.addAll(newList);
        filteredList.clear();
        filteredList.addAll(newList);
        notifyDataSetChanged();
    }

    public void filter(String keyword) {
        filteredList.clear();
        if (keyword == null || keyword.trim().isEmpty()) {
            filteredList.addAll(fullUserList);
        } else {
            String lowerKeyword = keyword.toLowerCase();
            for (User user : fullUserList) {
                if (user.name.toLowerCase().contains(lowerKeyword)) {
                    filteredList.add(user);
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_card, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = filteredList.get(position);
        holder.txtName.setText(user.name);
        holder.txtId.setText(user.id);

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
        return filteredList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtId;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtUserName);
            txtId = itemView.findViewById(R.id.txtUserId);
        }
    }
}
