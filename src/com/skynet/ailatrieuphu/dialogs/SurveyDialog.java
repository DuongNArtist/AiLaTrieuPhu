package com.skynet.ailatrieuphu.dialogs;

import java.util.Random;

import android.content.Context;
import android.os.Handler;
import android.widget.ImageView;

import com.skynet.ailatrieuphu.R;
import com.skynet.ailatrieuphu.activities.AiLaTrieuPhuActivity;
import com.skynet.ailatrieuphu.audios.AudioManager;

public class SurveyDialog extends BaseDialog {

    public static final String TAG = SurveyDialog.class.getSimpleName();

    public static final int DURATION = 9000;
    public static final int DELAY = 5000;
    public static final int PERIOD = 100;
    public static final int TIMES = 32;
    private ImageView[] mivParses;
    private Handler mHandler;
    private Random mRandom;
    private int[] mIndexes;

    public SurveyDialog(Context context) {
        super(context);
        if (AiLaTrieuPhuActivity.mEnglish) {
            AudioManager.getInstance().playSound(
                    AudioManager.HELPER_AUDIENCE_EN, false);
        } else {
            AudioManager.getInstance().playSound(
                    AudioManager.HELPER_AUDIENCE_VI, false);
        }
        mHandler = new Handler();
        mRandom = new Random();
        mIndexes = new int[TIMES];
        for (int index = 0; index < TIMES; index++) {
            mIndexes[index] = index;
        }
        for (int index = 0; index < TIMES; index++) {
            int target = mRandom.nextInt(TIMES);
            int temp = mIndexes[index];
            mIndexes[index] = mIndexes[target];
            mIndexes[target] = temp;
        }
        int delay = DELAY;
        if (AiLaTrieuPhuActivity.mEnglish) {
            delay = 0;
            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    for (int index = 0; index < mivParses.length; index++) {
                        mivParses[index]
                                .setImageResource(R.drawable.img_audience_off);
                    }
                    for (int index = 0; index < TIMES; index++) {
                        animate(index, mIndexes[index]);
                    }
                }
            }, DURATION / 2);
        }
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                for (int index = 0; index < TIMES; index++) {
                    animate(index, mIndexes[index]);
                }
            }
        }, delay);
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                cancel();
            }
        }, DURATION);
    }

    @Override
    public int getViewId() {
        return R.layout.dialog_survey;
    }

    @Override
    public void bindView() {
        mivParses = new ImageView[TIMES];
        for (int index = 0; index < mivParses.length; index++) {
            mivParses[index] = (ImageView) findViewById(R.id.iv_dhap_00 + index);
        }
    }

    private void animate(final int index, final int indexOfAudience) {
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                mivParses[indexOfAudience]
                        .setImageResource(R.drawable.img_audience_on);
            }
        }, index * PERIOD);
    }
}
