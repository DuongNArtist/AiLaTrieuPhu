package com.skynet.ailatrieuphu.models;


public class RankModel {

    private String mName;
    private String mLink;
    private long mScore;

    public RankModel() {

    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        mLink = link;
    }

    public long getScore() {
        return mScore;
    }

    public void setScore(long score) {
        mScore = score;
    }

}
