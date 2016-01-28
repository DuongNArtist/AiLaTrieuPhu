package com.skynet.ailatrieuphu.audios;

import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.util.SparseArray;

public class SoundManager extends BaseAudioManager<Sound> implements
        OnLoadCompleteListener {

    private static final int SOUND_STATUS_OK = 0;
    public static final int MAX_SIMULTANEOUS_STREAMS_DEFAULT = 5;

    private final SoundPool mSoundPool;
    private final SparseArray<Sound> mSoundMap = new SparseArray<Sound>();

    public SoundManager() {
        this(MAX_SIMULTANEOUS_STREAMS_DEFAULT);
    }

    @SuppressWarnings("deprecation")
    public SoundManager(final int pMaxSimultaneousStreams) {
        this.mSoundPool = new SoundPool(pMaxSimultaneousStreams,
                AudioManager.STREAM_MUSIC, 0);
        this.mSoundPool.setOnLoadCompleteListener(this);
    }

    SoundPool getSoundPool() {
        return this.mSoundPool;
    }

    @Override
    public void add(final Sound pSound) {
        super.add(pSound);

        this.mSoundMap.put(pSound.getSoundID(), pSound);
    }

    @Override
    public boolean remove(final Sound pSound) {
        final boolean removed = super.remove(pSound);
        if (removed) {
            this.mSoundMap.remove(pSound.getSoundID());
        }
        return removed;
    }

    @Override
    public void releaseAll() {
        super.releaseAll();
        this.mSoundPool.release();
    }

    @Override
    public synchronized void onLoadComplete(final SoundPool pSoundPool,
            final int pSoundID, final int pStatus) {
        if (pStatus == SoundManager.SOUND_STATUS_OK) {
            final Sound sound = this.mSoundMap.get(pSoundID);
            if (sound != null) {
                sound.setLoaded(true);
            }
        }
    }
}
