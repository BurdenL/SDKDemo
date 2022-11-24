package com.wallpapers.sdkdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tools.myNotice.notice.MyNotice;
import com.tools.myNotice.notice.NoticePushImpl;
import com.tools.myNotice.notice.NoticeClient;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    // 通知相关
    private int id = 1111;
    private String channelId = "channelId1";//渠道id
    private NotificationManager mNotificationManager;
    private NoticeClient noticeClient;

    private Button btn_send;
    private TextView textview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textview = findViewById(R.id.textview);
        btn_send = findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this::onClick);


        Log.e(TAG, "onCreate: " + getPackageName());
        if (NotificationManagerCompat.getEnabledListenerPackages(this).contains(getPackageName())) {
            NoticePushImpl.getIns(this);

        } else {
            startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
        }


        mNotificationManager = (NotificationManager) getSystemService(MainActivity.NOTIFICATION_SERVICE);
        addNotificationChannel();

//        GPushImpl.start(new Client() {
//            @Override
//            public void onReceiveMessage(String msg) {
//                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
//            }
//        });


        noticeClient = new NoticeClient() {
            @Override
            public void onReceiveMessage(MyNotice myNotice) {
                if (myNotice != null) {
                    Toast.makeText(MainActivity.this, "SKD_Demo:  " + myNotice, Toast.LENGTH_SHORT).show();

                    textview.setText(myNotice.toString());
//                    writeData(simpleDateFormat.format(new Date(System.currentTimeMillis())) + ":" + myNotice);

                }
            }
        };
        NoticePushImpl.getIns(MainActivity.this).addReceiveMessage(noticeClient);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        NoticePushImpl.getIns(MainActivity.this).removeReceiveMessage(noticeClient);
    }

    public void addNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //创建通知渠道
            CharSequence name = "渠道名称1";
            String description = "渠道描述1";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;//重要性级别 这里用默认的
            NotificationChannel mChannel = new NotificationChannel(channelId, name, importance);

            mChannel.setDescription(description);//渠道描述
            mChannel.enableLights(true);//是否显示通知指示灯
            mChannel.enableVibration(true);//是否振动

            mNotificationManager.createNotificationChannel(mChannel);//创建通知渠道
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                NotificationCompat.Builder mBuilder1 = new NotificationCompat.Builder(MainActivity.this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)//小图标
                        .setContentTitle("我是标题")
                        .setContentText("我是内容内容");

                mNotificationManager.notify(id, mBuilder1.build());
                break;
        }
    }
}