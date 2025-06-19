package com.example.btlandroid.ui.PostDetail;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.btlandroid.R;
import com.example.btlandroid.models.Post;
import com.google.android.material.imageview.ShapeableImageView;

public class PostDetailActivity extends AppCompatActivity {

    public static final String EXTRA_POST = "extra_post";

    private ImageView ivBack;
    private TextView tvPostTitle, tvUserName, tvUserDepartment, tvDescription, tvFee, tvSupportType, tvPostDate;
    private ShapeableImageView ivUserAvatar;
    private LinearLayout llSkills; // Changed from FlexboxLayout to LinearLayout
    private Button btnContact, btnSave;

    private Post currentPost;

    public static void start(Context context, Post post) {
        Intent intent = new Intent(context, PostDetailActivity.class);
        intent.putExtra(EXTRA_POST, post);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        initViews();
        getDataFromIntent();
        setupClickListeners();
        displayPostData();
    }

    private void initViews() {
        ivBack = findViewById(R.id.iv_back);
        tvPostTitle = findViewById(R.id.tv_post_title);
        tvUserName = findViewById(R.id.tv_user_name);
//        tvUserDepartment = findViewById(R.id.tv_user_department);
        tvDescription = findViewById(R.id.tv_description);
        tvFee = findViewById(R.id.tv_fee);
        tvSupportType = findViewById(R.id.tv_support_type);
        tvPostDate = findViewById(R.id.tv_post_date);
        ivUserAvatar = findViewById(R.id.iv_user_avatar);
        llSkills = findViewById(R.id.ll_skills); // Changed from flexboxSkills to llSkills
        btnContact = findViewById(R.id.btn_contact);
        btnSave = findViewById(R.id.btn_save);
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_POST)) {
            currentPost = (Post) intent.getSerializableExtra(EXTRA_POST);
        }
    }

    private void setupClickListeners() {
        ivBack.setOnClickListener(v -> finish());

        btnContact.setOnClickListener(v -> {
            // TODO: Implement contact functionality
        });

        btnSave.setOnClickListener(v -> {
            // TODO: Implement save post functionality
        });
    }

    private void displayPostData() {
        if (currentPost == null) return;

        // Set basic post information
        tvPostTitle.setText(currentPost.getTitle());
        tvUserName.setText(currentPost.getName());
        tvDescription.setText(currentPost.getDescription());
        tvFee.setText(currentPost.getFee());
        tvSupportType.setText(currentPost.getSupportType());

        // Format and display post date
        String formattedDate = "Đăng ngày " + currentPost.getTime();
        tvPostDate.setText(formattedDate);

        // Load user avatar using Glide
        if (currentPost.getAvtURL() != null && !currentPost.getAvtURL().isEmpty()) {
            Glide.with(this)
                    .load(currentPost.getAvtURL())
                    .placeholder(R.drawable.person_24dp)
                    .error(R.drawable.person_24dp)
                    .into(ivUserAvatar);
        } else {
            ivUserAvatar.setImageResource(R.drawable.person_24dp);
        }

        // Set department info
//        tvUserDepartment.setText("Khoa CNTT"); // Default for now

        // Display skills/subjects as horizontal chips
        displaySkillChips();
    }

    private void displaySkillChips() {
        llSkills.removeAllViews(); // Clear existing views

        if (currentPost.getSubject() != null && !currentPost.getSubject().isEmpty()) {
            String[] skills = currentPost.getSubject().split(",");

            for (String skill : skills) {
                View chipView = LayoutInflater.from(this).inflate(R.layout.item_skill_chip, llSkills, false);
                TextView tvSkill = chipView.findViewById(R.id.tv_skill);
                tvSkill.setText(skill.trim());

                // Add margin to the right of each chip
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) chipView.getLayoutParams();
                params.setMargins(0, 0, 16, 0); // right margin 16dp
                chipView.setLayoutParams(params);

                llSkills.addView(chipView);
            }
        }
    }
}