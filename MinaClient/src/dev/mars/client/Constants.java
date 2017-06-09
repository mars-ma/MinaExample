package dev.mars.client;



/**
 * 常量值
 * Created by dong.bing on 2016/11/24.
 */
public class Constants {
    public static final String secretKey = "cc175b9c0f1b6a831c399e269772661";
    public static final boolean DEBUG = true;
    //Socket服务器IP地址
    public static final String REMOTE_IP = "127.0.0.1";
    //Socket服务器端口号
    public static final int PORT = 8890;
    /*public static final String REMOTE_ID = "192.168.123.194";
    public static final int PORT = 6488;*/

    /**
     * READ_IDLE_TIMEOUT 设置会话读空闲时间
     */
    public static final int READ_IDLE_TIMEOUT = 60; //{@link READ_IDLE_TIMEOUT}

    /**
     * WRITE_IDLE_TIMEOUT 设置会话写空闲时间，当会话写空闲发送心跳包给服务器
     */
    public static final int WRITE_IDLE_TIMEOUT = 45;
    //WRITE_IDLE_TIMEOUT

    /**
     * 发生READ_IDLE_TIMES次 READ IDLE事件后关闭会话
     */
    public static final int READ_IDLE_CLOSE_TIMES = 2;

    /**
     * 第一次重连时间间隔
     */
    public static final long RECONNECT_TIME_INTERVAL = 5000;

    /**
     * 重连间隔增量
     */
    /*public static final long RECONNECT_TIME_INTERVAL_ADDITION = 5000;*/
    public static final long RECONNECT_TIME_INTERVAL_ADDITION = 0;

    /**
     * 最大重连间隔
     */
    public static final long MAX_RECONNECT_TIME_INTERVAL = 60000;

    //网络请求type名称
    public static final String REQUEST_LOGIN = "requestLogin";
    public static final String RESPONSE_LOGIN = "responseLogin";
    public static final String SEND_MESSAGE = "sendMessage";
    public static final String RECEIVE_MESSAGE = "receiveMessage";
    public static final String READ_MESSAGE = "readMessage";
    public static final String RESPONSE_READ_MESSAGE = "responseReadMessage";
    public static final String READ_MESSAGES = "readMessages";
    public static final String KICK_OFFLINE = "kickOffline";

    public static final String DEFAULT_DOWNLOAD_PATH = "G:\\download";

}
