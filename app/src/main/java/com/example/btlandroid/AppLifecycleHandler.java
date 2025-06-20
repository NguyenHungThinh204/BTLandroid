package com.example.btlandroid;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

public class AppLifecycleHandler implements Application.ActivityLifecycleCallbacks {
    private int activityCount = 0;

    @Override
    public void onActivityStarted(Activity activity) {
        activityCount++;
    }

    @Override
    public void onActivityStopped(Activity activity) {
        activityCount--;
        // Nếu không còn activity nào → user vừa thoát app
        if (activityCount == 0) {
            Log.d("AppLifecycle", "App moved to background");
        }
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (activityCount == 0) {
            Log.d("AppLifecycle", "App killed");

            // Xóa dữ liệu nếu app bị kill
            SharedPreferences prefs = activity.getSharedPreferences("skill_share_app", Context.MODE_PRIVATE);
            prefs.edit().clear().apply();

            FirebaseAuth.getInstance().signOut();
        }
    }

    // Các phương thức còn lại không cần dùng
    @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}
    @Override public void onActivityResumed(Activity activity) {}
    @Override public void onActivityPaused(Activity activity) {}
    @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}
}
