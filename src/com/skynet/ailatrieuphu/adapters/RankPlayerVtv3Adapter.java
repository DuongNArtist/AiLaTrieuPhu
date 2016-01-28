package com.skynet.ailatrieuphu.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.skynet.ailatrieuphu.R;
import com.skynet.ailatrieuphu.fragments.DirectVTV3Fragment;
import com.skynet.ailatrieuphu.models.RankPlayerModel;

public class RankPlayerVtv3Adapter extends ArrayAdapter<RankPlayerModel> {

    private LayoutInflater mLayoutInflater;

    public RankPlayerVtv3Adapter(Context context, int resource,
            DirectVTV3Fragment directVTV3Fragment, List<RankPlayerModel> arrList) {
        super(context, resource, arrList);
        mLayoutInflater = LayoutInflater.from(context);
    }

    private class ViewHolder {
        public ImageView ivAvatar;
        public TextView tvName;
        public TextView tvScore;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = mLayoutInflater.inflate(R.layout.layout_player, parent,
                    false);
            holder.ivAvatar = (ImageView) view
                    .findViewById(R.id.img_player_rank_vtv3_avatar);
            holder.tvName = (TextView) view
                    .findViewById(R.id.tv_player_rank_direct_name);
            holder.tvScore = (TextView) view
                    .findViewById(R.id.tv_player_rank_direct_score);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        RankPlayerModel rankModel = getItem(position);
        holder.tvName.setText(rankModel.getName());
        holder.tvScore.setText(rankModel.getScore() + "");

        final DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.img_player)
                .showImageForEmptyUri(R.drawable.img_player)
                .showImageOnFail(R.drawable.img_player).cacheInMemory(true)
                .cacheOnDisc(true).build();

        ImageLoader.getInstance().displayImage(rankModel.getLink(),
                holder.ivAvatar, options);
        return view;
    }
}
