package com.skynet.ailatrieuphu.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHelper {

    public static final String SP_NAME = "SP_NAME";
    private static PreferenceHelper mInstance;
    private SharedPreferences mSharedPreferences;

    private PreferenceHelper(Context context) {
        mSharedPreferences = context.getSharedPreferences(SP_NAME,
                Context.MODE_PRIVATE);
    }

    public static PreferenceHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new PreferenceHelper(context);
        }
        return mInstance;
    }

    public PreferenceHelper putInt(String key, int value) {
        mSharedPreferences.edit().putInt(key, value).commit();
        return mInstance;
    }

    public int getInt(String key, int defaultValue) {
        return mSharedPreferences.getInt(key, defaultValue);
    }

    public PreferenceHelper putLong(String key, long value) {
        mSharedPreferences.edit().putLong(key, value).commit();
        return mInstance;
    }

    public long getLong(String key, long defaultValue) {
        return mSharedPreferences.getLong(key, defaultValue);
    }

    public PreferenceHelper putString(String key, String value) {
        mSharedPreferences.edit().putString(key, value).commit();
        return mInstance;
    }

    public String getString(String key, String defaultValue) {
        return mSharedPreferences.getString(key, defaultValue);
    }

    public PreferenceHelper putBoolean(String key, boolean value) {
        mSharedPreferences.edit().putBoolean(key, value).commit();
        return mInstance;
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return mSharedPreferences.getBoolean(key, defaultValue);
    }

    public PreferenceHelper putFloat(String key, float value) {
        mSharedPreferences.edit().putFloat(key, value).commit();
        return mInstance;
    }

    public float getFloat(String key, float defaultValue) {
        return mSharedPreferences.getFloat(key, defaultValue);
    }

    public void clear() {
        mSharedPreferences.edit().clear().commit();
    }

}
