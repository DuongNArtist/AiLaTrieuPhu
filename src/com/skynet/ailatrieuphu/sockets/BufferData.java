package com.skynet.ailatrieuphu.sockets;

public class BufferData {
    private static final int INIT_LEN = 32;
    private byte[] data;
    private int length;
    private int position;

    public BufferData() {
        data = new byte[INIT_LEN];
        position = 0;
    }

    public BufferData(byte[] data) {
        this.data = data;
        position = 0;
        length = data.length;
    }

    public void writeByte(byte i) {
        data[position++] = i;
        updateLength();
    }

    private void updateLength() {
        if (position > length) {
            length = position;
        }
    }

    public void ensureAvail(int available) {
        int needLen = position + available;
        if (needLen > data.length) {
            byte[] temp = new byte[needLen];
            System.arraycopy(data, 0, temp, 0, length);
            data = temp;
        }
    }

    public static String asHex(byte buf[]) {
        StringBuffer strbuf = new StringBuffer(buf.length * 2);
        for (int i = 0; i < buf.length; i++) {
            if (((int) buf[i] & 0xff) < 0x10)
                strbuf.append("0");
            strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
        }
        return strbuf.toString();
    }

    public void writeBytes(byte buffer[], int offset, int len) {
        System.arraycopy(buffer, offset, data, position, len);
        position += len;
        updateLength();
    }

    public byte readByte() {
        if (position > data.length - 1) {
            throw new ArrayIndexOutOfBoundsException("Kich thuoc mang"
                    + data.length + " du lieu dang hex " + asHex(data));
        }
        return data[position++];
    }

    public byte[] readBytes(int len) {
        byte[] res = new byte[len];
        System.arraycopy(data, position, res, 0, len);
        position += len;
        return res;
    }

    public byte[] getData() {
        return data;
    }

    public int getLength() {
        return length;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getCapacity() {
        return data.length;
    }

}
