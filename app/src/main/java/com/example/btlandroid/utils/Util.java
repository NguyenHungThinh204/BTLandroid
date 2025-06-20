package com.example.btlandroid.utils;

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
}
