package com.skynet.ailatrieuphu.adapters;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.skynet.ailatrieuphu.R;
import com.skynet.ailatrieuphu.fragments.IndirectVTV3Fragment;
import com.skynet.ailatrieuphu.models.WeekModel;

public class WeekPlayVTV3Adapter extends ArrayAdapter<WeekModel> {

    private LayoutInflater mLayoutInflater;
    private IndirectVTV3Fragment mIndirectVTV3Fragment;
    private boolean mSending;
    private String mTitle;

    public boolean isSending() {
        return mSending;
    }

    public void setSending(boolean sending) {
        this.mSending = sending;
    }

    public WeekPlayVTV3Adapter(Context context, int resource,
            IndirectVTV3Fragment indirectVTV3Fragment, List<WeekModel> objects) {
        super(context, resource, objects);
        mTitle = context.getResources().getString(R.string.dnt_week);
        mIndirectVTV3Fragment = indirectVTV3Fragment;
        mLayoutInflater = LayoutInflater.from(context);
        mSending = false;
    }

    private class ViewHodel {
        private Button btWeek;
        private TextView tvWeek;
        private TextView tvTitle;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHodel viewHodel;
        if (view == null) {
            viewHodel = new ViewHodel();
            view = mLayoutInflater.inflate(R.layout.layout_week, parent, false);
            viewHodel.btWeek = (Button) view.findViewById(R.id.bt_week_icon);
            viewHodel.btWeek.setTag(new Integer(position));
            viewHodel.tvWeek = (TextView) view
                    .findViewById(R.id.tv_week_number);
            viewHodel.tvTitle = (TextView) view
                    .findViewById(R.id.tv_week_title);
            view.setTag(viewHodel);
        } else {
            viewHodel = (ViewHodel) view.getTag();
        }
        viewHodel.tvTitle.setText(mTitle);
        WeekModel weekModel = getItem(position);
        viewHodel.btWeek.setEnabled(weekModel.isEnabled());
        if (weekModel.isSelected()) {
            viewHodel.tvTitle.setTextColor(Color.WHITE);
            viewHodel.tvWeek.setTextColor(Color.parseColor("#071836"));
            viewHodel.btWeek.setBackgroundResource(R.drawable.img_week_1);
        } else {
            viewHodel.tvTitle.setTextColor(Color.parseColor("#071836"));
            viewHodel.tvWeek.setTextColor(Color.WHITE);
            viewHodel.btWeek.setBackgroundResource(R.drawable.img_week_0);
        }
        viewHodel.tvWeek.setText(weekModel.getName());
        if (position > getCount() - 1) {
            if (mSending) {
                mSending = false;
                mIndirectVTV3Fragment.sendRequestGetListOfWeek();
            }
        } else {
            mSending = true;
        }
        return view;
    }
}
