package com.skynet.ailatrieuphu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.skynet.ailatrieuphu.R;
import com.skynet.ailatrieuphu.activities.AiLaTrieuPhuActivity;

public class BuddyAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public BuddyAdapter(Context context, String[] buddyNames) {
        super(context, 0, buddyNames);
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.layout_buddy, viewGroup,
                    false);
        }
        ImageView ivAvatar = (ImageView) view
                .findViewById(R.id.iv_layout_buddy_avatar);
        final int resId;
        if (AiLaTrieuPhuActivity.mEnglish) {
            resId = R.drawable.img_buddy_en_00 + position;
        } else {
            resId = R.drawable.img_buddy_vi_00 + position;
        }
        ivAvatar.setImageResource(resId);
        return view;
    }
}
