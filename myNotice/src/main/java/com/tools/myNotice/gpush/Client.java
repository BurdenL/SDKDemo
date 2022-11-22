package com.tools.myNotice.gpush;

/**
 * @ClassName: Client
 * @Description: 作用描述
 * @Author: liu
 * @CreateDate: 2022/11/10 9:50
 * @Version: 1.0
 */
public interface Client {
    void onReceiveMessage(String msg);
}
