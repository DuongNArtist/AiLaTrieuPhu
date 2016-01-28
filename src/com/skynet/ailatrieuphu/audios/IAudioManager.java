package com.skynet.ailatrieuphu.audios;

public interface IAudioManager<T extends IAudio> {

    public float getMasterVolume();

    public void setMasterVolume(final float pMasterVolume);

    public void add(final T pAudioEntity);

    public boolean remove(final T pAudioEntity);

    public void releaseAll();
}