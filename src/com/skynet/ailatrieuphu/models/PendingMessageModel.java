package com.skynet.ailatrieuphu.models;

import com.skynet.ailatrieuphu.sockets.Message;

public class PendingMessageModel {

    private Message message;
    private boolean isNeedLogin;

    public PendingMessageModel(Message msg, boolean is) {
        message = msg;
        isNeedLogin = is;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public boolean isNeedLogin() {
        return isNeedLogin;
    }

    public void setNeedLogin(boolean isNeedLogin) {
        this.isNeedLogin = isNeedLogin;
    }

}
