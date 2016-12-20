package com.example.bombtest.constant;

/**
 * Created by HONGDA on 2016/12/14.
 */
public class Constant {
    /**
     * token 的主要作用是身份授权和安全，因此不能通过客户端直接访问融云服务器获取 token，
     * 您必须通过 Server API 从融云服务器 获取 token 返回给您的 App，并在之后连接时使用
     */
    public static final String token1 = "duFJkc/4codUK0mu+0N3fKMr9UMQ0pPPzGYJ5GDBQb4Km4VnXb8qNQfVNpNQJGEwFWZbdHdAnWPjfBuueXrRZg==";
    public static final String token2 = "i3pln72SNVi4HVeql/i30fgexAdqvb9hcKGVQbyaqfOwg8rbHG9EEAGNLVYFaFbVBN/1YT9aZN75k1C33uvOEA==";
    public static final String token3 = "AS20+IkNx4M6wTpp42CGRfgexAdqvb9hcKGVQbyaqfOwg8rbHG9EENl+K99c68bSgEMuBuNEYP47mA+Ioh8twA==";
    public static final String QQtoken1 = "eRehsoogMoAoFoAkIY0eGKMr9UMQ0pPPzGYJ5GDBQb4Km4VnXb8qNaJ/dP6glCNy6q1Bg9TfKeg9xxmRBxBFyVdcpR7oZ1n8XlGVGXaGpG61nuPEyfXz+CbIb4dl7Z2dMQ8yXrX2a08=";


    public static String curtoken = "";

    public static String Cur_userId = "";
    public static String userPassword = "";
    public static String userName = "";
    public static String Cur_userIcon = "";
    public static String usergender = "";
    public static String sign = "";

    //纸片的类型 1、文字消息，2、图片消息，3、图文消息，4、语音消息
    public static final int Paper_TEXT = 1;
    public static final int Paper_AUDIO = 2;
    public static final int Paper_IMG = 3;
    public static final int Paper_TEXT_IMG = 4;

    //云端逻辑测试
    public static String RuserId = "";
    public static String RuserName = "";
    public static String RuserIcon = "";
}
