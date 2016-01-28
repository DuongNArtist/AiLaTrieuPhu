package com.skynet.ailatrieuphu.sockets;

import com.skynet.ailatrieuphu.activities.AiLaTrieuPhuActivity;

public interface SocketCallback {
    public void onReceived(AiLaTrieuPhuActivity mainActivity, Message message);

    public void onFailed();
}
