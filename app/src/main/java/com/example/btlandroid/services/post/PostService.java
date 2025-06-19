package com.example.btlandroid.services.post;

import androidx.annotation.NonNull;

import com.example.btlandroid.models.Post;
import com.example.btlandroid.models.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostService {
    public interface PostListCallback {
        void onResult(List<Post> needPosts, List<Post> offerPosts);
        void onError(Exception e);
    }

    public void getAllPostsWithUser(final PostListCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference postRef = db.collection("posts");
        postRef.get().addOnSuccessListener(postSnapshots -> {
            List<Post> needPosts = new ArrayList<>();
            List<Post> offerPosts = new ArrayList<>();
            List<Post> allPosts = new ArrayList<>();
            List<String> userIds = new ArrayList<>();
            for (DocumentSnapshot doc : postSnapshots.getDocuments()) {
                Post post = doc.toObject(Post.class);
                if (post != null) {
                    post.setId(doc.getId());
                    allPosts.add(post);
                    if (post.getUserId() != null && !userIds.contains(post.getUserId())) {
                        userIds.add(post.getUserId());
                    }
                }
            }
            if (userIds.isEmpty()) {
                callback.onResult(needPosts, offerPosts);
                return;
            }
            db.collection("users").whereIn("id", userIds).get().addOnSuccessListener(userSnapshots -> {
                Map<String, User> userMap = new HashMap<>();
                for (DocumentSnapshot userDoc : userSnapshots.getDocuments()) {
                    User user = userDoc.toObject(User.class);
                    if (user != null) {
                        userMap.put(user.getId(), user);
                    }
                }
                for (Post post : allPosts) {
                    User user = userMap.get(post.getUserId());
                    if (user != null) {
                        post.setName(user.getName());
                        post.setAvtURL(user.getAvtURL());
                    }
                    if ("need".equals(post.getPostType())) {
                        needPosts.add(post);
                    } else if ("offer".equals(post.getPostType())) {
                        offerPosts.add(post);
                    }
                }
                callback.onResult(needPosts, offerPosts);
            }).addOnFailureListener(callback::onError);
        }).addOnFailureListener(callback::onError);
    }
} 