package com.example.btlandroid.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.btlandroid.dto.Result;
import com.example.btlandroid.models.Post;
import com.example.btlandroid.services.post.PostService;

public class PostViewModel extends ViewModel {
    private final PostService postService = new PostService();
    private final MutableLiveData<Result<Post>> createPostResult = new MutableLiveData<>();

    public LiveData<Result<Post>> getCreatePostResult() {
        return createPostResult;
    }

    public void createPost(Post post) {
        postService.createPost(post).observeForever(createPostResult::setValue);
    }
}
