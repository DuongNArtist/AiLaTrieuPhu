package com.skynet.ailatrieuphu.dialogs;

import java.util.Random;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skynet.ailatrieuphu.R;

public class AudienceDialog extends BaseDialog implements
        android.view.View.OnClickListener, OnCancelListener {

    public static final String TAG = AudienceDialog.class.getSimpleName();
    private static final int[] BAR_IDS = { R.id.iv_dha_bar_0,
            R.id.iv_dha_bar_1, R.id.iv_dha_bar_2, R.id.iv_dha_bar_3 };
    private static final int[] PER_IDS = { R.id.tv_dha_per_0,
            R.id.tv_dha_per_1, R.id.tv_dha_per_2, R.id.tv_dha_per_3 };
    public static final int PERIOD = 25;

    private ImageView[] mivBars;
    private TextView[] mtvPers;
    private Button mbtClose;
    private Handler mHandler;
    private Random mRandom;
    private int mRightAnswer;
    private int mWidth;
    private int mHeight;
    private int mSpeed;
    private int mMax;
    private int[] mFailOptions;
    private int[] mGoalPers;
    private int[] mInitPers;
    private OnBackListener mOnBackListener;

    public AudienceDialog(Context context, int rightAnswer, int[] failOptions) {
        super(context);
        setCancelable(true);
        setOnCancelListener(this);
        mRightAnswer = rightAnswer;
        mFailOptions = failOptions;
        mRandom = new Random();
        mHandler = new Handler();
        initializeValues();
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < 2 * PERIOD; i++) {
                    increaseAll(i);
                }
            }
        }, 1000);
    }

    public void increaseAll(final int index) {
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < BAR_IDS.length; i++) {
                    increaseHeight(i);
                }
            }
        }, index * PERIOD);
    }

    public void initializeValues() {
        mWidth = mivBars[0].getLayoutParams().width;
        mHeight = mivBars[0].getLayoutParams().height;
        mSpeed = mHeight / PERIOD;

        Log.i(TAG, "init width  = " + mWidth);
        Log.i(TAG, "init height = " + mHeight);
        Log.i(TAG, "init speed  = " + mSpeed);

        int sum = 100;
        mGoalPers = new int[4];
        mInitPers = new int[4];
        int start = 50;
        if (mRandom.nextInt(100) < 20) {
            start = 10;
        }
        mGoalPers[mRightAnswer] = start + mRandom.nextInt(sum - start);
        sum -= mGoalPers[mRightAnswer];

        switch (mRightAnswer) {
        case 0:
            mGoalPers[1] = mRandom.nextInt(sum);
            sum -= mGoalPers[1];
            mGoalPers[2] = mRandom.nextInt(sum);
            sum -= mGoalPers[2];
            mGoalPers[3] = sum;
            break;

        case 1:
            mGoalPers[0] = mRandom.nextInt(sum);
            sum -= mGoalPers[0];
            mGoalPers[2] = mRandom.nextInt(sum);
            sum -= mGoalPers[2];
            mGoalPers[3] = sum;
            break;

        case 2:
            mGoalPers[0] = mRandom.nextInt(sum);
            sum -= mGoalPers[0];
            mGoalPers[1] = mRandom.nextInt(sum);
            sum -= mGoalPers[1];
            mGoalPers[3] = sum;
            break;

        case 3:
            mGoalPers[0] = mRandom.nextInt(sum);
            sum -= mGoalPers[0];
            mGoalPers[1] = mRandom.nextInt(sum);
            sum -= mGoalPers[1];
            mGoalPers[2] = sum;
            break;

        default:
            break;
        }

        if (mFailOptions != null) {
            int index0 = mFailOptions[0];
            int index1 = mFailOptions[1];
            Log.i(TAG, index0 + " - " + index1);
            mGoalPers[mRightAnswer] += mGoalPers[index0];
            mGoalPers[index0] = 0;
            mGoalPers[mRightAnswer] += mGoalPers[index1];
            mGoalPers[index1] = 0;
        }

        mMax = 0;
        for (int i = 0; i < mGoalPers.length; i++) {
            if (mGoalPers[i] > mGoalPers[mMax]) {
                mMax = i;
            }
        }

        LinearLayout.LayoutParams initLayoutParams = new LinearLayout.LayoutParams(
                mWidth, 0);
        for (int index = 0; index < mivBars.length; index++) {
            mivBars[index].setLayoutParams(initLayoutParams);
        }
    }

    @Override
    public int getViewId() {
        return R.layout.dialog_audience;
    }

    @Override
    public void bindView() {
        mbtClose = (Button) findViewById(R.id.bt_dha_close);
        mbtClose.setOnClickListener(this);
        mivBars = new ImageView[BAR_IDS.length];
        for (int i = 0; i < mivBars.length; i++) {
            mivBars[i] = (ImageView) findViewById(BAR_IDS[i]);
        }
        mtvPers = new TextView[PER_IDS.length];
        for (int i = 0; i < mtvPers.length; i++) {
            mtvPers[i] = (TextView) findViewById(PER_IDS[i]);
        }
    }

    private void increaseHeight(final int index) {
        if (mInitPers[index] <= mGoalPers[index]) {
            mInitPers[index] += mSpeed;
        }
        Log.i(TAG, "height[" + index + "] = " + mInitPers[index]);
        final LinearLayout.LayoutParams goalLayoutParams = new LinearLayout.LayoutParams(
                mWidth, mInitPers[index] * mHeight / 100);
        final String strPercent;
        if (mInitPers[index] < mGoalPers[index]) {
            strPercent = mInitPers[index] + "%";
        } else {
            strPercent = mGoalPers[index] + "%";
        }
        mtvPers[index].setText(strPercent);
        mivBars[index].setLayoutParams(goalLayoutParams);
    }

    public void setOnBackListener(OnBackListener listener) {
        mOnBackListener = listener;
    }

    @Override
    public void onClick(View v) {
        mOnBackListener.onBack();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        mOnBackListener.onBack();
    }
}
