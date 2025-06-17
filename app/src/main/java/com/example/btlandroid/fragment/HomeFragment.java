package com.example.btlandroid.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btlandroid.R;
import com.example.btlandroid.adapter.NeedHelpPostAdapter;
import com.example.btlandroid.adapter.OfferHelpPostAdapter;
import com.example.btlandroid.models.Post;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView rvNeedHelpPosts, rvOfferHelpPosts;
    private NeedHelpPostAdapter needHelpAdapter;
    private OfferHelpPostAdapter offerHelpAdapter;
    private List<Post> needHelpPosts, offerHelpPosts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initViews(view);
        setupRecyclerViews();
        loadMockData();

        return view;
    }

    private void initViews(View view) {
        rvNeedHelpPosts = view.findViewById(R.id.rv_need_help_posts);
        rvOfferHelpPosts = view.findViewById(R.id.rv_offer_help_posts);
    }

    private void setupRecyclerViews() {
        // Setup Need Help Posts RecyclerView
        LinearLayoutManager needHelpLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvNeedHelpPosts.setLayoutManager(needHelpLayoutManager);

        // Setup Offer Help Posts RecyclerView
        LinearLayoutManager offerHelpLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvOfferHelpPosts.setLayoutManager(offerHelpLayoutManager);
    }

    private void loadMockData() {
        // Mock data for posts needing help
        needHelpPosts = new ArrayList<>();
        needHelpPosts.add(new Post(
                "Nguyễn Văn A",
                "Cần gia sư Toán",
                "Cần tìm gia sư dạy Toán lớp 12, chuẩn bị thi đại học",
                Arrays.asList("Toán", "Đại số"),
                "2 giờ trước",
                "200.000đ/buổi",
                "need_help",
                "need"
        ));

        needHelpPosts.add(new Post(
                "Trần Thị B",
                "Học tiếng Anh giao tiếp",
                "Muốn học tiếng Anh giao tiếp cơ bản cho công việc",
                Arrays.asList("Tiếng Anh", "Giao tiếp"),
                "5 giờ trước",
                "150.000đ/buổi",
                "need_help",
                "need"
        ));

        needHelpPosts.add(new Post(
                "Lê Văn C",
                "Cần hỗ trợ lập trình",
                "Cần người hướng dẫn Java cơ bản và OOP",
                Arrays.asList("Lập trình", "Java"),
                "1 ngày trước",
                "300.000đ/buổi",
                "need_help",
                "need"
        ));

        // Mock data for posts offering help
        offerHelpPosts = new ArrayList<>();
        offerHelpPosts.add(new Post(
                "Phạm Văn D",
                "Dạy Toán cấp 3",
                "Có kinh nghiệm 3 năm dạy Toán THPT, đã giúp nhiều học sinh đỗ đại học",
                Arrays.asList("Toán", "Hình học", "Đại số"),
                "3 giờ trước",
                "180.000đ/buổi",
                "Gia sư",
                "offer"
        ));

        offerHelpPosts.add(new Post(
                "Hoàng Thị E",
                "Dạy tiếng Anh IELTS",
                "IELTS 7.5, có thể dạy từ cơ bản đến nâng cao",
                Arrays.asList("Tiếng Anh", "IELTS"),
                "6 giờ trước",
                "250.000đ/buổi",
                "Gia sư",
                "offer"
        ));

        offerHelpPosts.add(new Post(
                "Đỗ Văn F",
                "Hướng dẫn lập trình",
                "Senior Developer, có thể dạy Java, Python, Web Development",
                Arrays.asList("Lập trình", "Java", "Python", "Web"),
                "1 ngày trước",
                "400.000đ/buổi",
                "Mentor",
                "offer"
        ));

        // Set adapters
        needHelpAdapter = new NeedHelpPostAdapter(getContext(), needHelpPosts);
        rvNeedHelpPosts.setAdapter(needHelpAdapter);

        offerHelpAdapter = new OfferHelpPostAdapter(getContext(), offerHelpPosts);
        rvOfferHelpPosts.setAdapter(offerHelpAdapter);
    }
}