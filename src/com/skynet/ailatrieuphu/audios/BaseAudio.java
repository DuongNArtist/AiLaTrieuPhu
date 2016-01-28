package com.skynet.ailatrieuphu.audios;

public abstract class BaseAudio implements IAudio {
    private final IAudioManager<? extends IAudio> mAudioManager;

    protected float mLeftVolume = 1.0f;
    protected float mRightVolume = 1.0f;

    private boolean mReleased;

    public BaseAudio(final IAudioManager<? extends IAudio> pAudioManager) {
        this.mAudioManager = pAudioManager;
    }

    public boolean isReleased() {
        return this.mReleased;
    }

    protected IAudioManager<? extends IAudio> getAudioManager() {
        return this.mAudioManager;
    }

    public float getActualLeftVolume() {
        return this.mLeftVolume * this.getMasterVolume();
    }

    public float getActualRightVolume() {
        return this.mRightVolume * this.getMasterVolume();
    }

    protected float getMasterVolume() {
        return this.mAudioManager.getMasterVolume();
    }

    @Override
    public float getVolume() {
        return (this.mLeftVolume + this.mRightVolume) * 0.5f;
    }

    @Override
    public float getLeftVolume() {
        return this.mLeftVolume;
    }

    @Override
    public float getRightVolume() {
        return this.mRightVolume;
    }

    @Override
    public final void setVolume(final float pVolume) {
        this.setVolume(pVolume, pVolume);
    }

    @Override
    public void setVolume(final float pLeftVolume, final float pRightVolume) {
        this.mLeftVolume = pLeftVolume;
        this.mRightVolume = pRightVolume;
    }

    @Override
    public void release() {
        this.mReleased = true;
    }

}
