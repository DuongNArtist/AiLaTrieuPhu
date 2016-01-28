package com.skynet.ailatrieuphu.audios;

import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class Music extends BaseAudio {

    private MediaPlayer mMediaPlayer;

    Music(final MusicManager pMusicManager, final MediaPlayer pMediaPlayer) {
        super(pMusicManager);
        this.mMediaPlayer = pMediaPlayer;
    }

    public boolean isPlaying() {
        if (mMediaPlayer == null) {
            return false;
        }
        return this.mMediaPlayer.isPlaying();
    }

    public MediaPlayer getMediaPlayer() {
        return this.mMediaPlayer;
    }

    @Override
    protected MusicManager getAudioManager() {
        return (MusicManager) super.getAudioManager();
    }

    @Override
    public void play() {
        if (mMediaPlayer == null) {
            return;
        }
        mMediaPlayer.start();
    }

    @Override
    public void stop() {
        if (mMediaPlayer == null) {
            return;
        }
        if (mMediaPlayer.isPlaying()) {
            try {
                this.mMediaPlayer.stop();
                this.mMediaPlayer.prepare();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void resume() {
        if (mMediaPlayer == null) {
            return;
        }
        this.mMediaPlayer.start();
    }

    @Override
    public void pause() {
        if (mMediaPlayer == null) {
            return;
        }
        this.mMediaPlayer.pause();
        this.mMediaPlayer.seekTo(0);
    }

    @Override
    public void setLooping(final boolean pLooping) {
        if (mMediaPlayer == null) {
            return;
        }
        this.mMediaPlayer.setLooping(pLooping);
    }

    @Override
    public void setVolume(final float pLeftVolume, final float pRightVolume) {
        super.setVolume(pLeftVolume, pRightVolume);
        if (mMediaPlayer == null) {
            return;
        }
        final float masterVolume = this.getAudioManager().getMasterVolume();
        final float actualLeftVolume = pLeftVolume * masterVolume;
        final float actualRightVolume = pRightVolume * masterVolume;
        this.mMediaPlayer.setVolume(actualLeftVolume, actualRightVolume);
    }

    @Override
    public void onMasterVolumeChanged(final float pMasterVolume) {
        this.setVolume(this.mLeftVolume, this.mRightVolume);
    }

    @Override
    public void release() {
        if (mMediaPlayer == null) {
            return;
        }
        this.mMediaPlayer.release();
        this.mMediaPlayer = null;
        this.getAudioManager().remove(this);
        super.release();
    }

    public void seekTo(final int pMilliseconds) {
        if (mMediaPlayer == null) {
            return;
        }
        this.mMediaPlayer.seekTo(pMilliseconds);
    }

    public void setOnCompletionListener(
            final OnCompletionListener pOnCompletionListener) {
        if (mMediaPlayer == null) {
            return;
        }
        this.mMediaPlayer.setOnCompletionListener(pOnCompletionListener);
    }

    public int getDuration() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getDuration();
        }
        return -1;
    }

    public int getCurrent() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getCurrentPosition();
        }
        return -1;
    }

}
