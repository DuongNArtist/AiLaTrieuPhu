package com.skynet.ailatrieuphu.sockets;

public class Protocol {
    public static final String IOS = "IOS";
    public static final String ANDROID = "ANDORID";
    public static final String J2ME = "J2ME";
    public static final byte PROVIDER_ONLY_CARD = 0;
    public static final byte PROVIDER_SMS_CARD = 1;
    public static final byte CMD_REQUEST_PING = -99;
    public static final byte CMD_REQUEST_LOGIN = 0;
    public static final byte CMD_REQUEST_SEND_PLATFORM = 77;
    public static final byte CMD_REQUEST_SEND_PROVIDER = -1;
    public static final int CMD_SESSION_FAILD = 1;
    public static final int CMD_CONNECT_FAILD = -50;
    public static final int CMD_REQUEST_GET_NORMAL_PLAY_PACKAGE_QUESTION = 96;
    public static final int CMD_REQUEST_GET_DIRECT_VTV3_PACKAGE_QUESTION = 101;
    public static final int CMD_REQUEST_SUBMIT_NORMAL_PLAY_RESULT = 97;
    public static final int CMD_REQUEST_SUBMIT_INDIRECT = 110;
    public static final int CMD_REQUEST_SUBMIT_VTV3_PLAY_ANSWER = 102;
    public static final int CMD_REQUEST_CHECK_IN_DIRECT_VTV3 = 99;
    public static final int CMD_REQUEST_CHECK_OUT_DIRECT_VTV3 = 100;
    public static final int CMD_REQUEST_GET_RANK_ALL = 98;
    public static final int CMD_REQUEST_GET_RANK_VTV3 = 103;
    public static final int CMD_REQUEST_GET_RANK_PLAYER_VTV3 = 104;
    public static final int CMD_REQUEST_GET_SELECTED_WEEK_PACKAGE_QUESTION = 105;
    public static final int CMD_REQUEST_GET_LIST_OF_WEEK = 106;
    public static final int CMD_REQUEST_END_DIRECT_VTV3 = 107;
    public static final int CMD_REQUEST_NEW_GAME = 108;
    public static final int CMD_REQUEST_JOIN_VTV3 = 109;
    public static final int CMD_REQUEST_TIME_BEGIN_DIRECT_VTV3 = 111;

}
