package com.example.btlandroid.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btlandroid.R;
import com.example.btlandroid.models.Post;
import com.example.btlandroid.ui.PostDetail.PostDetailActivity;

import java.util.List;

public class PostGridAdapter extends RecyclerView.Adapter<PostGridAdapter.ViewHolder> {
    private Context context;
    private List<Post> postList;

    public PostGridAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post_grid, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = postList.get(position);

        holder.tvUserName.setText(post.getName());
        holder.tvTitle.setText(post.getTitle());
        holder.tvDescription.setText(post.getDescription());
        holder.tvFee.setText(post.getFee());
        holder.tvTime.setText(post.getTime());
        holder.tvSubjects.setText(post.getSubject());

//        if (post.getSubject() != null && !post.getSubject().isEmpty()) {
//            String subjects = String.join(", ", post.getSubject());
//            holder.tvSubjects.setText(subjects);
//        }

        holder.itemView.setOnClickListener(v -> {
            PostDetailActivity.start(context, post);
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public void updateData(List<Post> newPostList) {
        this.postList = newPostList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvTitle, tvDescription, tvSubjects, tvFee, tvTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvSubjects = itemView.findViewById(R.id.tv_subjects);
            tvFee = itemView.findViewById(R.id.tv_fee);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }
}