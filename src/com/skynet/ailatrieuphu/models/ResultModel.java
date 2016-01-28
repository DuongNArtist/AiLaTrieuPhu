package com.skynet.ailatrieuphu.models;

public class ResultModel {

    private int mIdWeek;
    private int mId;
    private int mLevel;
    private int mTime;
    private boolean mRight;

    public ResultModel() {

    }

    public int getIdWeek() {
        return mIdWeek;
    }

    public void setIdWeek(int idWeek) {
        this.mIdWeek = idWeek;
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

    public int getTime() {
        return mTime;
    }

    public void setTime(int time) {
        this.mTime = time;
    }

    public boolean isRight() {
        return mRight;
    }

    public void setRight(boolean right) {
        this.mRight = right;
    }

}
