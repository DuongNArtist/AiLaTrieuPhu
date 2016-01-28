package com.skynet.ailatrieuphu.sockets;

public class Message {

    private int msgHandlerType; // ID of Handler (ProcessHandler - ID = 0)
    private int serverCommand;
    private long serverTime;
    private BufferData buffer;

    public Message() {

    }

    public Message(int command) {
        this.msgHandlerType = 0;
        this.serverCommand = command;
        buffer = new BufferData();
        serverTime = System.currentTimeMillis();
    }

    public Message(int command, byte[] data) {
        this.serverCommand = command;
        buffer = new BufferData(data);
        serverTime = System.currentTimeMillis();
    }

    public int getMessageHandlerType() {
        return msgHandlerType;
    }

    public void setMessageHandlerType(int type) {
        this.msgHandlerType = type;
    }

    public BufferData getBuffer() {
        return buffer;
    }

    public void setBuffer(BufferData buffer) {
        this.buffer = buffer;
    }

    public int getServerCommand() {
        return serverCommand;
    }

    public void setServerCommand(int command) {
        this.serverCommand = command;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + msgHandlerType;
        result = prime * result + serverCommand;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Message other = (Message) obj;
        if (serverCommand != other.serverCommand)
            return false;
        return true;
    }

    public long getServerTime() {
        return serverTime;
    }

}
