package com.tools.myNotice.notice;

import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.UserHandle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: NotificationMonitorService
 * @Description: 通知服务
 * @Author: liu
 * @CreateDate: 2022/11/10 11:18
 * @Version: 1.0
 */
public class NotificationService extends NotificationListenerService {


    private static final String TAG = "NotificationService";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: ");
        toggleNotificationListenerService();
    }


    /**
     * 切换通知监听服务
     */
    private void toggleNotificationListenerService() {
        String brand = Build.BRAND;
        // API>=24,使用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && !("huawei".equalsIgnoreCase(brand) || "honor".equalsIgnoreCase(brand))) {
            requestRebind(new ComponentName(getApplicationContext(), NotificationService.class));

        } else {
            // API<24,使用
            PackageManager pm = getPackageManager();
            pm.setComponentEnabledSetting(new ComponentName(this, NotificationService.class),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
            pm.setComponentEnabledSetting(new ComponentName(this, NotificationService.class),
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
        }


    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn, RankingMap rankingMap) {
        super.onNotificationPosted(sbn, rankingMap);
        Log.e(TAG, "onNotificationPosted: 1111");
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.e(TAG, "onNotificationPosted: burden");
        MyNotice myNotice = new MyNotice();
        myNotice.setNotificationPkg(sbn.getPackageName());
        myNotice.setNotificationTime(sbn.getPostTime());


        try {
            Notification notification = sbn.getNotification();
            if (notification != null) {
                Bundle extras = notification.extras;
                if (extras != null) {
                    myNotice.setNotificationTitle(extras.getString(Notification.EXTRA_TITLE, ""));
//                    myNotice.setNotificationText(extras.getString(Notification.EXTRA_TEXT, ""));

                    String content = extras.getString(Notification.EXTRA_TEXT, "");
                    Object msgText = extras.getCharSequence(Notification.EXTRA_TEXT, "");

                    if (content.isEmpty() && msgText instanceof SpannableString) {
                        content = msgText.toString();
                    }


                    myNotice.setNotificationText(content);
                    // 注意：获取的通知信息和短信的传递内容不一样 短信为SpannableString 这里容易造成转换异常
//                    if (msgText instanceof SpannableString) {
//                        Log.d(TAG, "is SpannableString ...." + ((SpannableString) msgText).subSequence(0, ((SpannableString) msgText).length()));
//                        myNotice.setNotificationText(extras.getString(msgText.toString()));
//
//                    } else if (msgText instanceof String) {
//                        Log.d(TAG, "showMsg msgText=" + msgText);
//                        myNotice.setNotificationText(extras.getString(msgText.toString()));
//
//                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            NoticePushImpl.getIns().seedPostNotice(myNotice);

        }
    }


    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }


    private Map<String, Object> getNoticeInfo(Notification notification) {
        int key = 0;
        if (notification == null)
            return null;
        RemoteViews views = notification.contentView;
        if (views == null)
            return null;
        Class secretClass = views.getClass();

        try {
            Map<String, Object> text = new HashMap<>();

            Field outerFields[] = secretClass.getDeclaredFields();
            for (int i = 0; i < outerFields.length; i++) {
                if (!outerFields[i].getName().equals("mActions"))
                    continue;

                outerFields[i].setAccessible(true);

                ArrayList<Object> actions = (ArrayList<Object>) outerFields[i].get(views);
                for (Object action : actions) {
                    Field innerFields[] = action.getClass().getDeclaredFields();
                    Object value = null;
                    Integer type = null;
                    for (Field field : innerFields) {
                        field.setAccessible(true);
                        if (field.getName().equals("value")) {
                            value = field.get(action);
                        } else if (field.getName().equals("type")) {
                            type = field.getInt(action);
                        }
                    }
                    // 经验所得 type 等于9 10为短信title和内容，不排除其他厂商拿不到的情况
                    if (type != null && (type == 9 || type == 10)) {
                        if (key == 0) {
                            text.put("title", value != null ? value.toString() : "");
                        } else if (key == 1) {
                            text.put("text", value != null ? value.toString() : "");
                        } else {
                            text.put(Integer.toString(key), value != null ? value.toString() : null);
                        }
                        key++;
                    }
                }
                key = 0;

            }
            return text;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 获取notification的view
    public static View getContentView(Context context, Notification notification) {
        RemoteViews contentView = null;
        // 获取contentView
        if (notification.contentView != null) {
            contentView = notification.contentView;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            contentView = Notification.Builder.recoverBuilder(context, notification).createContentView();
        }

        // RemoteViews转成view
        View view = null;
        try {
            view = contentView == null ? null : contentView.apply(context, null);
        } catch (Throwable e) {
        }
        return view;
    }


    // 获取view里面的文本
    public static String getContent(View view) {
        StringBuilder stringBuilder = new StringBuilder();
        traversalView(view, stringBuilder);
        return stringBuilder.toString();
    }

    // 遍历View，获取TextView里面的文本内容
    private static void traversalView(View view, StringBuilder stringBuilder) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int count = viewGroup.getChildCount();

            for (int i = 0; i < count; ++i) {
                View childView = viewGroup.getChildAt(i);
                traversalView(childView, stringBuilder);
            }
        } else {
            if (view instanceof TextView) {
                TextView tv = (TextView) view;
                CharSequence text = tv.getText();
                stringBuilder.append(text);
                stringBuilder.append(";");
            }
        }
    }

    /**
     * 监听断开
     */
    @Override
    public void onListenerDisconnected() {
        String brand = Build.BRAND;
        // API>=24,使用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && !("huawei".equalsIgnoreCase(brand) || "honor".equalsIgnoreCase(brand))) {
            requestRebind(new ComponentName(getApplicationContext(), NotificationService.class));

        } else {
            // API<24,使用
            PackageManager pm = getPackageManager();
            pm.setComponentEnabledSetting(new ComponentName(this, NotificationService.class),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
            pm.setComponentEnabledSetting(new ComponentName(this, NotificationService.class),
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
        }
    }


}
