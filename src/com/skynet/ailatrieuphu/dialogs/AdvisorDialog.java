package com.skynet.ailatrieuphu.dialogs;

import java.util.Random;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skynet.ailatrieuphu.R;

public class AdvisorDialog extends BaseDialog implements OnCancelListener,
        OnClickListener {

    public static final String TAG = AdvisorDialog.class.getSimpleName();
    public static final int[] ADVISOR_IDS = { R.id.iv_advisor_0,
            R.id.iv_advisor_1, R.id.iv_advisor_2 };
    public static final int[] TXT_ADVISOR_IDS = { R.id.tv_advisor_0,
            R.id.tv_advisor_1, R.id.tv_advisor_2 };
    public static final int[] CONVERSATION_IDS = {
            R.drawable.img_conversation_0, R.drawable.img_conversation_1,
            R.drawable.img_conversation_2 };

    private Handler mHandler;
    private Button mbtClose;
    private Random mRandom;
    private ImageView[] mivAdvisors;
    private TextView[] mtvAdvisors;
    private String[] mAnswers;
    private TextView mtvConversation;
    private char[] mChrAnswers;
    private int mRightAnswer;
    private int[] mFailOptions;
    private OnBackListener mOnBackListener;

    public AdvisorDialog(Context context, int rightAnswer, int[] failOptions) {
        super(context);
        setCancelable(true);
        setOnCancelListener(this);
        mRightAnswer = rightAnswer;
        mFailOptions = failOptions;
        mHandler = new Handler();
        mRandom = new Random();
        mAnswers = new String[ADVISOR_IDS.length];
        mChrAnswers = new char[ADVISOR_IDS.length];
        initData();
        startAnimation();
    }

    @Override
    public int getViewId() {
        return R.layout.dialog_advisor;
    }

    @Override
    public void bindView() {
        mbtClose = (Button) findViewById(R.id.bt_advisor_close);
        mbtClose.setOnClickListener(this);
        mtvConversation = (TextView) findViewById(R.id.tv_advisor_conversation);
        mivAdvisors = new ImageView[ADVISOR_IDS.length];
        for (int index = 0; index < mivAdvisors.length; index++) {
            mivAdvisors[index] = (ImageView) findViewById(ADVISOR_IDS[index]);
            mivAdvisors[index].setVisibility(LinearLayout.INVISIBLE);
        }
        mtvAdvisors = new TextView[TXT_ADVISOR_IDS.length];
        for (int index = 0; index < mivAdvisors.length; index++) {
            mtvAdvisors[index] = (TextView) findViewById(TXT_ADVISOR_IDS[index]);
            mtvAdvisors[index].setVisibility(LinearLayout.INVISIBLE);
        }
    }

    private void initData() {
        int failAnswer = 0;
        for (int index = 0; index < 4; index++) {
            if (mFailOptions != null) {
                if (index != mRightAnswer && index != mFailOptions[0]
                        && index != mFailOptions[1]) {
                    failAnswer = index;
                }
            } else {
                if (index != mRightAnswer) {
                    failAnswer = index;
                }
            }
        }
        for (int i = 0; i < mAnswers.length; i++) {
            mChrAnswers[i] = mChrAnswers[i] = (char) (65 + mRightAnswer);
            if (mRandom.nextInt(100) < 20) {
                mChrAnswers[i] = (char) (65 + failAnswer);
            }
            mAnswers[i] = mContext.getResources().getString(
                    R.string.dnt_advisor, mChrAnswers[i],
                    mRandom.nextInt((int) mChrAnswers[i]));
        }
    }

    private void startAnimation() {
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                for (int index = 0; index < mivAdvisors.length; index++) {
                    animate(index);
                }
            }
        }, 500);
    }

    private void animate(final int index) {
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                mivAdvisors[index].setVisibility(ImageView.VISIBLE);
                mtvConversation.setBackgroundResource(CONVERSATION_IDS[index]);
                mtvConversation.setText(mAnswers[index]);
                mtvAdvisors[index].setVisibility(TextView.VISIBLE);
                mtvAdvisors[index].setText(mChrAnswers[index] + "");
            }
        }, index * 2000);
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
