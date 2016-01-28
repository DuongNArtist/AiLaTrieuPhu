package com.skynet.ailatrieuphu.widgets;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.TextView;

public class TyperTextView extends TextView {

    private CharSequence mText = "";
    private int mIndex;
    private long mDelay;
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            setText(mText.subSequence(0, mIndex++));
            if (mIndex <= mText.length()) {
                mHandler.postDelayed(mRunnable, mDelay);
            }
        }
    };

    public TyperTextView(Context context) {
        super(context);
    }

    public TyperTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public TyperTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void animateText(CharSequence text, int delay) {
        mText = text;
        mDelay = delay;
        mIndex = 0;
        setText("");
        mHandler.removeCallbacks(mRunnable);
        mHandler.postDelayed(mRunnable, mDelay);
    }
}
