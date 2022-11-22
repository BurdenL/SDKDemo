package com.tools.myNotice.notice;

import androidx.annotation.NonNull;

/**
 * ClassName: Notice
 * Description: 通知消息实体类
 * Author: lau
 * CreateDate: 2022/11/10 11:49
 * Version: 1.0
 */
public class MyNotice {

    private String notificationPkg = "";
    private String notificationTitle = "";
    private String notificationText = "";
    private long notificationTime = 0;


    public MyNotice() {
    }

    public MyNotice(String notificationPkg, String notificationTitle, String notificationText, long notificationTime) {
        this.notificationPkg = notificationPkg;
        this.notificationTitle = notificationTitle;
        this.notificationText = notificationText;
        this.notificationTime = notificationTime;
    }

    public String getNotificationPkg() {
        return notificationPkg;
    }

    public void setNotificationPkg(String notificationPkg) {
        this.notificationPkg = notificationPkg;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }

    public long getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(long notificationTime) {
        this.notificationTime = notificationTime;
    }

    @Override
    public String toString() {
        return "MyNotice{" +
                "notificationPkg='" + notificationPkg + '\'' +
                ", notificationTitle='" + notificationTitle + '\'' +
                ", notificationText='" + notificationText + '\'' +
                ", notificationTime=" + notificationTime +
                '}';
    }
}
