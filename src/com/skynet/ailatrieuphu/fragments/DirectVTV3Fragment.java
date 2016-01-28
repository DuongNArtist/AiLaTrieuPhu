package com.skynet.ailatrieuphu.fragments;

import java.util.ArrayList;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.devsmart.android.ui.HorizontalListView;
import com.skynet.ailatrieuphu.R;
import com.skynet.ailatrieuphu.activities.AiLaTrieuPhuActivity;
import com.skynet.ailatrieuphu.adapters.RankPlayerVtv3Adapter;
import com.skynet.ailatrieuphu.audios.AudioManager;
import com.skynet.ailatrieuphu.constants.Constants;
import com.skynet.ailatrieuphu.dialogs.ConfirmDialog;
import com.skynet.ailatrieuphu.dialogs.OnConfirmListener;
import com.skynet.ailatrieuphu.managers.MoneyManager;
import com.skynet.ailatrieuphu.models.RankPlayerModel;
import com.skynet.ailatrieuphu.models.TimeQuestionModel;
import com.skynet.ailatrieuphu.preferences.PreferenceManager;
import com.skynet.ailatrieuphu.sockets.IOUtility;
import com.skynet.ailatrieuphu.sockets.Message;
import com.skynet.ailatrieuphu.sockets.Protocol;
import com.skynet.ailatrieuphu.sockets.SendData;

public class DirectVTV3Fragment extends PlayFragment implements OnClickListener {

    public static final String TAG = DirectVTV3Fragment.class.getSimpleName();

    private final int DELAY_NEXT_QUESTION = 2000;
    private static int mMoney = 0;
    private HorizontalListView mlvWeek;
    private boolean mDisplayed = false;
    private boolean mResult;
    private String mTimeQuestion = "";
    private String mIdQuestion = "";
    private int timer = 0;

    public boolean isResult() {
        return mResult;
    }

    public void setResult(boolean result) {
        this.mResult = result;
    }

    public boolean isEndDirect(Message message) {
        boolean end = IOUtility.readBoolean(message);
        return end;
    }

    public void sendRequesJoinVtv3() {
        Message message = SendData.requestJoinVtv3();
        mMainActivity.sendRequest(message, true, true);
    }

    public void sendRequesCheckOut() {
        Message message = SendData.requestCheckOutDirectVtv3();
        mMainActivity.sendRequest(message, true, false);
    }

    @Override
    public void onReceived(AiLaTrieuPhuActivity mainActivity, final Message message) {
        switch (message.getServerCommand()) {
        case Protocol.CMD_REQUEST_CHECK_OUT_DIRECT_VTV3:
            handleRequestCheckOutDirectVtv3(message);
            break;

        case Protocol.CMD_REQUEST_GET_RANK_PLAYER_VTV3:
            handleRequestGetRankPlayerVtv3(message);
            break;

        case Protocol.CMD_REQUEST_GET_DIRECT_VTV3_PACKAGE_QUESTION:
            handleRequestQuestionsDirectVtv3(message);
            break;

        case Protocol.CMD_REQUEST_END_DIRECT_VTV3:
            handleRequestEndDirectVtv3(message);
            break;

        case Protocol.CMD_REQUEST_SUBMIT_VTV3_PLAY_ANSWER:
            handleReQuestSubmitVtv3(message);
            break;

        case Protocol.CMD_REQUEST_JOIN_VTV3:
            handleReQuestJoinVtv3(message);
            break;

        default:
            break;
        }
    }

    public void handleReQuestJoinVtv3(Message message) {
        boolean checkJoin = IOUtility.readBoolean(message);
        if (!checkJoin) {
            final ConfirmDialog dialog = ConfirmDialog
                    .getInstance(mMainActivity);
            dialog.setMessage(getResources().getString(
                    R.string.bmv_join_vtv3_false));
            dialog.show();
            Log.v(TAG, "Da xau dialog join");
            dialog.setOnConfirmListener(new OnConfirmListener() {

                @Override
                public void onClickYesListener() {
                    dialog.dismiss();
                    mMainActivity.mLoginType = Constants.LOGIN_AUTO;
                    sendRequesJoinVtv3();
                }

                @Override
                public void onClickNoListener() {
                    dialog.dismiss();
                    finish();
                }
            });
        }
    }

    public void handleReQuestSubmitVtv3(Message message) {
        boolean result = IOUtility.readBoolean(message);
        if (!result) {
            showDialogSubmitAgain();
        }
    }

    public void handleRequestEndDirectVtv3(final Message message) {
        mAudioManager.playSound(AudioManager.TIME_OUT, false);
        PreferenceManager.getInstance(mMainActivity).setQuestion("");
        if (mDisplayed == true) {
            if (mResult == true) {
                mMoneyManager.nextIndex();
                mtvMoney.setText(mMoneyManager.getCurrentMoney() + "");
                mtvMoney.startAnimation(mTextAnimations[2]);
                startAnimationButtonRightOrFail(
                        mbtOptions[mQuestionManager.getAnswerCurrentQuestion()],
                        true);
                int answer = mQuestionManager.getAnswerCurrentQuestion();
                int level = mQuestionManager.getLevelCurrentQuestion();
                if (level > 4) {
                    AudioManager.getInstance().playSound(
                            AudioManager.RIGHT_A_VI + answer, false);
                } else {
                    AudioManager.getInstance().playSound(AudioManager.RIGHT_VI,
                            false);
                }
            } else {
                startAnimationButtonRightOrFail(
                        mbtOptions[mQuestionManager.getAnswerCurrentQuestion()],
                        true);
            }
        }
        mPausing = true;
        showDialogEndVtv3();
    }

    public void showDialogEndVtv3() {
        final ConfirmDialog dialog = ConfirmDialog.getInstance(mMainActivity);
        dialog.setMessage(getResources().getString(R.string.bmv_time_out));
        dialog.show();
        dialog.setOnConfirmListener(new OnConfirmListener() {

            @Override
            public void onClickYesListener() {
                dialog.dismiss();
                endGame();
            }

            @Override
            public void onClickNoListener() {
                dialog.dismiss();
                finish();
                mMainActivity.showFullStartApp();
                mMainActivity.showBannerAdmode(mMainActivity.mllBanner);
            }
        });

    }

    public void showDialogSubmitAgain() {
        final ConfirmDialog dialog = ConfirmDialog.getInstance(mMainActivity);
        dialog.setMessage(getResources().getString(R.string.dnt_unsaved));
        dialog.show();
        dialog.setOnConfirmListener(new OnConfirmListener() {

            @Override
            public void onClickYesListener() {
                dialog.dismiss();
                submitResult();
            }

            @Override
            public void onClickNoListener() {
                dialog.dismiss();
            }
        });

    }

    public void endGame() {
        mMoneyManager.resetMoney();
        mResultManager.resetResult();
        mPausing = true;
        mTime = TIMER;
        mtvTimer.setText("0");
        mtvMoney.setText("0");
        mQuestionManager.resetQuestion();
        finish();
        mMainActivity.switchContent(new IndirectVTV3Fragment(),
                IndirectVTV3Fragment.TAG, true, null);
    }

    public void handleRequestCheckOutDirectVtv3(Message message) {
        boolean out = IOUtility.readBoolean(message);
        if (out) {
            mHandler.removeCallbacksAndMessages(null);
            finish();
        } else {
            sendRequesCheckOut();
        }
    }

    public void onRightAnswer(final Message message) {
        mtvMoney.setText(MoneyManager.parseMoney(mMoney));
        mtvMoney.startAnimation(mTextAnimations[2]);
        int answer = mQuestionManager.getAnswerCurrentQuestion();
        startAnimationButtonRightOrFail(
                mbtOptions[mQuestionManager.getAnswerCurrentQuestion()], true);
        if (AiLaTrieuPhuActivity.mEnglish) {
            if (mQuestionManager.getLevelCurrentQuestion() >= 5) {
                AudioManager.getInstance().playSound(
                        AudioManager.RIGHT_A_EN + 2 * answer, false);
            } else {
                AudioManager.getInstance().playSound(AudioManager.RIGHT_EN,
                        false);
            }
        } else {
            if (mQuestionManager.getLevelCurrentQuestion() >= 5) {
                AudioManager.getInstance().playSound(
                        AudioManager.RIGHT_A_VI + 2 * answer, false);
            } else {
                AudioManager.getInstance().playSound(AudioManager.RIGHT_VI,
                        false);
            }
        }
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                displayQuestionDirectVtv3(message);
            }
        }, DELAY_NEXT_QUESTION);
    }

    public void onFailAnswer(final Message message) {
        int rightAnswer = mQuestionManager.getAnswerCurrentQuestion();
        if (AiLaTrieuPhuActivity.mEnglish) {
            mAudioManager.playSound(AudioManager.FAIL_A_EN + 2 * rightAnswer,
                    false);
        } else {
            mAudioManager.playSound(AudioManager.FAIL_A_VI + 2 * rightAnswer,
                    false);
        }
        startAnimationButtonRightOrFail(
                mbtOptions[mQuestionManager.getAnswerCurrentQuestion()], true);
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                displayQuestionDirectVtv3(message);
            }
        }, DELAY_NEXT_QUESTION);
    }

    public void handleRequestQuestionsDirectVtv3(final Message message) {
        ConfirmDialog.getInstance(mMainActivity).dismiss();
        if (mDisplayed == true) {
            displayInformPlayer();
            if (mResult == true) {
                onRightAnswer(message);
            } else {
                onFailAnswer(message);
            }
        } else {
            if (AiLaTrieuPhuActivity.mEnglish) {
                mAudioManager.playSound(AudioManager.START_EN, false);
            } else {
                mAudioManager.playSound(AudioManager.START_VI, false);
            }
            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    displayQuestionDirectVtv3(message);
                }
            }, DELAY_NEXT_QUESTION);
        }
    }

    public void displayQuestionDirectVtv3(Message message) {
        mQuestionManager.parseQuestionDirectVtv3(message);
        mResult = false;
        mDisplayed = true;
        displayContent();
    }

    public void handleRequestGetRankPlayerVtv3(Message message) {
        int size = IOUtility.readInt(message);
        RankPlayerVtv3Adapter rankPlayerVtv3Adapter = (RankPlayerVtv3Adapter) mlvWeek
                .getAdapter();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                RankPlayerModel rankPlayerModel = new RankPlayerModel();
                rankPlayerModel.setName(IOUtility.readString(message));
                String link = IOUtility.readString(message);
                if (link.length() > 0) {
                    link = "https://graph.facebook.com/" + link
                            + "/picture?type=large&width=200&height=200";
                }
                rankPlayerModel.setLink(link);
                rankPlayerModel.setScore(IOUtility.readLong(message));
                rankPlayerVtv3Adapter.add(rankPlayerModel);
            }
        }
        rankPlayerVtv3Adapter.notifyDataSetChanged();
    }

    @Override
    public void onFailed() {
        final ConfirmDialog dialog = ConfirmDialog.getInstance(mMainActivity);
        dialog.setMessage(getResources().getString(R.string.bmv_connect_false));
        dialog.show();
        dialog.setOnConfirmListener(new OnConfirmListener() {

            @Override
            public void onClickYesListener() {
                dialog.dismiss();
                sendRequesJoinVtv3();
            }

            @Override
            public void onClickNoListener() {
                dialog.dismiss();
                finish();
            }
        });
    }

    public void addResulVtv3Direct(boolean result) {
        mResultManager.addResulVtv3Direct(
                mQuestionManager.getIdCurrentQuestion(),
                mQuestionManager.getLevelCurrentQuestion(), TIMER - mTime,
                result);
    }

    @Override
    public int getViewId() {
        return R.layout.fragment_vtv3;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
    }

    @Override
    public void handleRight(Button button) {
        mtvQuestion.setText(getResources().getString(
                R.string.bmv_question_direct));
        mResult = true;
        addResulVtv3Direct(true);
        submitResult();
        mMoney = mMoneyLast - mMoneyFirst;
    }

    @Override
    public void handleFail(Button button) {
        mtvQuestion.setVisibility(TextView.VISIBLE);
        mtvQuestion.setText(getResources().getString(
                R.string.bmv_question_direct));
        mResult = false;
        addResulVtv3Direct(false);
        submitResult();
    }

    @Override
    public void timeOut() {
        setEnableAllButton(false);
        mAudioManager.playSound(AudioManager.TIME_OUT, false);
        for (int i = 0; i < mbtOptions.length; i++) {
            mbtOptions[i].setBackgroundResource(R.drawable.btn_option);
        }
        mResult = false;
        mPausing = true;
        mtvQuestion.setVisibility(TextView.VISIBLE);
        mtvQuestion.setText(getResources().getString(
                R.string.bmv_question_direct));
        mtvTimer.setText("0");
    }

    @Override
    public void handleAnswer(Button button, int answer) {
        boolean result = mQuestionManager.submitAnswer(answer);
        if (result == true) {
            handleRight(button);
        } else {
            handleFail(button);
        }
    }

    @Override
    public void initExtendedVariables() {
        mDisplayed = false;
        mResult = false;
        sendRequesJoinVtv3();
    }

    @Override
    public void initExtendedViews(View view) {
        mMainActivity.mllBanner.removeAllViews();
        mMoneyManager.resetMoney();
        mResultManager.resetResult();
        onCreatFormPlayer(view);
        mlvWeek = (HorizontalListView) view
                .findViewById(R.id.list_player_rank_vtv3);
        setEnableAllButton(false);
        ArrayList<RankPlayerModel> rankPlayerModels = new ArrayList<RankPlayerModel>();
        mlvWeek.setAdapter(new RankPlayerVtv3Adapter(mMainActivity, 0, this,
                rankPlayerModels));
        mtvQuestion.setText(getResources().getString(
                R.string.bmv_question_direct));
        mtvQuestion.setVisibility(TextView.VISIBLE);
        mtvMoney.setText(MoneyManager.parseMoney(mMoney));

    }

    @Override
    public void setEnableAllButton(boolean enabled) {
        setEnableOptionButton(enabled);
    }

    public HorizontalListView getListView() {
        return mlvWeek;
    }

    @Override
    public void confirmFinish() {
        showQuitGameDialog();
    }

    private void showQuitGameDialog() {
        final ConfirmDialog dialog = ConfirmDialog.getInstance(mMainActivity);
        dialog.setMessage(getResources().getString(R.string.dnt_quit));
        dialog.show();
        dialog.setOnConfirmListener(new OnConfirmListener() {

            @Override
            public void onClickYesListener() {
                dialog.dismiss();
                sendRequesCheckOut();
                mPausing = true;
                mMainActivity.showFullStartApp();
                mMainActivity.showBannerAdmode(mMainActivity.mllBanner);
            }

            @Override
            public void onClickNoListener() {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void displayContent() {
        String source = PreferenceManager.getInstance(mMainActivity)
                .getQuestion();
        if (TimeQuestionModel.getInstance().fromString(source)) {
            if ((mQuestionManager.getIdCurrentQuestion()) == Integer
                    .parseInt(TimeQuestionModel.getInstance().getId())) {
                long timeLast = System.currentTimeMillis();
                long firstTime = Long.parseLong(TimeQuestionModel.getInstance()
                        .getTime());
                long t = timeLast - firstTime;
                if (t >= 30000) {
                    timer = -1;
                } else {
                    timer = (int) (30 - (t) / 1000);
                }
            } else {
                saveQuestion();
            }
        } else {
            saveQuestion();
        }
        displayQuestion(timer);
        if (timer < 0) {
            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    setEnableOptionButton(false);
                }
            }, 1100);
        }
        int lever = mQuestionManager.getLevelCurrentQuestion();
        mMoneyManager.setIndex(lever - 1);
        mMoneyFirst = mMoneyManager.getCurrentMoney();
        mMoneyLast = mMoneyManager.getNextMoney();
        mtvMoney.setText(MoneyManager.parseMoney(mMoney));
        mtvIndex.setText(mQuestionManager.getLevelCurrentQuestion() + "");
    }

    public void saveQuestion() {
        timer = TIMER;
        mIdQuestion = Integer.toString(mQuestionManager.getIdCurrentQuestion());
        TimeQuestionModel.getInstance().setmId(mIdQuestion);
        long time = System.currentTimeMillis();
        mTimeQuestion = Long.toString(time);
        TimeQuestionModel.getInstance().setTime(mTimeQuestion);
        PreferenceManager.getInstance(mMainActivity).setQuestion(
                TimeQuestionModel.getInstance().toString());
    }

    @Override
    public void submitResult() {
        Message message = SendData.requestSubmitVtv3DirectResult(mResultManager
                .getResultModel());
        mMainActivity.sendRequest(message, false, true);
    }

    @Override
    public void startGame() {

    }

    @Override
    public void finishGame() {
        releaseGame();
        mHandler.removeCallbacksAndMessages(null);
        finish();
    }

    @Override
    public void requestQuestion() {

    }
}
