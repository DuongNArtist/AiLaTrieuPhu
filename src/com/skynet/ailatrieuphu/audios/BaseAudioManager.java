package com.skynet.ailatrieuphu.audios;

import java.util.ArrayList;

public abstract class BaseAudioManager<T extends IAudio> implements
        IAudioManager<T> {

    protected final ArrayList<T> mAudioEntities = new ArrayList<T>();

    protected float mMasterVolume = 1.0f;

    @Override
    public float getMasterVolume() {
        return this.mMasterVolume;
    }

    @Override
    public void setMasterVolume(final float pMasterVolume) {
        this.mMasterVolume = pMasterVolume;
        final ArrayList<T> audioEntities = this.mAudioEntities;
        for (int i = audioEntities.size() - 1; i >= 0; i--) {
            final T audioEntity = audioEntities.get(i);
            audioEntity.onMasterVolumeChanged(pMasterVolume);
        }
    }

    @Override
    public void add(final T pAudioEntity) {
        this.mAudioEntities.add(pAudioEntity);
    }

    @Override
    public boolean remove(final T pAudioEntity) {
        return this.mAudioEntities.remove(pAudioEntity);
    }

    @Override
    public void releaseAll() {
        final ArrayList<T> audioEntities = this.mAudioEntities;
        for (int i = audioEntities.size() - 1; i >= 0; i--) {
            final T audioEntity = audioEntities.get(i);
            audioEntity.stop();
            audioEntity.release();
        }
    }
}
