package com.skynet.ailatrieuphu.sockets;

public class IOUtility {

    public static byte readByte(Message message) {
        return message.getBuffer().readByte();
    }

    public static short readShort(Message message) {
        short res = 0;
        for (int i = 0; i < 2; i++) {
            res <<= 8;
            res |= (0xFF & message.getBuffer().readByte());
        }
        return res;
    }

    public static byte[] readBytes(Message message) {
        int len = readShort(message);
        return message.getBuffer().readBytes(len);
    }

    public static long readLong(Message message) {
        long res = 0;
        for (int i = 0; i < 8; i++) {
            res <<= 8;
            res |= (0xFF & message.getBuffer().readByte());
        }
        return res;
    }

    public static int readInt(Message message) {
        int res = 0;
        for (int i = 0; i < 4; i++) {
            res <<= 8;
            res |= (0xFF & message.getBuffer().readByte());
        }
        return res;
    }

    public static DateTime readDateTime(Message message) {
        int year = readInt(message);
        int mon = readInt(message);
        int day = readInt(message);

        int hour = readInt(message);
        int min = readInt(message);
        int sec = readInt(message);
        int milis = readInt(message);
        return new DateTime(year, mon, day, hour, min, sec, milis);
    }

    public static boolean readBoolean(Message message) {
        byte b = message.getBuffer().readByte();
        return (b == 1);
    }

    public static char readChar(Message message) {
        short s = readShort(message);
        return (char) s;
    }

    public static String readString(Message message) {
        int len = readShort(message);
        StringBuffer sb = new StringBuffer(len);
        for (int i = 0; i < len; i++) {
            sb.append(readChar(message));
        }
        return sb.toString();
    }

    public static String[] readArrayString(Message message) {
        int len = readShort(message);
        String st[] = new String[len];
        for (int i = 0; i < len; i++) {
            st[i] = readString(message);
        }
        return st;
    }

    public static int[] readArrayInt(Message message) {
        int len = readShort(message);
        int result[] = new int[len];
        for (int i = 0; i < len; i++) {
            result[i] = readInt(message);
        }
        return result;
    }

    public static void writeArrayInt(int arr[], Message message) {
        writeShort((short) arr.length, message);
        for (int j = 0; j < arr.length; j++) {
            writeInt(arr[j], message);
        }
    }

    private static void _writeShort(short j, Message message) {
        for (int i = 1; i >= 0; i--) {
            message.getBuffer().writeByte((byte) (j >> i * 8));
        }
    }

    public static void writeDateTime(DateTime dateTime, Message message) {
        writeInt(dateTime.getYear(), message);
        writeInt(dateTime.getMonth(), message);
        writeInt(dateTime.getDay(), message);

        writeInt(dateTime.getHour(), message);
        writeInt(dateTime.getMinute(), message);
        writeInt(dateTime.getSecond(), message);
        writeInt(dateTime.getMilisecond(), message);
    }

    public static void write(byte[] buffer, int offset, int len, Message message) {
        BufferData data = message.getBuffer();
        data.ensureAvail(len + 2);
        _writeShort((short) len, message);
        message.getBuffer().writeBytes(buffer, offset, len);
    }

    public static void writeByte(byte b, Message message) {
        BufferData data = message.getBuffer();
        data.ensureAvail(1);
        message.getBuffer().writeByte(b);
    }

    public static void writeBoolean(boolean b, Message message) {
        BufferData data = message.getBuffer();
        data.ensureAvail(1);
        if (b) {
            message.getBuffer().writeByte((byte) 1);
        } else {
            message.getBuffer().writeByte((byte) 0);
        }
    }

    public static void writeInt(int b, Message message) {
        BufferData data = message.getBuffer();
        data.ensureAvail(4);
        for (int i = 3; i >= 0; i--) {
            message.getBuffer().writeByte((byte) (b >> i * 8));
        }
    }

    public static void writeShort(short j, Message message) {
        BufferData data = message.getBuffer();
        data.ensureAvail(2);
        for (int i = 1; i >= 0; i--) {
            message.getBuffer().writeByte((byte) (j >> i * 8));
        }
    }

    public static void writeLong(long l, Message message) {
        BufferData data = message.getBuffer();
        data.ensureAvail(8);
        for (int i = 7; i >= 0; i--) {
            message.getBuffer().writeByte((byte) (l >> i * 8));
        }
    }

    public static void writeChar(char c, Message message) {
        short t = (short) c;
        writeShort(t, message);
    }

    public static void writeString(String s, Message message) {
        if (s == null)
            s = "";
        BufferData data = message.getBuffer();
        int len = s.length();
        data.ensureAvail(4 + 2 * len);
        _writeShort((short) len, message);
        for (int i = 0; i < len; i++) {
            short ts = (short) s.charAt(i);
            _writeShort(ts, message);
        }
    }

    public static void writeArrayString(String[] s, Message message) {
        writeShort((short) s.length, message);
        for (int i = 0; i < s.length; i++) {
            writeString(s[i], message);
        }
    }
}
