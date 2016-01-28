package com.skynet.ailatrieuphu.sockets;

public abstract class MessageHandler {

    private Object context;

    public Object getContext() {
        return context;
    }

    public void setContext(Object context) {
        this.context = context;
    }

    protected abstract void serviceMessage(Message message, int messageId);

    public MessageHandler() {
    }

    public void processMessage(Message message) {
        int messageType = message.getServerCommand();// lay cmd cho tung msg
        try {
            serviceMessage(message, messageType);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public abstract void onFailed(int reason);

    public abstract void onDisconnected();

    public abstract void onConnected();
}
