package com.tools.myNotice.notice;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @ClassName: GPushImpl
 * @Description: 通知SDK控制类
 * @Author: liu
 * @CreateDate: 2022/11/10 9:49
 * @Version: 1.0
 */
public class NoticePushImpl implements NoticePush {
    private List<NoticeClient> mClients;

    private static NoticePushImpl noticePush;
    private MyNotice postNotice;

    private Activity activity;


    private NoticePushImpl(Activity activity) {
        mClients = new ArrayList<>();

        if (activity != null)
            this.activity = activity;

        this.startService();
    }


    public static void init(Activity activity) {
        if (noticePush == null) {
            noticePush = new NoticePushImpl(activity);
        }


    }

    public static NoticePushImpl getIns() {

        return noticePush;

    }

    @Override
    public void pushMessage(MyNotice myNotice) {

        Iterator<NoticeClient> iterator = mClients.iterator();
        while (iterator.hasNext()) {
            iterator.next().onReceiveMessage(myNotice);
        }
    }

    public void addReceiveMessage(NoticeClient client) {
        if (client == null)
            return;

        if (this.mClients == null)
            this.mClients = new ArrayList<>();

        if (!this.mClients.contains(client)) {
            noticePush.mClients.add(client);
        }

        if (!isServiceRunning(activity, "com.tools.myNotice.notice.NotificationService")) {
            startService();
        }
    }

    public void removeReceiveMessage(NoticeClient client) {

        if (this.mClients.isEmpty() || client == null)
            return;

        while (this.mClients.contains(client)) {
            noticePush.mClients.remove(client);
        }
    }


    public void seedPostNotice(MyNotice postNotice) {
        if (postNotice != null) {
            this.postNotice = postNotice;

            pushMessage(this.postNotice);

        }
    }

    public void startService() {
        try {
            Intent intent = new Intent(activity, NotificationService.class);
            activity.startService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 判断服务是否开启
     *
     * @return
     */
    public static boolean isServiceRunning(Context context, String ServiceName) {
        if (("").equals(ServiceName) || ServiceName == null)
            return false;
        ActivityManager myManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager
                .getRunningServices(Integer.MAX_VALUE);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString()
                    .equals(ServiceName)) {
                return true;
            }
        }
        return false;
    }

}
