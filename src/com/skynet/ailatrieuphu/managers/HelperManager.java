package com.skynet.ailatrieuphu.managers;

import android.util.SparseBooleanArray;

public class HelperManager {

    private SparseBooleanArray mSparseBooleanArray;

    public HelperManager() {
        mSparseBooleanArray = new SparseBooleanArray();
        resetLife();
    }

    public void resetLife() {
        for (int index = 0; index < 4; index++) {
            putLife(index, true);
        }
    }

    public void putLife(int index, boolean available) {
        mSparseBooleanArray.put(index, available);
    }

    public boolean isAvailable(int index) {
        return mSparseBooleanArray.get(index);
    }

    public void usedLife(int index) {
        if (isAvailable(index)) {
            putLife(index, true);
        }
    }
}
