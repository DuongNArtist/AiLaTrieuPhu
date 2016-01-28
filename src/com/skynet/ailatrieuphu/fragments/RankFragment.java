package com.skynet.ailatrieuphu.fragments;

import java.util.ArrayList;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.skynet.ailatrieuphu.R;
import com.skynet.ailatrieuphu.activities.AiLaTrieuPhuActivity;
import com.skynet.ailatrieuphu.adapters.RankAdapter;
import com.skynet.ailatrieuphu.audios.AudioManager;
import com.skynet.ailatrieuphu.dialogs.ConfirmDialog;
import com.skynet.ailatrieuphu.dialogs.OnConfirmListener;
import com.skynet.ailatrieuphu.models.RankModel;
import com.skynet.ailatrieuphu.sockets.IOUtility;
import com.skynet.ailatrieuphu.sockets.Message;
import com.skynet.ailatrieuphu.sockets.Protocol;
import com.skynet.ailatrieuphu.sockets.SendData;

public class RankFragment extends BaseFragment implements OnClickListener {

    public static final String TAG = RankFragment.class.getSimpleName();

    private Button mbtAll;
    private Button mbtVtv3;
    private ListView mlvRanking;
    private ImageView mivTitle;
    private ImageView mivLogo;
    private Handler mHandler;

    public void sendRequestGetRankAll() {
        Message message = SendData.requestGetRankAll();
        mMainActivity.sendRequest(message, true, false);
    }

    public void sendRequestGetRankVtv3() {
        Message message = SendData.requestGetRankVtv3();
        mMainActivity.sendRequest(message, true, false);
    }

    @Override
    public void onReceived(AiLaTrieuPhuActivity mainActivity, Message message) {
        switch (message.getServerCommand()) {
        case Protocol.CMD_REQUEST_GET_RANK_ALL:
            handleRequestGetRankAll(message);
            break;

        case Protocol.CMD_REQUEST_GET_RANK_VTV3:
            handleRequestGetRankVtv3(message);
            break;

        default:
            break;
        }
    }

    public void handleRequestGetRankAll(Message message) {
        int size = IOUtility.readInt(message);
        RankAdapter rankAdapter = (RankAdapter) mlvRanking.getAdapter();
        if (!rankAdapter.isAll()) {
            rankAdapter.setAll(true);
            rankAdapter.clear();
            rankAdapter.notifyDataSetChanged();
            Log.i("Clear VTV3", "OK");
        }
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                RankModel rankModel = new RankModel();
                rankModel.setName(IOUtility.readString(message));
                String link = IOUtility.readString(message);
                if (link.length() > 0) {
                    link = "https://graph.facebook.com/" + link
                            + "/picture?type=large&width=200&height=200";
                }
                rankModel.setLink(link);
                rankModel.setScore(IOUtility.readLong(message));
                rankAdapter.add(rankModel);
                Log.i(TAG, "Link: " + rankModel.getLink());
            }
            rankAdapter.notifyDataSetChanged();
        }
    }

    private void handleRequestGetRankVtv3(Message message) {
        int size = IOUtility.readInt(message);
        RankAdapter rankAdapter = (RankAdapter) mlvRanking.getAdapter();
        if (rankAdapter.isAll()) {
            rankAdapter.setAll(false);
            rankAdapter.clear();
            rankAdapter.notifyDataSetChanged();
            Log.i("Clear All", "OK");
        }
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                RankModel rankModel = new RankModel();
                rankModel.setName(IOUtility.readString(message));
                String link = IOUtility.readString(message);
                if (link.length() > 0) {
                    link = "https://graph.facebook.com/" + link
                            + "/picture?type=large&width=200&height=200";
                }
                rankModel.setLink(link);
                rankModel.setScore(IOUtility.readLong(message));
                rankAdapter.add(rankModel);
            }
            rankAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFailed() {
        final ConfirmDialog dialog = ConfirmDialog.getInstance(mMainActivity);
        dialog.setMessage(getResources().getString(R.string.bmv_connect_false));
        dialog.setOnConfirmListener(new OnConfirmListener() {

            @Override
            public void onClickYesListener() {
                if (mbtAll.isEnabled()) {
                    sendRequestGetRankVtv3();
                } else {
                    sendRequestGetRankAll();
                }
                dialog.cancel();
            }

            @Override
            public void onClickNoListener() {
                finish();
                dialog.cancel();
            }
        });
        dialog.show();
    }

    @Override
    public void createVariables() {
        mHandler = new Handler();
    }

    @Override
    public int getViewId() {
        return R.layout.fragment_rank;
    }

    @Override
    public void createViews(View view) {
        mivTitle = (ImageView) view.findViewById(R.id.iv_rank_title);
        mivLogo = (ImageView) view.findViewById(R.id.iv_rank_logo);
        if (AiLaTrieuPhuActivity.mEnglish) {
            mivTitle.setImageResource(R.drawable.img_rank_en);
            mivLogo.setImageResource(R.drawable.img_logo_en);
        } else {
            mivTitle.setImageResource(R.drawable.img_rank_vi);
            mivLogo.setImageResource(R.drawable.img_logo_vi);
        }
        mbtAll = (Button) view.findViewById(R.id.bt_rank_all);
        mbtVtv3 = (Button) view.findViewById(R.id.bt_rank_vtv3);
        mlvRanking = (ListView) view.findViewById(R.id.list_ranking);
        mbtAll.setOnClickListener(this);
        mbtVtv3.setOnClickListener(this);
        ArrayList<RankModel> rankModels = new ArrayList<RankModel>();
        mlvRanking.setAdapter(new RankAdapter(mMainActivity, this, 0,
                rankModels));
        mbtAll.setEnabled(false);
        sendRequestGetRankAll();
    }

    @Override
    public void onClick(View view) {
        AudioManager.getInstance().playSound(AudioManager.TOUCH, false);
        switch (view.getId()) {
        case R.id.bt_rank_all:
            mbtAll.setEnabled(false);
            mbtVtv3.setEnabled(true);
            mbtAll.setBackgroundResource(R.drawable.btn_rank_vtv3_1);
            mbtVtv3.setBackgroundResource(R.drawable.btn_rank_vtv3_0);
            sendRequestGetRankAll();
            break;

        case R.id.bt_rank_vtv3:
            mbtAll.setEnabled(true);
            mbtVtv3.setEnabled(false);
            mbtAll.setBackgroundResource(R.drawable.btn_rank_vtv3_0);
            mbtVtv3.setBackgroundResource(R.drawable.btn_rank_vtv3_1);
            sendRequestGetRankVtv3();
            break;

        default:
            break;
        }
    }

    @Override
    public void confirmFinish() {
        finish();
    }

    public void disableButton(int delayMillis) {
        setEnableButton(false);
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                setEnableButton(true);
            }
        }, delayMillis);
    }

    private void setEnableButton(boolean enabled) {
        mbtAll.setEnabled(enabled);
        mbtVtv3.setEnabled(enabled);
    }
}
