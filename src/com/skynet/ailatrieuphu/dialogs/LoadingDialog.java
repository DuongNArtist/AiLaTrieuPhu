package com.skynet.ailatrieuphu.dialogs;

import com.skynet.ailatrieuphu.R;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class LoadingDialog extends BaseDialog {

    private static LoadingDialog mInstance;

    private ImageView mivLoading;
    private Animation mAnimation;

    private LoadingDialog(Context context) {
        super(context);
        mAnimation = AnimationUtils
                .loadAnimation(mContext, R.anim.anim_loading);
    }

    @Override
    public void show() {
        super.show();
        mivLoading.startAnimation(mAnimation);
    }

    public static LoadingDialog getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new LoadingDialog(context);
        }
        return mInstance;
    }

    @Override
    public int getViewId() {
        return R.layout.dialog_loading;
    }

    @Override
    public void bindView() {
        mivLoading = (ImageView) findViewById(R.id.iv_loading);
    }

}
