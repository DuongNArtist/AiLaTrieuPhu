package com.skynet.ailatrieuphu.fragments;

import java.util.ArrayList;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.devsmart.android.ui.HorizontalListView;
import com.skynet.ailatrieuphu.R;
import com.skynet.ailatrieuphu.activities.AiLaTrieuPhuActivity;
import com.skynet.ailatrieuphu.adapters.WeekPlayVTV3Adapter;
import com.skynet.ailatrieuphu.audios.AudioManager;
import com.skynet.ailatrieuphu.dialogs.ConfirmDialog;
import com.skynet.ailatrieuphu.dialogs.LoadingDialog;
import com.skynet.ailatrieuphu.dialogs.MessageDialog;
import com.skynet.ailatrieuphu.dialogs.OnBackListener;
import com.skynet.ailatrieuphu.dialogs.OnConfirmListener;
import com.skynet.ailatrieuphu.managers.MoneyManager;
import com.skynet.ailatrieuphu.models.ResultModel;
import com.skynet.ailatrieuphu.models.WeekModel;
import com.skynet.ailatrieuphu.sockets.IOUtility;
import com.skynet.ailatrieuphu.sockets.Message;
import com.skynet.ailatrieuphu.sockets.Protocol;
import com.skynet.ailatrieuphu.sockets.SendData;

public class IndirectVTV3Fragment extends PlayFragment implements
        OnClickListener {

    public static final String TAG = IndirectVTV3Fragment.class.getSimpleName();
    public int mPosition = -1;
    private int mPreview = -1;
    public TextView mtvNameWeek;
    private HorizontalListView mlvWeek;
    private boolean mResult;
    private int mWeekPage;
    private boolean mParsed = false;
    public boolean mEndQuestion = false;
    public boolean mBackClick = false;
    public boolean mClickWeekOk = false;
    private boolean mConnect = true;
    private boolean mSendResult = false;
    private boolean mNoWeek = false;
    private int mSound;

    public boolean isPasered() {
        return mParsed;
    }

    public void setPasered(boolean pasered) {
        this.mParsed = pasered;
    }

    public boolean isResult() {
        return mResult;
    }

    public void setResult(boolean result) {
        this.mResult = result;
    }

    public void sendRequestGetListOfWeek() {
        Message message = SendData.requestGetListOfWeek(mWeekPage,
                PlayFragment.mLanguage);
        mMainActivity.sendRequest(message, true, false);
    }

    @Override
    public void onReceived(AiLaTrieuPhuActivity mainActivity, final Message message) {
        switch (message.getServerCommand()) {
        case Protocol.CMD_REQUEST_GET_LIST_OF_WEEK:
            handleResquestGetListOfWeek(message);
            break;

        case Protocol.CMD_REQUEST_GET_SELECTED_WEEK_PACKAGE_QUESTION:
            handRequestPackageQuestionOfWeek(message);
            break;

        case Protocol.CMD_REQUEST_SUBMIT_INDIRECT:
            handleRequestSubmit(message);
            break;

        default:
            break;
        }
    }

    public void handleSubmitOkOrFalse() {
        mSendResult = false;
        if (mEndQuestion) {
            mEndQuestion = false;
            return;
        } else if (mClickWeekOk) {
            mClickWeekOk = false;
            requestQuestion();
            return;
        } else if (mBackClick) {
            mBackClick = false;
            finish();
            mMainActivity.showFullStartApp();
            mMainActivity.showBannerAdmode(mMainActivity.mllBanner);
            return;
        }
    }

    private void handleRequestSubmit(Message message) {
        boolean result = IOUtility.readBoolean(message);
        setEnableOptionButton(false);
        if (result) {
            handleSubmitOkOrFalse();
        } else {
            LoadingDialog.getInstance(mMainActivity).show();
            showDialogSubmitAgain();
        }
    }

    public void showDialogSubmitAgain() {
        final ConfirmDialog dialog = ConfirmDialog.getInstance(mMainActivity);
        dialog.setMessage(getResources().getString(R.string.dnt_unsaved));
        dialog.show();
        dialog.setOnConfirmListener(new OnConfirmListener() {

            @Override
            public void onClickYesListener() {
                dialog.dismiss();
                sendRequestSubmitResult();
            }

            @Override
            public void onClickNoListener() {
                dialog.dismiss();
                LoadingDialog.getInstance(mMainActivity).cancel();
                handleSubmitOkOrFalse();
            }
        });

    }

    public int getPageWeek() {
        return mWeekPage;
    }

    public void setPageWeek(int pageWeek) {
        this.mWeekPage = pageWeek;
    }

    public void handRequestPackageQuestionOfWeek(Message message) {
        mEndQuestion = false;
        mMoney = 0;
        if (mQuestionManager.parsePackageQuestionIndirectVtv3(message) > 0) {
            mResultManager.setIdWeek(((WeekModel) mlvWeek.getAdapter().getItem(
                    mPosition)).getId());
            displayContent();
        } else {
            showRepickDialog();
        }
    }

    public void showRepickDialog() {
        final MessageDialog dialog = MessageDialog.getInstance(mMainActivity);
        dialog.setMessage(getResources().getString(R.string.dnt_no_question));
        dialog.show();
        dialog.setOnBackListener(new OnBackListener() {

            @Override
            public void onBack() {
                dialog.dismiss();
                if (mNoWeek) {
                    mNoWeek = false;
                    finish();
                }
            }
        });
    }

    private void handleResquestGetListOfWeek(Message message) {
        int size = IOUtility.readInt(message);
        WeekPlayVTV3Adapter weekPlayVTV3Adapter = (WeekPlayVTV3Adapter) mlvWeek
                .getAdapter();
        if (size > 0) {
            mWeekPage++;
            for (int i = 0; i < size; i++) {
                WeekModel weekModel = new WeekModel();
                weekModel.setId(IOUtility.readInt(message));
                weekModel.setName(IOUtility.readString(message));
                weekModel.setEnabled(true);
                weekPlayVTV3Adapter.add(weekModel);
            }
            weekPlayVTV3Adapter.notifyDataSetChanged();
        } else {
            mNoWeek = true;
            showRepickDialog();
        }
    }

    private void checkNextQuesion() {
        if (mQuestionManager.getCurrent() == 0) {
            mEndQuestion = true;
            mBackClick = false;
            mClickWeekOk = false;
            onFinishGame();
        } else if (mQuestionManager.getCurrent() <= 14) {
            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    displayContent();
                }
            }, 1000);
        } else if (mQuestionManager.getCurrent() == 15) {
            mAudioManager.playSound(AudioManager.AND_PASS, false);
            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    displayContent();
                }
            }, 3000);
        }
    }

    @Override
    public void onFailed() {
        mConnect = false;
        final ConfirmDialog dialog = ConfirmDialog.getInstance(mMainActivity);
        dialog.setMessage(getResources().getString(R.string.bmv_connect_false));
        dialog.show();
        dialog.setOnConfirmListener(new OnConfirmListener() {

            @Override
            public void onClickYesListener() {
                dialog.dismiss();
                if (mSendResult) {
                    sendRequestSubmitResult();
                }
                if (mClickWeekOk) {
                    requestQuestion();
                }
            }

            @Override
            public void onClickNoListener() {
                dialog.dismiss();
                finish();
            }
        });
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
    public void confirmFinish() {
        mBackClick = true;
        mClickWeekOk = false;
        final ConfirmDialog dialog = ConfirmDialog.getInstance(mMainActivity);
        dialog.setMessage(getResources().getString(R.string.dnt_quit));
        dialog.show();
        dialog.setOnConfirmListener(new OnConfirmListener() {

            @Override
            public void onClickYesListener() {
                mAudioManager.pauseSound(mSound);
                mHandler.removeCallbacksAndMessages(null);
                dialog.dismiss();
                mPausing = true;
                mPosition = -1;
                if (mMoney > 0) {
                    showResultDialog();
                } else {
                    mBackClick = false;
                    finish();
                    mMainActivity.showFullStartApp();
                    mMainActivity.showBannerAdmode(mMainActivity.mllBanner);
                }
            }

            @Override
            public void onClickNoListener() {
                dialog.dismiss();
                mAudioManager.pauseSound(mSound);
                mBackClick = false;
            }
        });
    }

    @Override
    public void handleRight(final Button button) {
        mMoney += mMoneyLast - mMoneyFirst;
        int answer = mQuestionManager.getAnswerCurrentQuestion();
        int delayMillis = 2000;
        addResult(true);
        startAnimationButtonRightOrFail(button, true);

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
        mtvMoney.setText(MoneyManager.parseMoney(mMoney));
        mtvMoney.startAnimation(mTextAnimations[2]);
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                mQuestionManager.nextQuestion();
                checkNextQuesion();
            }
        }, delayMillis);
    }

    @Override
    public void handleFail(final Button button) {
        int delay = 3000;
        AudioManager.getInstance().playSound(AudioManager.FAIL_ANSWER, false);
        addResult(false);
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                startAnimationButtonRightOrFail(button, false);
            }
        }, 1000);
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                mQuestionManager.nextQuestion();
                checkNextQuesion();
            }
        }, delay);
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
        addResult(false);
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                mQuestionManager.nextQuestion();
                checkNextQuesion();
            }
        }, 1000);
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
        mResult = false;
        mNoWeek = false;
        mWeekPage = 0;
        mParsed = false;
        sendRequestGetListOfWeek();
    }

    @Override
    public void initExtendedViews(View view) {
        mMainActivity.mllBanner.removeAllViews();
        onCreatFormPlayer(view);
        mtvNameWeek = (TextView) view
                .findViewById(R.id.tv_layout_vtv3_name_week);
        mlvWeek = (HorizontalListView) view
                .findViewById(R.id.list_player_rank_vtv3);
        setEnableAllButton(false);
        ArrayList<WeekModel> weekModels = new ArrayList<WeekModel>();
        mlvWeek.setAdapter(new WeekPlayVTV3Adapter(mMainActivity, 0, this,
                weekModels));
        mlvWeek.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    final int position, long id) {
                mAudioManager.playSound(AudioManager.TOUCH, false);
                mClickWeekOk = true;
                mBackClick = false;
                mPreview = position;
                if (mPosition == position) {
                    return;
                } else if (mPreview == position) {
                    final WeekPlayVTV3Adapter adapter = (WeekPlayVTV3Adapter) mlvWeek
                            .getAdapter();
                    final ConfirmDialog dialog = ConfirmDialog
                            .getInstance(mMainActivity);
                    dialog.setMessage(getResources().getString(
                            R.string.bmv_selected_week,
                            adapter.getItem(position).getName()));
                    dialog.show();
                    adapter.getItem(mPreview).setSelected(true);
                    adapter.notifyDataSetChanged();

                    dialog.setOnConfirmListener(new OnConfirmListener() {

                        @Override
                        public void onClickYesListener() {
                            dialog.dismiss();
                            mPausing = true;
                            mPosition = position;
                            mHandler.removeCallbacksAndMessages(null);
                            for (int i = 0; i < adapter.getCount(); i++) {
                                if (adapter.getItem(i) != adapter
                                        .getItem(mPreview)) {
                                    adapter.getItem(i).setSelected(false);
                                }
                            }
                            adapter.notifyDataSetChanged();
                            if (mParsed) {
                                if (mMoney > 0) {
                                    showResultDialog();
                                } else {
                                    requestQuestion();
                                }
                            } else {
                                mClickWeekOk = false;
                                requestQuestion();
                            }
                        }

                        @Override
                        public void onClickNoListener() {
                            dialog.dismiss();
                            adapter.getItem(mPreview).setSelected(false);
                            adapter.notifyDataSetChanged();
                            mClickWeekOk = false;
                        }
                    });
                }
            }
        });
    }

    public void showResultDialog() {
        final ConfirmDialog dialog = ConfirmDialog.getInstance(mMainActivity);
        if (mQuestionManager.getCurrent() > 0) {
            if (AiLaTrieuPhuActivity.mEnglish) {
                mSound = AudioManager.THANKS_EN;
            } else {
                mSound = AudioManager.THANKS_VI;
            }
        } else {
            mSound = AudioManager.VICTORY_VI;
        }
        mAudioManager.playSound(mSound, false);
        String finalMoney = "";
        finalMoney = MoneyManager.parseMoney(mMoney);
        String message = getResources()
                .getString(R.string.dnt_save, finalMoney);
        dialog.setMessage(message);
        dialog.show();
        dialog.setOnConfirmListener(new OnConfirmListener() {

            @Override
            public void onClickYesListener() {
                mAudioManager.pauseSound(mSound);
                dialog.dismiss();
                mMoney = 0;
                sendRequestSubmitResult();
            }

            @Override
            public void onClickNoListener() {
                dialog.dismiss();
                mAudioManager.pauseSound(mSound);
                mMoney = 0;
                if (mEndQuestion) {
                    mEndQuestion = false;
                    return;
                } else if (mClickWeekOk) {
                    mClickWeekOk = false;
                    requestQuestion();
                } else if (mBackClick) {
                    mBackClick = false;
                    finish();
                    mMainActivity.showFullStartApp();
                    mMainActivity.showBannerAdmode(mMainActivity.mllBanner);
                }
            }
        });
    }

    @Override
    public void startGame() {
        mMoneyManager.resetMoney();
        mResultManager.resetResult();
        mPausing = true;
        mTime = TIMER;
        mWeekPage = 0;
        mtvQuestion.setText(R.string.bmv_question_indirect);
        mtvQuestion.setVisibility(TextView.VISIBLE);
        mtvMoney.setText("0");
    }

    public void resetGame() {
        mMoneyManager.resetMoney();
        mResultManager.resetResult();
        mPausing = true;
        mTime = TIMER;
        mtvTimer.setText("0");
        mtvMoney.setText("0");
        mQuestionManager.resetQuestion();
        clearQuestion();
        mtvQuestion.setVisibility(TextView.VISIBLE);
        mtvQuestion.setText(getResources().getString(
                R.string.bmv_question_indirect));
    }

    public void onFinishGame() {
        clearQuestion();
        if (mConnect) {
            mPosition = -1;
            mClickWeekOk = false;
        }
        mtvQuestion.setVisibility(TextView.VISIBLE);
        mtvQuestion.setText(getResources().getString(
                R.string.bmv_question_indirect));
        mtvTimer.setText(0 + "");
        mtvMoney.setText("0");
        mPausing = true;
        mParsed = false;
        setEnableAllButton(false);
        if (mMoney > 0) {
            showResultDialog();
        }

    }

    public void sendRequestSubmitResult() {
        mSendResult = true;
        int id = mResultManager.getIdWeek();
        ArrayList<ResultModel> resultModels = mResultManager.getResultModels();
        Message message = SendData.requestSubmitVtv3InDirectResult(id,
                resultModels);
        mMainActivity.sendRequest(message, true, true);
    }

    @Override
    public void setEnableAllButton(boolean enabled) {
        setEnableOptionButton(enabled);
    }

    public HorizontalListView getListView() {
        return mlvWeek;
    }

    @Override
    public void finishGame() {

    }

    @Override
    public void displayContent() {
        if (mQuestionManager.getCurrent() == 0) {
            if (!AiLaTrieuPhuActivity.mEnglish) {
                mAudioManager.playSound(AudioManager.START_VI, false);
            } else {
                mAudioManager.playSound(AudioManager.START_EN, false);
            }
            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    displayQuestion(TIMER);
                }
            }, 1500);
        } else {
            displayQuestion(TIMER);
        }
        displayInformPlayer();
        mtvMoney.setText(MoneyManager.parseMoney(mMoney));
        mParsed = true;

    }

    @Override
    public void requestQuestion() {
        mParsed = false;
        mMoney = 0;
        resetGame();
        Message message = SendData
                .requestGetSelectedWeekPackageQuestion(((WeekModel) mlvWeek
                        .getAdapter().getItem(mPosition)).getId());
        mMainActivity.sendRequest(message, true, false);
        mtvNameWeek.setText(getResources().getString(R.string.dnt_week)
                + " "
                + ((WeekModel) mlvWeek.getAdapter().getItem(mPosition))
                        .getName());
    }

    public void handleCloseDialogLogin() {
        if (mClickWeekOk) {
            requestQuestion();
        } else if (mBackClick) {
            finish();
        }
    }

    @Override
    public void submitResult() {

    }
}
