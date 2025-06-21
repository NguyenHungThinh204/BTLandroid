package com.example.btlandroid.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlandroid.ui.chat.ChatActivity;
import com.example.btlandroid.R;
import com.example.btlandroid.models.User;

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
                if (user.getName().toLowerCase().contains(lowerKeyword)) {
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
        holder.txtName.setText(user.getName());
        holder.txtPhone.setText(user.getPhone());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("currentUserId", currentUserId);
            intent.putExtra("receiverId", user.getId());
            intent.putExtra("receiverName", user.getName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPhone;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtUserName);
            txtPhone = itemView.findViewById(R.id.txtPhone);
        }
    }
}
