package com.skynet.ailatrieuphu.audios;

import android.app.Activity;

public class AudioManager {

    public static int mSoundId = -1;
    private static int mCount = 0;
    public final static int AND_COUNT = mCount++;
    public final static int AND_IMPORTANT = mCount++;
    public final static int AND_PASS = mCount++;
    public final static int FAIL_A_EN = mCount++;
    public final static int FAIL_A_VI = mCount++;
    public final static int FAIL_B_EN = mCount++;
    public final static int FAIL_B_VI = mCount++;
    public final static int FAIL_C_EN = mCount++;
    public final static int FAIL_C_VI = mCount++;
    public final static int FAIL_D_EN = mCount++;
    public final static int FAIL_D_VI = mCount++;
    public final static int HELPER_50_EN = mCount++;
    public final static int HELPER_50_VI = mCount++;
    public final static int HELPER_AUDIENCE_EN = mCount++;
    public final static int HELPER_AUDIENCE_VI = mCount++;
    public final static int HELPER_BUDDY_EN = mCount++;
    public final static int HELPER_BUDDY_VI = mCount++;
    public final static int INTRO = mCount++;
    public final static int OUTRO = mCount++;
    public final static int QUESTION_00_EN = mCount++;
    public final static int QUESTION_00_VI = mCount++;
    public final static int QUESTION_01_EN = mCount++;
    public final static int QUESTION_01_VI = mCount++;
    public final static int QUESTION_02_EN = mCount++;
    public final static int QUESTION_02_VI = mCount++;
    public final static int QUESTION_03_EN = mCount++;
    public final static int QUESTION_03_VI = mCount++;
    public final static int QUESTION_04_EN = mCount++;
    public final static int QUESTION_04_VI = mCount++;
    public final static int QUESTION_05_EN = mCount++;
    public final static int QUESTION_05_VI = mCount++;
    public final static int QUESTION_06_EN = mCount++;
    public final static int QUESTION_06_VI = mCount++;
    public final static int QUESTION_07_EN = mCount++;
    public final static int QUESTION_07_VI = mCount++;
    public final static int QUESTION_08_EN = mCount++;
    public final static int QUESTION_08_VI = mCount++;
    public final static int QUESTION_09_EN = mCount++;
    public final static int QUESTION_09_VI = mCount++;
    public final static int QUESTION_10_EN = mCount++;
    public final static int QUESTION_10_VI = mCount++;
    public final static int QUESTION_11_EN = mCount++;
    public final static int QUESTION_11_VI = mCount++;
    public final static int QUESTION_12_EN = mCount++;
    public final static int QUESTION_12_VI = mCount++;
    public final static int QUESTION_13_EN = mCount++;
    public final static int QUESTION_13_VI = mCount++;
    public final static int QUESTION_14_EN = mCount++;
    public final static int QUESTION_14_VI = mCount++;
    public final static int RIGHT_A_EN = mCount++;
    public final static int RIGHT_A_VI = mCount++;
    public final static int RIGHT_B_EN = mCount++;
    public final static int RIGHT_B_VI = mCount++;
    public final static int RIGHT_C_EN = mCount++;
    public final static int RIGHT_C_VI = mCount++;
    public final static int RIGHT_D_EN = mCount++;
    public final static int RIGHT_D_VI = mCount++;
    public final static int RIGHT_EN = mCount++;
    public final static int RIGHT_VI = mCount++;
    public final static int RULE_EN = mCount++;
    public final static int RULE_VI = mCount++;
    public final static int START_EN = mCount++;
    public final static int START_VI = mCount++;
    public final static int THANKS_EN = mCount++;
    public final static int THANKS_VI = mCount++;
    public final static int TIME_OUT = mCount++;
    public final static int TOUCH = mCount++;
    public final static int VICTORY_EN = mCount++;
    public final static int VICTORY_VI = mCount++;
    public final static int WAIT_EN = mCount++;
    public final static int WAIT_VI = mCount++;
    public final static int FAIL_ANSWER = mCount++;
    public final static int RIGHT_ANSWER = mCount++;

    private static AudioManager mInstance;
    private Activity mContext;
    private MusicManager mMusicManager;
    private SoundManager mSoundManager;

    private AudioManager() {

    }

    public static AudioManager getInstance() {
        if (mInstance == null) {
            mInstance = new AudioManager();
        }
        return mInstance;
    }

    public static MySound soundsArray[] = {
            new MySound("mfx/and_count.mp3", MySound.BG_MUSIC),
            new MySound("mfx/and_important.mp3", MySound.BG_MUSIC),
            new MySound("mfx/and_pass.mp3", MySound.BG_MUSIC),
            new MySound("mfx/fail_a_en.mp3", MySound.BG_MUSIC),
            new MySound("mfx/fail_a_vi.mp3", MySound.BG_MUSIC),
            new MySound("mfx/fail_b_en.mp3", MySound.BG_MUSIC),
            new MySound("mfx/fail_b_vi.mp3", MySound.BG_MUSIC),
            new MySound("mfx/fail_c_en.mp3", MySound.BG_MUSIC),
            new MySound("mfx/fail_c_vi.mp3", MySound.BG_MUSIC),
            new MySound("mfx/fail_d_en.mp3", MySound.BG_MUSIC),
            new MySound("mfx/fail_d_vi.mp3", MySound.BG_MUSIC),
            new MySound("mfx/helper_50_en.mp3", MySound.BG_MUSIC),
            new MySound("mfx/helper_50_vi.mp3", MySound.BG_MUSIC),
            new MySound("mfx/helper_audience_en.mp3", MySound.BG_MUSIC),
            new MySound("mfx/helper_audience_vi.mp3", MySound.BG_MUSIC),
            new MySound("mfx/helper_buddy_en.mp3", MySound.BG_MUSIC),
            new MySound("mfx/helper_buddy_vi.mp3", MySound.BG_MUSIC),
            new MySound("mfx/intro.mp3", MySound.BG_MUSIC),
            new MySound("mfx/outro.mp3", MySound.BG_MUSIC),
            new MySound("mfx/question_00_en.mp3", MySound.BG_MUSIC),
            new MySound("mfx/question_00_vi.mp3", MySound.BG_MUSIC),
            new MySound("mfx/question_01_en.mp3", MySound.BG_MUSIC),
            new MySound("mfx/question_01_vi.mp3", MySound.BG_MUSIC),
            new MySound("mfx/question_02_en.mp3", MySound.BG_MUSIC),
            new MySound("mfx/question_02_vi.mp3", MySound.BG_MUSIC),
            new MySound("mfx/question_03_en.mp3", MySound.BG_MUSIC),
            new MySound("mfx/question_03_vi.mp3", MySound.BG_MUSIC),
            new MySound("mfx/question_04_en.mp3", MySound.BG_MUSIC),
            new MySound("mfx/question_04_vi.mp3", MySound.BG_MUSIC),
            new MySound("mfx/question_05_en.mp3", MySound.BG_MUSIC),
            new MySound("mfx/question_05_vi.mp3", MySound.BG_MUSIC),
            new MySound("mfx/question_06_en.mp3", MySound.BG_MUSIC),
            new MySound("mfx/question_06_vi.mp3", MySound.BG_MUSIC),
            new MySound("mfx/question_07_en.mp3", MySound.BG_MUSIC),
            new MySound("mfx/question_07_vi.mp3", MySound.BG_MUSIC),
            new MySound("mfx/question_08_en.mp3", MySound.BG_MUSIC),
            new MySound("mfx/question_08_vi.mp3", MySound.BG_MUSIC),
            new MySound("mfx/question_09_en.mp3", MySound.BG_MUSIC),
            new MySound("mfx/question_09_vi.mp3", MySound.BG_MUSIC),
            new MySound("mfx/question_10_en.mp3", MySound.BG_MUSIC),
            new MySound("mfx/question_10_vi.mp3", MySound.BG_MUSIC),
            new MySound("mfx/question_11_en.mp3", MySound.BG_MUSIC),
            new MySound("mfx/question_11_vi.mp3", MySound.BG_MUSIC),
            new MySound("mfx/question_12_en.mp3", MySound.BG_MUSIC),
            new MySound("mfx/question_12_vi.mp3", MySound.BG_MUSIC),
            new MySound("mfx/question_13_en.mp3", MySound.BG_MUSIC),
            new MySound("mfx/question_13_vi.mp3", MySound.BG_MUSIC),
            new MySound("mfx/question_14_en.mp3", MySound.BG_MUSIC),
            new MySound("mfx/question_14_vi.mp3", MySound.BG_MUSIC),
            new MySound("mfx/right_a_en.mp3", MySound.BG_MUSIC),
            new MySound("mfx/right_a_vi.mp3", MySound.BG_MUSIC),
            new MySound("mfx/right_b_en.mp3", MySound.BG_MUSIC),
            new MySound("mfx/right_b_vi.mp3", MySound.BG_MUSIC),
            new MySound("mfx/right_c_en.mp3", MySound.BG_MUSIC),
            new MySound("mfx/right_c_vi.mp3", MySound.BG_MUSIC),
            new MySound("mfx/right_d_en.mp3", MySound.BG_MUSIC),
            new MySound("mfx/right_d_vi.mp3", MySound.BG_MUSIC),
            new MySound("mfx/right_en.mp3", MySound.BG_MUSIC),
            new MySound("mfx/right_vi.mp3", MySound.BG_MUSIC),
            new MySound("mfx/rule_en.mp3", MySound.BG_MUSIC),
            new MySound("mfx/rule_vi.mp3", MySound.BG_MUSIC),
            new MySound("mfx/start_en.mp3", MySound.BG_MUSIC),
            new MySound("mfx/start_vi.mp3", MySound.BG_MUSIC),
            new MySound("mfx/thanks_en.mp3", MySound.BG_MUSIC),
            new MySound("mfx/thanks_vi.mp3", MySound.BG_MUSIC),
            new MySound("mfx/time_out.mp3", MySound.BG_MUSIC),
            new MySound("mfx/touch.mp3", MySound.BG_MUSIC),
            new MySound("mfx/victory_en.mp3", MySound.BG_MUSIC),
            new MySound("mfx/victory_vi.mp3", MySound.BG_MUSIC),
            new MySound("mfx/wait_en.mp3", MySound.BG_MUSIC),
            new MySound("mfx/wait_vi.mp3", MySound.BG_MUSIC),
            new MySound("mfx/fail.mp3", MySound.BG_MUSIC),
            new MySound("mfx/right.mp3", MySound.BG_MUSIC) };

    public void init(Activity activity) {
        mContext = activity;
        if (mMusicManager == null) {
            mMusicManager = new MusicManager();
        }
        if (mSoundManager == null) {
            mSoundManager = new SoundManager();
        }
    }

    public void loadAllSounds() {
        for (int i = 0; i < soundsArray.length; i++) {
            soundsArray[i].loadSound(mContext, mMusicManager, mSoundManager);
        }
    }

    public void playSound(int sound, boolean isLoop) {
        if (sound < 0 || sound >= soundsArray.length) {
            return;
        }
        mSoundId = sound;
        soundsArray[sound].playSound(isLoop);
    }

    public void pauseSound(int sound) {
        if (sound < 0 || sound >= soundsArray.length) {
            return;
        }
        soundsArray[sound].pauseSound();
    }

    public void stopSound(int sound) {
        if (sound < 0 || sound >= soundsArray.length) {
            return;
        }
        soundsArray[sound].stopSound();
    }

    public void stopSoundAll() {
        for (int i = 0; i < soundsArray.length; i++) {
            soundsArray[i].stopSound();
        }
    }

    public void pauseSoundAll() {
        for (int i = 0; i < soundsArray.length; i++) {
            soundsArray[i].pauseSound();
        }
    }

    public void resumeAllSound() {
        for (int i = 0; i < soundsArray.length; i++) {
            soundsArray[i].resumeSound();
        }
    }

    public void resumeSound(int sound) {
        if (sound < 0 || sound >= soundsArray.length) {
            return;
        }
        soundsArray[sound].resumeSound();
    }

    public void releaseSound(int sound) {
        if (sound < 0 || sound >= soundsArray.length) {
            return;
        }
        soundsArray[sound].releaseSound();
    }

    public void releaseAllSound() {
        for (int i = 0; i < soundsArray.length; i++) {
            soundsArray[i].releaseSound();
        }
    }

    public void setValueSound(float value) {
        for (int i = 0; i < soundsArray.length; i++) {
            if (soundsArray[i].getType() == MySound.BG_MUSIC) {
                soundsArray[i].getMusic().setVolume(value);
            }
        }
    }

    public void setValueSFX(float value) {
        for (int i = 0; i < soundsArray.length; i++) {
            if (soundsArray[i].getType() == MySound.EFFECT_MUSIC) {
                soundsArray[i].getSound().setVolume(value);
            }
        }
    }

    public Sound getSound(int sound) {
        if (soundsArray[sound].getType() == MySound.EFFECT_MUSIC) {
            return soundsArray[sound].getSound();
        }
        return null;
    }

    public Music getMusic(int sound) {
        if (soundsArray[sound].getType() == MySound.BG_MUSIC) {
            return soundsArray[sound].getMusic();
        }
        return null;
    }

}
