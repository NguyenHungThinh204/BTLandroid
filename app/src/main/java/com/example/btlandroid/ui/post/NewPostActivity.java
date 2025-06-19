package com.example.btlandroid.ui.post;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.example.btlandroid.R;
import com.example.btlandroid.dto.UserDetail;
import com.example.btlandroid.models.Post;
import com.example.btlandroid.ui.BaseActivity;
import com.example.btlandroid.utils.SharedPrefUtil;
import com.example.btlandroid.viewmodel.PostViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.apache.commons.lang3.StringUtils;

public class NewPostActivity extends BaseActivity {
    private ImageButton btnBack;
    private TextView btnCreatePost;
    private TextInputEditText edtTitle, edtDescription, edtSubject, edtBudget;
    private TextInputLayout titleLayout, descLayout, subjectLayout, budgetLayout;
    private RadioGroup radioPostType, radioSupportType;
    private PostViewModel postViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        setContentView(R.layout.activity_new_post);
        refToViews();

        postViewModel.getCreatePostResult().observe(this, liveData -> {
            if (liveData.loading) {
                return;
            }
            if (liveData.isSuccess) {
                finish();
            }
            Toast.makeText(this, liveData.message, Toast.LENGTH_SHORT).show();
        });

        setListener();
    }

    private void setListener() {
        btnBack.setOnClickListener(v -> {
            showPopup(R.layout.popup_cancel_new_post, R.id.btnExit, R.id.btnStay);
        });

        btnCreatePost.setOnClickListener(v -> {
            showPopup(R.layout.popup_confirm_new_post, R.id.btnYes, R.id.btnNo);
        });

        edtTitle.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                titleLayout.setHint("");
            } else {
                if (StringUtils.isBlank(edtTitle.getText().toString())) {
                    titleLayout.setHint("Nhập tiêu đề bài đăng...");
                }
            }
        });

        edtDescription.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                descLayout.setHint("");
            } else {
                if (StringUtils.isBlank(edtDescription.getText().toString())) {
                    descLayout.setHint("Mô tả chi tiết...");
                }
            }
        });

        edtSubject.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                subjectLayout.setHint("");
            } else {
                if (StringUtils.isBlank(edtSubject.getText().toString())) {
                    subjectLayout.setHint("Môn học/ Kỹ năng liên quan (C++, UI/UX,...)");
                }
            }
        });

        edtBudget.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                budgetLayout.setHint("");
            } else {
                if (StringUtils.isBlank(edtBudget.getText().toString())) {
                    budgetLayout.setHint("Môn học/ Kỹ năng liên quan (C++, UI/UX,...)");
                }
            }
        });
    }

    private void showPopup(int popupId, int btnYesId, int btnNoId) {
        View popupView = LayoutInflater.from(this).inflate(popupId, null);

        // Tạo dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(popupView);

        AlertDialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }

        MaterialButton btnYes = popupView.findViewById(btnYesId); // Thoát / Xác nhận đăng bài
        MaterialButton btnNo = popupView.findViewById(btnNoId); // Ở lại / Hủy đăng bài

        btnYes.setOnClickListener(v -> {
            dialog.dismiss();
            if (popupId == R.layout.popup_cancel_new_post) {
                finish();
            } else {
                Post post = setPost();
                if (post == null) return;
                postViewModel.createPost(post);
            }
        });

        btnNo.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    private void refToViews() {
        btnBack = findViewById(R.id.btnBack);
        btnCreatePost = findViewById(R.id.btnCreatePost);
        edtTitle = findViewById(R.id.edtTitle);
        edtDescription = findViewById(R.id.edtDescription);
        edtSubject = findViewById(R.id.edtSubject);
        edtBudget = findViewById(R.id.edtBudget);
        radioPostType = findViewById(R.id.radioPostType);
        radioSupportType = findViewById(R.id.radioSupportType);
        titleLayout = findViewById(R.id.titleLayout);
        descLayout = findViewById(R.id.descLayout);
        subjectLayout = findViewById(R.id.subjectLayout);
        budgetLayout = findViewById(R.id.budgetLayout);
    }

    private Post setPost() {
        UserDetail user = SharedPrefUtil.getObject("user", UserDetail.class);
        if (user == null) return null;

        String title = edtTitle.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();
        String subject = edtSubject.getText().toString().trim();
        String budget = edtBudget.getText().toString().trim();
        String postType = radioPostType.getCheckedRadioButtonId() == R.id.radioNeed ? "need" : "offer";
        int spTypeId = radioSupportType.getCheckedRadioButtonId();
        String supportType = null;
        if (spTypeId == R.id.radioOnline) supportType = "online";
        else if (spTypeId == R.id.radioOffline) supportType = "offline";
        else if (spTypeId == R.id.radioOtherSupportType) supportType = "other";

        Post post = new Post();
        post.setUserId(user.getId());
        post.setName(user.getName());
        post.setAvtURL(user.getAvtURL());
        post.setTitle(title);
        post.setDescription(description);
        post.setSubject(subject);
        post.setFee(budget);
        post.setPostType(postType);
        post.setSupportType(supportType);

        if (!isValidPost(post)) return null;
        return post;
    }

    private boolean isValidPost(Post post) {
        String errMsg = null;
        if (StringUtils.isBlank(post.getTitle())) {
            errMsg = "Vui lòng nhập tiêu đề";
        } else if (StringUtils.isBlank(post.getPostType())) {
            errMsg = "Vui lòng chọn loại bài đăng";
        } else if (StringUtils.isBlank(post.getSubject())) {
            errMsg = "Vui lòng nhập môn học/ kỹ năng liên quan";
        }

        if (errMsg != null) {
            Toast.makeText(this, errMsg, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    @SuppressWarnings("MissingSuperCall")
    public void onBackPressed() {
        showPopup(R.layout.popup_cancel_new_post, R.id.btnExit, R.id.btnStay);
    }
}