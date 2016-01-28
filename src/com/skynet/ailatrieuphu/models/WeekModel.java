package com.skynet.ailatrieuphu.models;

public class WeekModel {
    private int mId;
    private String mName;
    private boolean mEnabled;
    private boolean mSelected;
    private WeekModel mWeekModel;

    public WeekModel getmWeekModel() {
        return mWeekModel;
    }

    public void setmWeekModel(WeekModel mWeekModel) {
        this.mWeekModel = mWeekModel;
    }

    public WeekModel() {

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

    public boolean isEnabled() {
        return mEnabled;
    }

    public void setEnabled(boolean enabled) {
        this.mEnabled = enabled;
    }

    public boolean isSelected() {
        return mSelected;
    }

    public void setSelected(boolean selected) {
        this.mSelected = selected;
    }

}
