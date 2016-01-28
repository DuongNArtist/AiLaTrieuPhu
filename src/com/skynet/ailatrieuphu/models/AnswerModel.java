package com.skynet.ailatrieuphu.models;

public class AnswerModel {

    private int mId;
    private int mLevel;
    private int mResult;
    private int mTime;

    public AnswerModel() {

    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public int getLevel() {
        return mLevel;
    }

    public void setLevel(int level) {
        this.mLevel = level;
    }

    public int getResult() {
        return mResult;
    }

    public void setResult(int result) {
        this.mResult = result;
    }

    public int getTime() {
        return mTime;
    }

    public void setTime(int time) {
        this.mTime = time;
    }

}
