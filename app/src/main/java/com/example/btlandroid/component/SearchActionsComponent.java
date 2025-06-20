package com.example.btlandroid.component;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.example.btlandroid.R;

public class SearchActionsComponent extends LinearLayout {

    private EditText etSearch;
    private LinearLayout btnNeedHelp, btnSupport, btnConnect, btnCreatePost;
    private ImageView ivNeedHelp, ivSupport, ivConnect, ivReload;

    private OnActionClickListener listener;
    private ActionType currentSelectedAction = ActionType.HOME;

    public enum ActionType {
        HOME, NEED_HELP, SUPPORT, CONNECT
    }

    public interface OnActionClickListener {
        void onNeedHelpClick();
        void onSupportClick();
        void onConnectClick();
        void onAddPostClick();
        void onSearchClick(String query);
        void onReloadClick();
    }

    public SearchActionsComponent(Context context) {
        super(context);
        init();
    }

    public SearchActionsComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SearchActionsComponent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.component_search_actions, this, true);

        etSearch = findViewById(R.id.et_search);
        btnNeedHelp = findViewById(R.id.btn_need_help);
        btnSupport = findViewById(R.id.btn_support);
        btnConnect = findViewById(R.id.btn_connect);
        ivNeedHelp = findViewById(R.id.iv_need_help);
        ivSupport = findViewById(R.id.iv_support);
        ivConnect = findViewById(R.id.iv_connect);
        btnCreatePost = findViewById(R.id.btn_create_post);
        ivReload = findViewById(R.id.iv_reload);

        setupClickListeners();
    }

    private void setupClickListeners() {
        btnNeedHelp.setOnClickListener(v -> {
            if (listener != null) {
                setSelectedAction(ActionType.NEED_HELP);
                listener.onNeedHelpClick();
            }
        });

        btnSupport.setOnClickListener(v -> {
            if (listener != null) {
                setSelectedAction(ActionType.SUPPORT);
                listener.onSupportClick();
            }
        });

        btnConnect.setOnClickListener(v -> {
            if (listener != null) {
//                setSelectedAction(ActionType.CONNECT);
                listener.onConnectClick();
            }
        });

        ivReload.setOnClickListener(v -> {
            if (listener != null) {
                setSelectedAction(ActionType.HOME);
                listener.onReloadClick();
            }
        });

        btnCreatePost.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAddPostClick();
            }
        });

        findViewById(R.id.iv_search).setOnClickListener(v -> {
            if (listener != null) {
                String query = etSearch.getText().toString().trim();
                listener.onSearchClick(query);
            }
        });
    }

    public void setOnActionClickListener(OnActionClickListener listener) {
        this.listener = listener;
    }

    public void setSelectedAction(ActionType actionType) {
        // Reset all backgrounds
        ivNeedHelp.setBackgroundResource(R.drawable.circle_background);
        ivSupport.setBackgroundResource(R.drawable.circle_background);
        ivConnect.setBackgroundResource(R.drawable.circle_background);

        // Set selected background
        switch (actionType) {
            case NEED_HELP:
                ivNeedHelp.setBackgroundResource(R.drawable.circle_background_selected);
                break;
            case SUPPORT:
                ivSupport.setBackgroundResource(R.drawable.circle_background_selected);
                break;
            case CONNECT:
                ivConnect.setBackgroundResource(R.drawable.circle_background_selected);
                break;
            case HOME:
            default:
                // All remain default
                break;
        }

        currentSelectedAction = actionType;
    }

    public ActionType getCurrentSelectedAction() {
        return currentSelectedAction;
    }
}