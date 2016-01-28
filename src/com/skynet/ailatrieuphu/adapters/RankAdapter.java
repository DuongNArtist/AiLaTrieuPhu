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
import com.skynet.ailatrieuphu.fragments.RankFragment;
import com.skynet.ailatrieuphu.managers.MoneyManager;
import com.skynet.ailatrieuphu.models.RankModel;

public class RankAdapter extends ArrayAdapter<RankModel> {

    private LayoutInflater mLayoutInflater;
    private boolean mAll;

    public RankAdapter(Context context, RankFragment rankFragment,
            int resource, List<RankModel> objects) {
        super(context, resource, objects);
        mLayoutInflater = LayoutInflater.from(context);
        mAll = true;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.layout_rank, viewGroup,
                    false);
        }

        TextView tvPosition = (TextView) view.findViewById(R.id.tv_rli_stt);
        final ImageView ivAvatar = (ImageView) view
                .findViewById(R.id.iv_rli_avatar);
        TextView tvName = (TextView) view.findViewById(R.id.tv_rli_name);
        TextView tvScore = (TextView) view.findViewById(R.id.tv_rli_score);

        RankModel rankModel = getItem(position);
        tvPosition.setText((position + 1) + "");
        tvName.setText(rankModel.getName());
        tvScore.setText(MoneyManager.parseMoney((int) rankModel.getScore()));

        final DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.img_player)
                .showImageForEmptyUri(R.drawable.img_player)
                .showImageOnFail(R.drawable.img_player).cacheInMemory(true)
                .cacheOnDisc(true).build();
        ImageLoader.getInstance().displayImage(rankModel.getLink(), ivAvatar,
                options);
        return view;
    }

    public boolean isAll() {
        return mAll;
    }

    public void setAll(boolean all) {
        mAll = all;
    }
}
