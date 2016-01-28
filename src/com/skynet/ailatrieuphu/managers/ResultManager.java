package com.skynet.ailatrieuphu.managers;

import java.util.ArrayList;

import android.util.Log;

import com.skynet.ailatrieuphu.models.ResultModel;

public class ResultManager {

    public static final String TAG = ResultManager.class.getSimpleName();

    private long mId;
    private int mIdWeek;
    private ArrayList<ResultModel> mResultModels;
    private ResultModel mResultModel;

    public ResultManager() {

    }

    public void resetResult() {
        mIdWeek = 0;
        mId = 0;
        mResultModels = new ArrayList<ResultModel>();
    }

    public void addResult(int id, int level, int time, boolean right) {
        ResultModel resultModel = new ResultModel();
        resultModel.setId(id);
        resultModel.setLevel(level);
        resultModel.setTime(time);
        resultModel.setRight(right);
        mResultModels.add(resultModel);
    }

    public void addResultWeek(int idWeek, int id, int level, int time,
            boolean right) {
        ResultModel resultModel = new ResultModel();
        resultModel.setIdWeek(idWeek);
        resultModel.setId(id);
        resultModel.setLevel(level);
        resultModel.setTime(time);
        resultModel.setRight(right);
        mResultModels.add(resultModel);
    }

    public void addResulVtv3Direct(int id, int level, int time, boolean right) {
        mResultModel = new ResultModel();
        mResultModel.setId(id);
        mResultModel.setLevel(level);
        mResultModel.setTime(time);
        mResultModel.setRight(right);
        Log.i(TAG, "" + mResultModel.getId());
    }

    public ResultModel getResultModel() {
        return mResultModel;
    }

    public ArrayList<ResultModel> getResultModels() {
        return mResultModels;
    }

    public int getSize() {
        return mResultModels.size();
    }

    public int getIdWeek() {
        return mIdWeek;
    }

    public void setIdWeek(int id) {
        this.mIdWeek = id;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }
}
