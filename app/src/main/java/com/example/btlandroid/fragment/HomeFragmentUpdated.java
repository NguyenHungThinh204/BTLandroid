package com.example.btlandroid.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btlandroid.R;
import com.example.btlandroid.adapter.NeedHelpPostAdapter;
import com.example.btlandroid.adapter.OfferHelpPostAdapter;
import com.example.btlandroid.adapter.PostGridAdapter;
import com.example.btlandroid.component.SearchActionsComponent;
import com.example.btlandroid.models.Post;
import com.example.btlandroid.ui.post.NewPostActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragmentUpdated extends Fragment implements SearchActionsComponent.OnActionClickListener {

    private SearchActionsComponent searchActionsComponent;
    private FrameLayout flDynamicContent;

    private List<Post> needHelpPosts, offerHelpPosts;
    private View currentContentView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initViews(view);
        setupComponent();
        loadPostsFromFirestore();

        return view;
    }

    private void initViews(View view) {
        searchActionsComponent = view.findViewById(R.id.search_actions_component);
        flDynamicContent = view.findViewById(R.id.fl_dynamic_content);
    }

    private void setupComponent() {
        searchActionsComponent.setOnActionClickListener(this);
    }

    private void loadPostsFromFirestore() {
        needHelpPosts = new ArrayList<>();
        offerHelpPosts = new ArrayList<>();
        com.example.btlandroid.services.post.PostService postService = new com.example.btlandroid.services.post.PostService();
        postService.getAllPostsWithUser(new com.example.btlandroid.services.post.PostService.PostListCallback() {
            @Override
            public void onResult(List<Post> needPosts, List<Post> offerPosts) {
                needHelpPosts.clear();
                offerHelpPosts.clear();
                needHelpPosts.addAll(needPosts);
                offerHelpPosts.addAll(offerPosts);
                showDefaultContent();
            }
            @Override
            public void onError(Exception e) {
                // Xử lý lỗi, ví dụ: Toast.makeText(getContext(), "Lỗi tải dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDefaultContent() {
        currentContentView = LayoutInflater.from(getContext()).inflate(R.layout.layout_home_default, flDynamicContent, false);
        flDynamicContent.removeAllViews();
        flDynamicContent.addView(currentContentView);

        // Setup horizontal RecyclerViews for default view
        RecyclerView rvNeedHelpPosts = currentContentView.findViewById(R.id.rv_need_help_posts);
        RecyclerView rvOfferHelpPosts = currentContentView.findViewById(R.id.rv_offer_help_posts);

        LinearLayoutManager needHelpLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvNeedHelpPosts.setLayoutManager(needHelpLayoutManager);
        NeedHelpPostAdapter needHelpAdapter = new NeedHelpPostAdapter(getContext(), needHelpPosts);
        rvNeedHelpPosts.setAdapter(needHelpAdapter);

        LinearLayoutManager offerHelpLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvOfferHelpPosts.setLayoutManager(offerHelpLayoutManager);
        OfferHelpPostAdapter offerHelpAdapter = new OfferHelpPostAdapter(getContext(), offerHelpPosts);
        rvOfferHelpPosts.setAdapter(offerHelpAdapter);
    }

    private void showNeedHelpGrid() {
        currentContentView = LayoutInflater.from(getContext()).inflate(R.layout.layout_need_help_grid, flDynamicContent, false);
        flDynamicContent.removeAllViews();
        flDynamicContent.addView(currentContentView);

        RecyclerView rvPostsGrid = currentContentView.findViewById(R.id.rv_posts_grid);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        rvPostsGrid.setLayoutManager(gridLayoutManager);
        PostGridAdapter gridAdapter = new PostGridAdapter(getContext(), needHelpPosts);
        rvPostsGrid.setAdapter(gridAdapter);
    }

    private void showOfferHelpGrid() {
        currentContentView = LayoutInflater.from(getContext()).inflate(R.layout.layout_offer_help_grid, flDynamicContent, false);
        flDynamicContent.removeAllViews();
        flDynamicContent.addView(currentContentView);

        RecyclerView rvPostsGrid = currentContentView.findViewById(R.id.rv_posts_grid);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        rvPostsGrid.setLayoutManager(gridLayoutManager);
        PostGridAdapter gridAdapter = new PostGridAdapter(getContext(), offerHelpPosts);
        rvPostsGrid.setAdapter(gridAdapter);
    }

    // SearchActionsComponent.OnActionClickListener implementation
    @Override
    public void onNeedHelpClick() {
        showNeedHelpGrid();
    }

    @Override
    public void onSupportClick() {
        showOfferHelpGrid();
    }

    @Override
    public void onConnectClick() {
        // TODO: Implement connect functionality
        // For now, return to default view
        searchActionsComponent.setSelectedAction(SearchActionsComponent.ActionType.HOME);
        showDefaultContent();
    }

    @Override
    public void onAddPostClick() {
        startActivity(new Intent(getContext(), NewPostActivity.class));
    }

    @Override
    public void onSearchClick(String query) {
        // TODO: Implement search functionality
    }
}