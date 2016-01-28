package com.skynet.ailatrieuphu.fragments;

import android.view.View;
import android.widget.LinearLayout;

import com.skynet.ailatrieuphu.R;
import com.skynet.ailatrieuphu.activities.AiLaTrieuPhuActivity;
import com.skynet.ailatrieuphu.audios.AudioManager;
import com.skynet.ailatrieuphu.preferences.PreferenceManager;
import com.skynet.ailatrieuphu.sockets.Message;
import com.skynet.ailatrieuphu.utilities.LanguageUtility;

public class SplashFragment extends BaseFragment implements Runnable {

    public static final String TAG = SplashFragment.class.getSimpleName();

    private LinearLayout mllBackground;

    @Override
    public void onReceived(AiLaTrieuPhuActivity ac, Message message) {

    }

    @Override
    public void onFailed() {

    }

    @Override
    public void createVariables() {
        new Thread(this).start();
    }

    @Override
    public int getViewId() {
        return R.layout.fragment_splash;
    }

    @Override
    public void createViews(View view) {
        mllBackground = (LinearLayout) view
                .findViewById(R.id.ll_fragment_splash);
        if (PreferenceManager.getInstance(mMainActivity).getAppLanguage()
                .equals(LanguageUtility.VI)) {
            mllBackground.setBackgroundResource(R.drawable.bkg_splash_vi);
        } else {
            mllBackground.setBackgroundResource(R.drawable.bkg_splash_en);
        }
    }

    @Override
    public void confirmFinish() {
        finish();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long start = System.currentTimeMillis();
        mAudioManager = AudioManager.getInstance();
        mAudioManager.init(mMainActivity);
        mAudioManager.loadAllSounds();
        long delay = 2000 - (System.currentTimeMillis() - start);
        if (delay < 0) {
            delay = 0;
        }
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finish();
        mMainActivity.switchContent(new MenuFragment(),
                MenuFragment.class.getSimpleName(), false, null);
    }

}
