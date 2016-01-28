package com.skynet.ailatrieuphu.audios;

import java.io.IOException;

import android.content.Context;
import android.util.Log;

public class MySound {

    public static final int BG_MUSIC = 0;
    public static final int EFFECT_MUSIC = 1;

    private String path;
    private int type;
    private Sound sound;
    private Music music;

    public MySound(String path, int type) {
        this.path = path;
        this.type = type;
        this.sound = null;
        this.music = null;
    }

    public void loadSoundFromUrl(MusicManager musicManager, String path) {
        if (type == BG_MUSIC) {
            try {
                music = MusicFactory.createMusicFromUrl(musicManager, path);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadSound(Context context, MusicManager mm, SoundManager sm) {
        if (type == BG_MUSIC) {
            try {
                music = MusicFactory.createMusicFromAsset(mm, context, path);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (type == EFFECT_MUSIC) {
            try {
                sound = SoundFactory.createSoundFromAsset(sm, context, path);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void releaseSound() {
        if (type == BG_MUSIC) {
            if (music != null) {
                music.stop();
                music.release();
                music = null;
            }
        } else if (type == EFFECT_MUSIC) {
            if (sound != null) {
                sound.stop();
                sound.release();
                sound = null;
            }
        }
    }

    public void playSound(boolean isLoop) {
        if (type == BG_MUSIC) {
            if (music == null) {
                return;
            }
            try {
                music.seekTo(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            music.setLooping(isLoop);
            music.play();
        } else if (type == EFFECT_MUSIC) {
            if (sound == null) {
                return;
            }
            sound.setLooping(isLoop);
            sound.play();
            Log.d("PlaySound", "Yes");
        }
    }

    public void pauseSound() {
        if (type == BG_MUSIC) {
            if (music != null && music.isPlaying()) {
                music.pause();
            }
        } else if (type == EFFECT_MUSIC) {
            if (sound != null && sound.isLoaded()) {
                sound.pause();
            }
        }
    }

    public void stopSound() {
        if (type == BG_MUSIC) {
            if (music != null) {
                music.stop();
            }
        } else if (type == EFFECT_MUSIC) {
            if (sound != null) {
                sound.stop();
            }
        }
    }

    public void resumeSound() {
        if (type == BG_MUSIC) {
            if (music != null) {
                music.resume();
            }
        } else if (type == EFFECT_MUSIC) {
            if (sound != null && !sound.isReleased()) {
                sound.resume();
            }
        }
    }

    public long getDuration() {
        long length = 0;
        if (type == BG_MUSIC) {
            if (music != null) {
                length = music.getDuration();
            }
        }
        return length;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Sound getSound() {
        return sound;
    }

    public void setSound(Sound sound) {
        this.sound = sound;
    }

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }

}
