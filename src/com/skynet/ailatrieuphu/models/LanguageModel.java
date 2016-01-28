package com.skynet.ailatrieuphu.models;

public class LanguageModel {
    private int mId;
    private String mName;
    private String mLanguage;

    public LanguageModel() {

    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public void setLanguage(String language) {
        this.mLanguage = language;
    }

}
