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
        loadMockData();
        showDefaultContent();

        return view;
    }

    private void initViews(View view) {
        searchActionsComponent = view.findViewById(R.id.search_actions_component);
        flDynamicContent = view.findViewById(R.id.fl_dynamic_content);
    }

    private void setupComponent() {
        searchActionsComponent.setOnActionClickListener(this);
    }

    private void loadMockData() {
        // Mock data for posts needing help
        needHelpPosts = new ArrayList<>();
        needHelpPosts.add(new Post("1", "1","Nguyễn Văn A", "", "Cần gia sư Toán", "Cần tìm gia sư dạy Toán lớp 12, chuẩn bị thi đại học", "Toán, Đại số", "2 giờ trước", "200.000đ/buổi", "need_help","need"));
        needHelpPosts.add(new Post("2","2","Trần Thị B","", "Học tiếng Anh giao tiếp", "Muốn học tiếng Anh giao tiếp cơ bản cho công việc", "Tiếng Anh,Giao tiếp", "5 giờ trước", "150.000đ/buổi", "need_help", "need"));
        needHelpPosts.add(new Post("3","3","Lê Văn C", "", "Cần hỗ trợ lập trình", "Cần người hướng dẫn Java cơ bản và OOP", "Lập trình, Java", "1 ngày trước", "300.000đ/buổi", "need_help", "need"));
        needHelpPosts.add(new Post("4", "4","Phạm Thị D", "Học Photoshop", "", "Cần học Photoshop cơ bản để làm thiết kế", "Photoshop, Thiết kế", "3 giờ trước", "180.000đ/buổi", "need_help","need"));
        needHelpPosts.add(new Post("5","5","Hoàng Văn E", "Gia sư Vật lý","", "Cần gia sư dạy Vật lý lớp 11", "Vật lý", "6 giờ trước", "220.000đ/buổi", "need_help","need"));
        needHelpPosts.add(new Post("6","6","Đỗ Thị F","", "Học Excel nâng cao", "Muốn học Excel nâng cao cho công việc văn phòng", "Excel, Tin học", "4 giờ trước", "120.000đ/buổi", "need_help","need"));

        // Mock data for posts offering help
        offerHelpPosts = new ArrayList<>();
        offerHelpPosts.add(new Post("7","7","Phạm Văn D","", "Dạy Toán cấp 3", "Có kinh nghiệm 3 năm dạy Toán THPT, đã giúp nhiều học sinh đỗ đại học", "Toán, Hình học, Đại số", "3 giờ trước", "180.000đ/buổi", "Gia sư","need"));
        offerHelpPosts.add(new Post("8","8","Hoàng Thị E","", "Dạy tiếng Anh IELTS", "IELTS 7.5, có thể dạy từ cơ bản đến nâng cao", "Tiếng Anh, IELTS", "6 giờ trước", "250.000đ/buổi", "Gia sư","offer"));
        offerHelpPosts.add(new Post("9","9","Đỗ Văn F","", "Hướng dẫn lập trình", "Senior Developer, có thể dạy Java, Python, Web Development", "Lập trình, Java, Python, Web", "1 ngày trước", "400.000đ/buổi", "Mentor","offer"));
        offerHelpPosts.add(new Post("10","10","Nguyễn Thị G","", "Dạy Photoshop", "Graphic Designer 5 năm kinh nghiệm", "Photoshop, Thiết kế", "2 giờ trước", "200.000đ/buổi", "Mentor","offer"));
        offerHelpPosts.add(new Post("11","11","Trần Văn H","", "Gia sư Vật lý", "Thạc sĩ Vật lý, dạy từ cấp 2 đến cấp 3", "Vật lý", "4 giờ trước", "240.000đ/buổi", "Gia sư","offer"));
        offerHelpPosts.add(new Post("12","12","Lê Thị I","", "Dạy Excel chuyên nghiệp", "Chuyên gia Excel với 7 năm kinh nghiệm", "Excel, Tin học", "5 giờ trước", "150.000đ/buổi", "Mentor","offer"));
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