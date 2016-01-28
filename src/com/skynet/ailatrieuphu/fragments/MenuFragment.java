package com.skynet.ailatrieuphu.fragments;

import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.skynet.ailatrieuphu.R;
import com.skynet.ailatrieuphu.activities.AiLaTrieuPhuActivity;
import com.skynet.ailatrieuphu.adapters.LanguageAdapter;
import com.skynet.ailatrieuphu.audios.AudioManager;
import com.skynet.ailatrieuphu.dialogs.ConfirmDialog;
import com.skynet.ailatrieuphu.dialogs.LanguageDialog;
import com.skynet.ailatrieuphu.dialogs.OnConfirmListener;
import com.skynet.ailatrieuphu.models.AccountModel;
import com.skynet.ailatrieuphu.preferences.PreferenceManager;
import com.skynet.ailatrieuphu.sockets.IOUtility;
import com.skynet.ailatrieuphu.sockets.Message;
import com.skynet.ailatrieuphu.sockets.Protocol;
import com.skynet.ailatrieuphu.sockets.SendData;
import com.skynet.ailatrieuphu.utilities.LanguageUtility;

public class MenuFragment extends BaseFragment implements OnClickListener {

    public static final String TAG = MenuFragment.class.getSimpleName();

    private Animation[] mAnimations;
    private ImageView mLogo;
    private Button mbtNormal;
    private Button mbtHelp;
    private Button mbtRank;
    private Button mbtVtv3;
    private Button mbtVn;
    private Button mbtUs;
    private TextView mtvGreeting;

    @Override
    public void onReceived(AiLaTrieuPhuActivity mainActivity, Message message) {
        switch (message.getServerCommand()) {
        case Protocol.CMD_REQUEST_CHECK_IN_DIRECT_VTV3:
            handleRequestCheckIn(message);
            break;

        default:
            break;
        }
    }

    private void handleRequestCheckIn(Message message) {
        boolean result = IOUtility.readBoolean(message);
        if (result) {
            mMainActivity.switchContent(new DirectVTV3Fragment(),
                    DirectVTV3Fragment.TAG, true, null);
        } else {
            mMainActivity.switchContent(new IndirectVTV3Fragment(),
                    IndirectVTV3Fragment.TAG, true, null);
        }
    }

    @Override
    public void onFailed() {
        final ConfirmDialog dialog = ConfirmDialog.getInstance(mMainActivity);
        dialog.setMessage(getResources().getString(R.string.bmv_connect_false));
        dialog.show();
        dialog.setOnConfirmListener(new OnConfirmListener() {

            @Override
            public void onClickYesListener() {
                dialog.cancel();
                hanldeClickVtv3();
            }

            @Override
            public void onClickNoListener() {
                dialog.cancel();
            }
        });

    }

    @Override
    public void onClick(View v) {
        AudioManager.getInstance().playSound(AudioManager.TOUCH, false);
        switch (v.getId()) {
        case R.id.bt_menu_normal:
            hanldeClickNormal();
            break;

        case R.id.bt_menu_rank:
            hanldeClickRank();
            break;

        case R.id.bt_menu_help:
            hanldeClickHelp();
            break;

        case R.id.bt_menu_vtv3:
            hanldeClickVtv3();
            break;

        case R.id.bt_menu_vi:
            hanldeClickVi();
            mbtUs.setEnabled(true);
            mbtVn.setEnabled(false);
            break;

        case R.id.bt_menu_en:
            hanldeClickEn();
            mbtUs.setEnabled(false);
            mbtVn.setEnabled(true);
            break;

        default:
            break;
        }
    }

    private void hanldeClickEn() {
        mPreferenceUtility.setAppLanguage(LanguageUtility.EN);
        setTextForAllControl();
    }

    private void hanldeClickVi() {
        mPreferenceUtility.setAppLanguage(LanguageUtility.VI);
        setTextForAllControl();
    }

    private void hanldeClickHelp() {
        mMainActivity.switchContent(new HelpFragment(), HelpFragment.TAG, true,
                null);
    }

    private void hanldeClickRank() {
        mMainActivity.switchContent(new RankFragment(), RankFragment.TAG, true,
                null);
    }

    public void hanldeClickVtv3() {
        final LanguageDialog dialog = LanguageDialog.getInstance(mMainActivity);
        dialog.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                    int position, long id) {
                dialog.cancel();
                AudioManager.getInstance().playSound(AudioManager.TOUCH, false);
                LanguageAdapter adapter = dialog.getAdapter();
                PlayFragment.mLanguage = adapter.getItem(position)
                        .getLanguage();
                showReadyDialog();
                AudioManager.getInstance().pauseSound(AudioManager.INTRO);

            }
        });
        dialog.show();
    }

    public void showReadyDialog() {
        final ConfirmDialog dialog = ConfirmDialog.getInstance(mMainActivity);
        dialog.setMessage(getResources().getString(R.string.dnt_ready));
        dialog.setOnConfirmListener(new OnConfirmListener() {

            @Override
            public void onClickYesListener() {
                dialog.cancel();
                Message message = SendData
                        .requestCheckInDirectVtv3(PlayFragment.mLanguage);
                mMainActivity.sendRequest(message, true, false);
            }

            @Override
            public void onClickNoListener() {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void hanldeClickNormal() {
        final LanguageDialog dialog = LanguageDialog.getInstance(mMainActivity);
        dialog.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                    int position, long id) {
                AudioManager.getInstance().playSound(AudioManager.TOUCH, false);
                LanguageAdapter adapter = dialog.getAdapter();
                PlayFragment.mLanguage = adapter.getItem(position)
                        .getLanguage();
                mMainActivity.switchContent(new NormalFragment(),
                        NormalFragment.TAG, true, null);
                dialog.cancel();
                AudioManager.getInstance().pauseSound(AudioManager.INTRO);
            }
        });
        dialog.show();
    }

    @Override
    public void createVariables() {
        mHandler = new Handler();
        mAudioManager.playSound(AudioManager.INTRO, false);
        mAnimations = new Animation[2];
        for (int index = 0; index < mAnimations.length; index++) {
            mAnimations[index] = AnimationUtils.loadAnimation(mMainActivity,
                    R.anim.anim_flag_off + index);
        }
    }

    @Override
    public int getViewId() {
        return R.layout.fragment_menu;
    }

    @Override
    public void createViews(View view) {
        mMainActivity.showBannerAdmode(mMainActivity.mllBanner);
        mbtNormal = (Button) view.findViewById(R.id.bt_menu_normal);
        mbtNormal.setOnClickListener(this);
        mbtVtv3 = (Button) view.findViewById(R.id.bt_menu_vtv3);
        mbtVtv3.setOnClickListener(this);
        mbtRank = (Button) view.findViewById(R.id.bt_menu_rank);
        mbtRank.setOnClickListener(this);
        mbtHelp = (Button) view.findViewById(R.id.bt_menu_help);
        mbtHelp.setOnClickListener(this);
        mbtVn = (Button) view.findViewById(R.id.bt_menu_vi);
        mbtVn.setOnClickListener(this);
        mbtUs = (Button) view.findViewById(R.id.bt_menu_en);
        mbtUs.setOnClickListener(this);
        mLogo = (ImageView) view.findViewById(R.id.iv_menu_logo);
        mtvGreeting = (TextView) view.findViewById(R.id.tv_greeting);
        setTextForAllControl();
    }

    @Override
    public void confirmFinish() {
        final ConfirmDialog dialog = ConfirmDialog.getInstance(mMainActivity);
        dialog.setMessage(getResources().getString(R.string.dnt_exit));
        dialog.setOnConfirmListener(new OnConfirmListener() {
            @Override
            public void onClickYesListener() {
                dialog.cancel();
                mAudioManager.releaseAllSound();
                MenuFragment.this.finish();
                System.gc();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }

            @Override
            public void onClickNoListener() {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void setTextForAllControl() {
        LanguageUtility.setLanguage(mMainActivity,
                mPreferenceUtility.getAppLanguage());
        mbtNormal.setText(getResources().getString(R.string.bmv_menu_play));
        mbtHelp.setText(getResources().getString(R.string.bmv_menu_help));
        mbtRank.setText(getResources().getString(R.string.bmv_menu_rank));
        mbtVtv3.setText(getResources().getString(R.string.bmv_menu_vtv3));
        if (mPreferenceUtility.getAppLanguage().equals(LanguageUtility.VI)) {
            AiLaTrieuPhuActivity.mEnglish = false;
            mbtVn.setBackgroundResource(R.drawable.flag_01_selected);
            mbtUs.setBackgroundResource(R.drawable.flag_00_normal);
            mLogo.setImageResource(R.drawable.img_logo_vi);
            mbtVn.startAnimation(mAnimations[1]);
            mbtUs.startAnimation(mAnimations[0]);
        } else {
            AiLaTrieuPhuActivity.mEnglish = true;
            mbtVn.setBackgroundResource(R.drawable.flag_01_normal);
            mbtUs.setBackgroundResource(R.drawable.flag_00_selected);
            mLogo.setImageResource(R.drawable.img_logo_en);
            mbtVn.startAnimation(mAnimations[0]);
            mbtUs.startAnimation(mAnimations[1]);
        }
        loadName();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadName();
    }

    private void loadName() {
        String username = PreferenceManager.getInstance(mMainActivity)
                .getUser();
        AccountModel accountModel = AccountModel.getInstance();
        accountModel.fromString(username);
        if (username.length() > 0) {
            username = accountModel.getUsername();
            if (accountModel.getEmail().length() > 2) {
                username = accountModel.getName();
            }
            username = getResources()
                    .getString(R.string.dnt_greeting, username);
            int length = username.length();
            StringBuilder builder = new StringBuilder();
            builder.append(username);
            for (int i = 0; i < length; i++) {
                builder.append(" ");
            }
            builder.append(username);
            username = builder.toString();
            mtvGreeting.setText(username);
            mtvGreeting.setSelected(true);
        }
    }
}
