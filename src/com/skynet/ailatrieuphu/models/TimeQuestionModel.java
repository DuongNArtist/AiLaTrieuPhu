package com.skynet.ailatrieuphu.models;

public class TimeQuestionModel {

    private static TimeQuestionModel mInstance;
    private final static String regularExpression = ";-;";
    private final static int NUM_FIELD = 2;
    private String mId;
    private String mTime;

    public TimeQuestionModel() {
    }

    public static TimeQuestionModel getInstance() {
        if (mInstance == null) {
            mInstance = new TimeQuestionModel();
        }
        return mInstance;
    }

    public String toString() {
        String string = genString(mId) + regularExpression;
        string += genString(mTime);
        return string;
    }

    public boolean fromString(String source) {
        try {
            String[] arr = source.split(regularExpression);
            if (arr == null || arr.length != NUM_FIELD) {
                return false;
            }
            int idx = 0;
            mId = arr[idx++];
            mTime = arr[idx++];
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private String genString(String source) {
        String resource = source;
        if (resource == null || "".equals(resource)) {
            resource = " ";
        }
        return resource;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        this.mTime = time;
    }

    public String getId() {
        return mId;
    }

    public void setmId(String id) {
        this.mId = id;
    }

}
