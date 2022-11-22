package com.tools.myNotice.gpush;

import android.os.Handler;
import android.os.HandlerThread;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * @ClassName: GPushImpl
 * @Description: 作用描述
 * @Author: liu
 * @CreateDate: 2022/11/10 9:49
 * @Version: 1.0
 */
public class GPushImpl implements GPush {
    private List<Client> mClients;
    private HandlerThread mHandlerThread;
    private Handler mHandler;
    private Random mRandom = new Random();


    private final List<String> msgs = new ArrayList<String>() {
        {
            add("1、文旅部：严查以中老年为目标的包价游产品");
            add("2、加快推进沿长江户籍改革，服务长江经济带高质量发展。");
            add("3、今年首批10家非法社会组织网站被关停，含中国文艺名人协会等。");
            add("4、上海：5月1日起，电动自行车骑乘人员必须戴头盔。");
            add("5、广州：清明祭扫实行实名预约，倡导网上祭扫、错峰延后祭扫。");
            add("6、河北武安铁矿致6死事故涉嫌瞒报，企业相关人员被控制。");
            add("7、黄峥辞任拼多多董事长：将放弃超级表决权，投入科学研究。");
            add("8、打破国外20年垄断，国产人工心脏研发成功，但商用落地时间暂不确定。");
            add("9、调查：六成青年入睡时间晚于23点，梦多睡眠浅成年轻人睡眠主要问题。");
        }
    };

    GPushImpl() {
        mClients = new ArrayList<>();
        mHandlerThread = new HandlerThread("Push-Thread");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
    }

    @Override
    public void pushMessage(String msg) {
        Iterator<Client> iterator = mClients.iterator();
        while (iterator.hasNext()) {
            iterator.next().onReceiveMessage(msg);
        }
    }

    public static void start(Client client) {
        GPushImpl gPush = new GPushImpl();
        gPush.mClients.add(client);
        gPush.mHandler.post(gPush.mRunnable);
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mHandler.postDelayed(mRunnable, mRandom.nextInt(10000));
            pushMessage(msgs.get(mRandom.nextInt(msgs.size())));
        }
    };
}
