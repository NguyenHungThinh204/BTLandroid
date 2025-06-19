package com.example.btlandroid.services.post;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.btlandroid.dto.Result;
import com.example.btlandroid.models.Post;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class PostService {
    public LiveData<Result<Post>> createPost(Post post) {
        MutableLiveData<Result<Post>> liveData = new MutableLiveData<>();

        if (post == null) {
            liveData.setValue(Result.error("Thông tin bài đăng không hợp lệ."));
            return liveData;
        }

        liveData.setValue(Result.loading());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("posts").document();
        post.setId(docRef.getId());
        docRef.set(post)
                .addOnSuccessListener(documentReference -> {
                    liveData.setValue(Result.success(post, "Bài viết đã được đăng!"));
                })
                .addOnFailureListener(e -> {
                    liveData.setValue(Result.error("Đã có lỗi xảy ra: " + e.getMessage()));
                });
        return liveData;
    }
}
