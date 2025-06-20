package com.example.btlandroid.utils;

import java.text.DecimalFormat;
import java.util.Date;

public class Util {
    public static String parseTime(Date date) {
        Date now = new Date();
        long diffMillis = now.getTime() - date.getTime();

        long diffMinutes = diffMillis / (60 * 1000);
        long diffHours = diffMillis / (60 * 60 * 1000);
        long diffDays = diffMillis / (24 * 60 * 60 * 1000);

        String timeAgo;
        if (diffDays > 0) {
            timeAgo = diffDays + " ngày trước";
        } else if (diffHours > 0) {
            timeAgo = diffHours + " giờ trước";
        } else if (diffMinutes > 0) {
            timeAgo = diffMinutes + " phút trước";
        } else {
            timeAgo = "Vừa xong";
        }

        return timeAgo;
    }

    public static String parseSpType(String spType) {
        switch (spType) {
            case "online": spType = "Online (Zoom, Meet,...)"; break;
            case "offline": spType = "Offline (trao đổi sau)"; break;
            case "other": spType = "Thỏa thuận"; break;
        }
        return spType;
    }

    public static String parseBudget(long budget) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(budget) + " VND/giờ";
    }
}
