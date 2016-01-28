package com.skynet.ailatrieuphu.audios;

import android.media.SoundPool;

public class Sound extends BaseAudio {

    private int mSoundID;
    private int mStreamID;
    private boolean mLoaded;
    private int mLoopCount;
    private float mRate = 1.0f;

    Sound(final SoundManager pSoundManager, final int pSoundID) {
        super(pSoundManager);
        this.mSoundID = pSoundID;
    }

    public int getSoundID() {
        return this.mSoundID;
    }

    public int getStreamID() {
        return this.mStreamID;
    }

    public boolean isLoaded() {
        return this.mLoaded;
    }

    public void setLoaded(final boolean pLoaded) {
        this.mLoaded = pLoaded;
    }

    public void setLoopCount(final int pLoopCount) {
        this.mLoopCount = pLoopCount;
        if (this.mStreamID != 0) {
            this.getSoundPool().setLoop(this.mStreamID, pLoopCount);
        }
    }

    public float getRate() {
        return this.mRate;
    }

    public void setRate(final float pRate) {
        this.mRate = pRate;
        if (this.mStreamID != 0) {
            this.getSoundPool().setRate(this.mStreamID, pRate);
        }
    }

    private SoundPool getSoundPool() {
        return this.getAudioManager().getSoundPool();
    }

    @Override
    protected SoundManager getAudioManager() {
        return (SoundManager) super.getAudioManager();
    }

    @Override
    public void play() {
        final float masterVolume = this.getMasterVolume();
        final float leftVolume = this.mLeftVolume * masterVolume;
        final float rightVolume = this.mRightVolume * masterVolume;

        this.mStreamID = this.getSoundPool().play(this.mSoundID, leftVolume,
                rightVolume, 1, this.mLoopCount, this.mRate);
    }

    @Override
    public void stop() {
        if (this.mStreamID != 0) {
            this.getSoundPool().stop(this.mStreamID);
        }
    }

    @Override
    public void resume() {
        if (this.mStreamID != 0) {
            this.getSoundPool().resume(this.mStreamID);
        }
    }

    @Override
    public void pause() {
        if (this.mStreamID != 0) {
            this.getSoundPool().pause(this.mStreamID);
        }
    }

    @Override
    public void release() {
        this.getSoundPool().unload(this.mSoundID);
        this.mSoundID = 0;
        this.mLoaded = false;

        this.getAudioManager().remove(this);

        super.release();
    }

    @Override
    public void setLooping(final boolean pLooping) {
        this.setLoopCount((pLooping) ? -1 : 0);
    }

    @Override
    public void setVolume(final float pLeftVolume, final float pRightVolume) {
        super.setVolume(pLeftVolume, pRightVolume);
        if (this.mStreamID != 0) {
            final float masterVolume = this.getMasterVolume();
            final float leftVolume = this.mLeftVolume * masterVolume;
            final float rightVolume = this.mRightVolume * masterVolume;
            this.getSoundPool().setVolume(this.mStreamID, leftVolume,
                    rightVolume);
        }
    }

    @Override
    public void onMasterVolumeChanged(final float pMasterVolume) {
        this.setVolume(this.mLeftVolume, this.mRightVolume);
    }

}
