package com.skynet.ailatrieuphu.preferences;

import android.content.Context;

public class PreferenceManager {

    static final String IS_FIRST_RUN_APP = "IS_FIRST_RUN_APP";
    static final String KEY_PURCHASE = "KEY_PURCHASE";
    static final String KEY_USERNAME = "USER_MODEL";
    static final String KEY_APP_LANGUAGE = "KEY_APP_LANGUAGE";
    static final String KEY_LINK = "KEY_LINK";
    static final String KEY_QUESTION = "KEY_QUESTION";

    private static PreferenceManager mInstance;
    private PreferenceHelper mPreferenceHelper;

    private PreferenceManager(Context context) {
        mPreferenceHelper = PreferenceHelper.getInstance(context);
    }

    public static PreferenceManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new PreferenceManager(context);
        }
        return mInstance;
    }

    public void clear() {
        mPreferenceHelper.clear();
    }

    public void setFirstRunApp(boolean run) {
        mPreferenceHelper.putBoolean(IS_FIRST_RUN_APP, run);
    }

    public boolean isFirstRunApp() {
        return mPreferenceHelper.getBoolean(IS_FIRST_RUN_APP, true);
    }

    public void setListPurchase(String data) {
        mPreferenceHelper.putString(KEY_PURCHASE, data);
    }

    public String getListPurchase() {
        return mPreferenceHelper.getString(KEY_PURCHASE, "");
    }

    public void setAppLanguage(String language) {
        mPreferenceHelper.putString(KEY_APP_LANGUAGE, language);
    }

    public String getAppLanguage() {
        return mPreferenceHelper.getString(KEY_APP_LANGUAGE, "vi");
    }

    public void setUser(String data) {
        mPreferenceHelper.putString(KEY_USERNAME, data);
    }

    public String getUser() {
        return mPreferenceHelper.getString(KEY_USERNAME, "");
    }

    public void setQuestion(String question) {
        mPreferenceHelper.putString(KEY_QUESTION, question);
    }

    public String getQuestion() {
        return mPreferenceHelper.getString(KEY_QUESTION, "");
    }

    public void setLink(String link) {
        mPreferenceHelper.putString(KEY_LINK, link);
    }

    public String getLink() {
        return mPreferenceHelper.getString(KEY_LINK, "");
    }
}
