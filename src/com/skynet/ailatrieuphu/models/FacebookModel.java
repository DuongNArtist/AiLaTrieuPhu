package com.skynet.ailatrieuphu.models;

public class FacebookModel {

    private static FacebookModel mInstance;

    private String mId;
    private String mToken;
    private String mEmail;
    private String mName;
    private String mBirthDay;
    private String mAddress;

    private FacebookModel() {

    }

    public static FacebookModel getInstance() {
        if (mInstance == null) {
            mInstance = new FacebookModel();
        }
        return mInstance;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        this.mToken = token;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getBirthday() {
        return mBirthDay;
    }

    public void setBirthday(String birthday) {
        this.mBirthDay = birthday;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        this.mAddress = address;
    }

}
