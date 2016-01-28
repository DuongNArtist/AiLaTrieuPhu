package com.skynet.ailatrieuphu.models;

public class QuestionModel {

    private int mId;
    private int mCategoryId;
    private int mAnswer;
    private String mQuestion;
    private String[] mOptions;
    private String mLink;
    private int mLevel;
    private String mNamePlayer;
    private String mAvatar;
    private boolean mAnswerPlayer;

    public String getNamePlayer() {
        return mNamePlayer;
    }

    public void setNamePlayer(String mNamePlayer) {
        this.mNamePlayer = mNamePlayer;
    }

    public String getAvatarPlayer() {
        return mAvatar;
    }

    public void setAvatarPlayer(String mAvatar) {
        this.mAvatar = mAvatar;
    }

    public boolean isAnswerPlayer() {
        return mAnswerPlayer;
    }

    public void setAnswerPlayer(boolean mAnswerPlayer) {
        this.mAnswerPlayer = mAnswerPlayer;
    }

    public QuestionModel() {
        mOptions = new String[4];
    }

    public String[] getOptions() {
        return mOptions;
    }

    public void setOption(int index, String option) {
        if (index < 4 && index > -1) {
            mOptions[index] = option;
        }
    }

    public String getOption(int index) {
        if (index < 4 && index > -1) {
            return mOptions[index];
        }
        return null;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public int getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(int categoryId) {
        this.mCategoryId = categoryId;
    }

    public int getAnswer() {
        return mAnswer;
    }

    public void setAnswer(int answer) {
        this.mAnswer = answer;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public void setQuestion(String question) {
        this.mQuestion = question;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        this.mLink = link;
    }

    public int getLevel() {
        return mLevel;
    }

    public void setLevel(int level) {
        this.mLevel = level;
    }
}
