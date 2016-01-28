package com.skynet.ailatrieuphu.dialogs;

import java.util.Random;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.res.Resources;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.skynet.ailatrieuphu.R;
import com.skynet.ailatrieuphu.models.AccountModel;
import com.skynet.ailatrieuphu.widgets.CircleImageView;
import com.skynet.ailatrieuphu.widgets.TyperTextView;

public class BuddyDialog extends BaseDialog implements
        android.view.View.OnClickListener, OnCancelListener {

    public static final String TAG = BuddyDialog.class.getSimpleName();
    public static final int DURATION = 1000;
    public static final int TIMES = 3;

    private Button mbtClose;
    private CircleImageView mivAvatar;
    private TyperTextView mtvConversation;
    private Handler mHandler;
    private String[] mConversations;
    private String mName;
    private int mResId;
    private OnBackListener mOnBackListener;

    public BuddyDialog(Context context, int resId, String name,
            int rightAnswer, int[] failOptions, int currentLevel,
            String nextMoney) {
        super(context);
        setCancelable(true);
        setOnCancelListener(this);
        mResId = resId;
        mName = name;
        mivAvatar.setImageResource(mResId);
        mHandler = new Handler();
        mConversations = new String[TIMES];
        Resources resources = mContext.getResources();
        mConversations[0] = resources.getString(R.string.dnt_buddy_00, mName,
                currentLevel, nextMoney);
        Random random = new Random();
        char chrAnswer = (char) (rightAnswer + 65);
        int failAnswer = 0;
        for (int index = 0; index < 4; index++) {
            if (failOptions != null) {
                if (index != rightAnswer && index != failOptions[0]
                        && index != failOptions[1]) {
                    failAnswer = index;
                }
            } else {
                if (index != rightAnswer) {
                    failAnswer = index;
                }
            }
        }
        if (random.nextInt(100) < 20) {
            chrAnswer = (char) (failAnswer + 65);
        }
        mConversations[1] = resources.getString(R.string.dnt_buddy_01,
                chrAnswer);
        mConversations[2] = resources.getString(R.string.dnt_buddy_02,
                chrAnswer, mName);
        for (int index = 0; index < mConversations.length; index++) {
            final int finalIndex = index;
            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    animate(finalIndex);
                }
            }, finalIndex * DURATION);
        }
    }

    private void animate(final int index) {
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                int delay = DURATION / mConversations[index].length();
                mtvConversation.animateText(mConversations[index], delay);
                if (index == 0) {
                    mivAvatar.setImageResource(R.drawable.img_mc);
                } else if (index == 1) {
                    mivAvatar.setImageResource(mResId);
                } else {
                    String link = AccountModel.getInstance().getUsername();
                    if (link == null) {
                        mivAvatar.setImageResource(R.drawable.img_player);
                    } else {
                        link = "https://graph.facebook.com/" + link
                                + "/picture?type=large&width=200&height=200";
                        final DisplayImageOptions options = new DisplayImageOptions.Builder()
                                .showImageOnLoading(R.drawable.img_player)
                                .showImageForEmptyUri(R.drawable.img_player)
                                .showImageOnFail(R.drawable.img_player)
                                .cacheInMemory(true).cacheOnDisc(true).build();
                        ImageLoader.getInstance().displayImage(link, mivAvatar,
                                options);
                    }
                }
            }
        }, 2 * index * DURATION);
    }

    @Override
    public int getViewId() {
        return R.layout.dialog_buddy;
    }

    @Override
    public void bindView() {
        mbtClose = (Button) findViewById(R.id.bt_buddy_close);
        mbtClose.setOnClickListener(this);
        mivAvatar = (CircleImageView) findViewById(R.id.iv_buddy_avatar);
        mtvConversation = (TyperTextView) findViewById(R.id.tv_buddy_conversation);
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
