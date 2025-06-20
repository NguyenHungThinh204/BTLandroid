package com.example.btlandroid;

import android.app.Application;

import com.example.btlandroid.utils.SharedPrefUtil;

public class SkillShareApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SharedPrefUtil.init(this);
        registerActivityLifecycleCallbacks(new AppLifecycleHandler());
    }
}
