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


    // 消息类型，1发送，2撤回
    private int notificationType = 0;
    private int notificationId = 0;
    private String notificationKey = "";
    private String notificationPkg = "";
    private String notificationTitle = "";
    private String notificationText = "";
    private long notificationTime = 0;


    public int getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(int notificationType) {
        this.notificationType = notificationType;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public String getNotificationKey() {
        return notificationKey;
    }

    public void setNotificationKey(String notificationKey) {
        this.notificationKey = notificationKey;
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
                "notificationType=" + notificationType +
                ", notificationId=" + notificationId +
                ", notificationKey='" + notificationKey + '\'' +
                ", notificationPkg='" + notificationPkg + '\'' +
                ", notificationTitle='" + notificationTitle + '\'' +
                ", notificationText='" + notificationText + '\'' +
                ", notificationTime=" + notificationTime +
                '}';
    }
}
