package com.skynet.ailatrieuphu.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.skynet.ailatrieuphu.R;
import com.skynet.ailatrieuphu.models.LanguageModel;

public class LanguageAdapter extends ArrayAdapter<LanguageModel> {

    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public LanguageAdapter(Context context, int resource,
            List<LanguageModel> objects) {
        super(context, resource, objects);
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.layout_language,
                    parent, false);
            viewHolder = new ViewHolder();
            viewHolder.mivIcon = (ImageView) convertView
                    .findViewById(R.id.iv_language_icon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mivIcon.setImageResource(getItem(position).getId());
        return convertView;
    }

    private class ViewHolder {
        public ImageView mivIcon;
    }

}
