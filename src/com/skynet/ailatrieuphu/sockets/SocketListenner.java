package com.skynet.ailatrieuphu.sockets;

public interface SocketListenner {
    public void onReceived(Message message);

    public void onDisconnected();

    public void onConnected();

    public void onFailed(int reason);
}
