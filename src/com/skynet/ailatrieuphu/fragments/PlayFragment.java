package com.skynet.ailatrieuphu.fragments;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.skynet.ailatrieuphu.R;
import com.skynet.ailatrieuphu.activities.AiLaTrieuPhuActivity;
import com.skynet.ailatrieuphu.audios.AudioManager;
import com.skynet.ailatrieuphu.dialogs.BaseDialog;
import com.skynet.ailatrieuphu.dialogs.ConfirmDialog;
import com.skynet.ailatrieuphu.dialogs.LoadingDialog;
import com.skynet.ailatrieuphu.dialogs.OnConfirmListener;
import com.skynet.ailatrieuphu.managers.MoneyManager;
import com.skynet.ailatrieuphu.managers.QuestionManager;
import com.skynet.ailatrieuphu.managers.ResultManager;
import com.skynet.ailatrieuphu.models.QuestionModel;

public abstract class PlayFragment extends BaseFragment implements Runnable,
        OnClickListener {

    public static final int[] OPTION_IDS = { R.id.bt_play_option_0,
            R.id.bt_play_option_1, R.id.bt_play_option_2, R.id.bt_play_option_3 };
    public static final int[] BUTTON_ANIM_IDS = { R.anim.anim_option_left,
            R.anim.anim_option_right };
    public static final int[] TEXT_ANIM_IDS = { R.anim.anim_text_timer,
            R.anim.anim_text_index, R.anim.anim_text_money };

    public static String mLanguage;

    protected static final int TIMER = 30;
    protected static final int WARNING = 10;
    protected static final int PERIOD = 1000;

    protected boolean mPlaying;
    protected boolean mPausing;
    protected boolean mReplaying;
    protected boolean mStopping;

    protected ArrayList<BaseDialog> mBaseDialogs;
    protected Animation[] mButtonAnimations;
    protected Animation[] mTextAnimations;
    protected Button[] mbtOptions;
    protected QuestionManager mQuestionManager;
    protected ResultManager mResultManager;
    protected MoneyManager mMoneyManager;
    protected MediaPlayer mMediaPlayer;
    protected ImageView mivQuestion;
    protected VideoView mvvQuestion;
    protected TextView mtvQuestion;
    protected TextView mtvCaption;
    protected TextView mtvTimer;
    protected TextView mtvIndex;
    protected TextView mtvMoney;
    protected Thread mThread;
    protected int mTime;

    protected int mMoneyFirst = 0;
    protected int mMoneyLast = 0;
    protected int mMoney = 0;

    protected ImageView mivAvatar;
    protected TextView mtvNamePlayer;
    protected TextView mtvAnswerPlayer;
    protected TextView mtvQuestionNumber;

    public boolean isPlaying() {
        return mPlaying;
    }

    public void setPlaying(boolean playing) {
        this.mPlaying = playing;
    }

    public boolean isPausing() {
        return mPausing;
    }

    public void setPausing(boolean pausing) {
        this.mPausing = pausing;
    }

    public boolean isReplaying() {
        return mReplaying;
    }

    public void setReplaying(boolean replaying) {
        this.mReplaying = replaying;
    }

    public boolean isStopping() {
        return mStopping;
    }

    public void setStopping(boolean stopping) {
        this.mStopping = stopping;
    }

    public void onCreatFormPlayer(View view) {
        mivAvatar = (ImageView) view.findViewById(R.id.img_player_avatar);
        mtvAnswerPlayer = (TextView) view
                .findViewById(R.id.tv_player_question_answer);
        mtvQuestionNumber = (TextView) view
                .findViewById(R.id.tv_player_question_numble);
        mtvNamePlayer = (TextView) view.findViewById(R.id.tv_player_name);
    }

    public void displayInformPlayer() {
        if (mQuestionManager.getAnswerPlayerQuestion()) {
            mtvAnswerPlayer.setText(getResources().getString(
                    R.string.bmv_answer_player_true));
            mtvAnswerPlayer.startAnimation(mTextAnimations[2]);
        } else {
            mtvAnswerPlayer.setText(getResources().getString(
                    R.string.bmv_answer_player_false));
        }
        mtvQuestionNumber.setText(getResources().getString(
                R.string.bmv_question_numble)
                + mQuestionManager.getLevelCurrentQuestion());
        mtvNamePlayer.setText(mQuestionManager.getNamePlayerQuestion());
        ImageLoader.getInstance().displayImage(
                mQuestionManager.getAvatarPlayerQuestion(), mivAvatar,
                mDisplayImageOptions);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlaying = false;
    }

    @Override
    public void createVariables() {
        initBaseVariables();
        initExtendedVariables();
    }

    public void initBaseVariables() {
        mBaseDialogs = new ArrayList<BaseDialog>();
        mButtonAnimations = new Animation[BUTTON_ANIM_IDS.length];
        for (int index = 0; index < mButtonAnimations.length; index++) {
            mButtonAnimations[index] = AnimationUtils.loadAnimation(
                    mMainActivity, BUTTON_ANIM_IDS[index]);
        }
        mTextAnimations = new Animation[TEXT_ANIM_IDS.length];
        for (int index = 0; index < mTextAnimations.length; index++) {
            mTextAnimations[index] = AnimationUtils.loadAnimation(
                    mMainActivity, TEXT_ANIM_IDS[index]);
        }
        mTime = TIMER;
        mPlaying = true;
        mPausing = true;
        mThread = new Thread(this);
        mMoneyManager = new MoneyManager(getResources());
        mQuestionManager = new QuestionManager();
        mResultManager = new ResultManager();
    }

    @Override
    public void createViews(View view) {
        initBaseViews(view);
        initExtendedViews(view);
        startGame();
        mThread.start();
    }

    public void initBaseViews(View view) {
        mtvCaption = (TextView) view.findViewById(R.id.tv_play_caption);
        mtvQuestion = (TextView) view.findViewById(R.id.tv_play_question);
        mivQuestion = (ImageView) view.findViewById(R.id.iv_play_question);
        mvvQuestion = (VideoView) view.findViewById(R.id.vv_play_question);
        mtvTimer = (TextView) view.findViewById(R.id.tv_play_timer);
        mtvIndex = (TextView) view.findViewById(R.id.tv_play_index);
        mtvMoney = (TextView) view.findViewById(R.id.tv_play_money);
        mbtOptions = new Button[4];
        for (int i = 0; i < mbtOptions.length; i++) {
            mbtOptions[i] = (Button) view.findViewById(OPTION_IDS[i]);
            mbtOptions[i].setOnClickListener(this);
        }
    }

    public void resetBaseTextView() {
        mtvTimer.setText("30s");
        mtvIndex.setText(mQuestionManager.getCurrent() + "");
        mtvMoney.setText(mMoneyManager.getStringCurrentMoney());
    }

    public void displayQuestion(int startTime) {
        mPausing = true;
        setEnableAllButton(false);
        clearQuestion();
        int level = mQuestionManager.getLevelCurrentQuestion() - 1;
        mMoneyManager.setIndex(level);
        mMoneyFirst = mMoneyManager.getCurrentMoney();
        mMoneyLast = mMoneyManager.getNextMoney();
        if (AiLaTrieuPhuActivity.mEnglish) {
            mAudioManager.playSound(AudioManager.QUESTION_00_EN + level * 2,
                    false);
        } else {
            mAudioManager.playSound(AudioManager.QUESTION_00_VI + level * 2,
                    false);
        }
        if (mQuestionManager.getCurrent() > 0) {
            for (int index = 0; index < mbtOptions.length; index++) {
                startAnimationOptionButtonBeforeDisplayContent(index);
            }
        }
        mTime = startTime;
        switch (mQuestionManager.getCategoryIdCurrentQuestion()) {
        case QuestionManager.TYPE_TEXT:
            displayTextQuestion(mQuestionManager.getQuestionModel());
            break;

        case QuestionManager.TYPE_IMAGE:
            displayPictureQuestion(mQuestionManager.getQuestionModel());
            break;

        case QuestionManager.TYPE_AUDIO:
            displayAudioQuestion(mQuestionManager.getQuestionModel());
            break;

        case QuestionManager.TYPE_VIDEO:
            displayVideoQuestion(mQuestionManager.getQuestionModel());
            break;

        default:
            break;
        }
        for (int index = 0; index < mbtOptions.length; index++) {
            mbtOptions[index].setVisibility(Button.VISIBLE);
            mbtOptions[index].setBackgroundResource(R.drawable.btn_option);
            mbtOptions[index].setText(mQuestionManager
                    .getOptionCurrentQuestion(index));
        }
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                setEnableAllButton(true);
            }
        }, 1000);
        mtvIndex.setText((level + 1) + "");
        mtvIndex.startAnimation(mTextAnimations[1]);
        mPausing = false;
    }

    public void clearQuestion() {
        resetBaseTextView();
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        if (mvvQuestion.isPlaying()) {
            mvvQuestion.stopPlayback();
        }
        mtvQuestion.setText("");
        mtvCaption.setText("");
        for (Button button : mbtOptions) {
            button.setText("");
        }
        mtvCaption.setVisibility(TextView.GONE);
        mtvQuestion.setVisibility(TextView.GONE);
        mivQuestion.setVisibility(ImageView.GONE);
        mvvQuestion.setVisibility(VideoView.GONE);
    }

    public void releaseGame() {
        LoadingDialog.getInstance(mMainActivity).show();
        mPausing = true;
        mHandler.removeCallbacksAndMessages(null);
        clearQuestion();
        setEnableAllButton(false);
        mAudioManager.releaseAllSound();
        mAudioManager.init(mMainActivity);
        mAudioManager.loadAllSounds();
        LoadingDialog.getInstance(mMainActivity).cancel();
    }

    private void displayTextQuestion(QuestionModel questionModel) {
        mtvQuestion.setText(questionModel.getQuestion());
        mtvQuestion.setVisibility(TextView.VISIBLE);
    }

    private void displayPictureQuestion(QuestionModel questionModel) {
        mtvCaption.setVisibility(TextView.VISIBLE);
        mtvCaption.setText(questionModel.getQuestion());
        mivQuestion.setVisibility(ImageView.VISIBLE);
        mImageLoader.displayImage(questionModel.getLink(), mivQuestion,
                mDisplayImageOptions, new ImageLoadingListener() {

                    @Override
                    public void onLoadingStarted(String arg0, View arg1) {
                        mPausing = true;
                    }

                    @Override
                    public void onLoadingFailed(String arg0, View arg1,
                            FailReason arg2) {
                        mPausing = false;
                        mivQuestion.setVisibility(ImageView.GONE);
                        mtvQuestion.setText(getResources().getString(
                                R.string.dnt_unloaded));
                    }

                    @Override
                    public void onLoadingComplete(String arg0, View arg1,
                            Bitmap arg2) {
                        mPausing = false;
                    }

                    @Override
                    public void onLoadingCancelled(String arg0, View arg1) {
                        mPausing = true;
                    }
                });
    }

    private void displayAudioQuestion(QuestionModel questionModel) {
        mtvCaption.setVisibility(TextView.VISIBLE);
        mtvCaption.setText(questionModel.getQuestion());
        mivQuestion.setVisibility(ImageView.VISIBLE);
        mivQuestion.setImageResource(R.drawable.bkg_audio);
        try {
            mPausing = true;
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(questionModel.getLink());
            mMediaPlayer.setOnPreparedListener(new OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer.start();
                    mPausing = false;
                    int delayMillis = 29000;
                    mHandler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            if (mMediaPlayer != null) {
                                mMediaPlayer.pause();
                                mMediaPlayer.release();
                                mMediaPlayer = null;
                            }
                        }
                    }, delayMillis);
                }
            });
            mMediaPlayer.prepare();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void displayVideoQuestion(QuestionModel questionModel) {
        mtvCaption.setVisibility(TextView.VISIBLE);
        mtvCaption.setText(questionModel.getQuestion());
        mvvQuestion.setVisibility(VideoView.VISIBLE);
        try {
            Uri uri = Uri.parse(questionModel.getLink());
            mvvQuestion.setVideoURI(uri);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        mvvQuestion.requestFocus();
        mvvQuestion.setOnPreparedListener(new OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                mvvQuestion.start();
            }
        });
    }

    public void startAnimationOptionButtonBeforeDisplayContent(final int index) {
        mbtOptions[index].setVisibility(Button.VISIBLE);
        mbtOptions[index].startAnimation(mButtonAnimations[index
                % mButtonAnimations.length]);
    }

    public void setEnableOptionButton(boolean enabled) {
        for (Button button : mbtOptions) {
            button.setEnabled(enabled);
        }
    }

    public void confirmSubmitAnswer(final Button button, final int answer) {
        mPausing = true;
        setEnableAllButton(false);
        button.setBackgroundResource(R.drawable.btn_selected);
        boolean isConfirmed = mQuestionManager.isConfirmed();
        if (isConfirmed) {
            showSubmitAnswerConfirmDialog(button, answer);
        } else {
            waitAndSubmitAnswer(button, answer);
        }
    }

    public void waitAndSubmitAnswer(final Button button, final int answer) {
        mHandler.removeCallbacksAndMessages(null);
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        if (mQuestionManager.getLevelCurrentQuestion() % 5 == 0) {
            int delayMillis = 0;
            if (AiLaTrieuPhuActivity.mEnglish) {
                mAudioManager.playSound(AudioManager.WAIT_EN, false);
                delayMillis = 6000;
            } else {
                mAudioManager.playSound(AudioManager.WAIT_VI, false);
                delayMillis = 3000;
            }
            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    handleAnswer(button, answer);
                }
            }, delayMillis);
        } else {
            handleAnswer(button, answer);
        }
    }

    private void showSubmitAnswerConfirmDialog(final Button button,
            final int answer) {
        mPausing = false;
        final ConfirmDialog dialog = ConfirmDialog.getInstance(mMainActivity);
        dialog.setOnConfirmListener(new OnConfirmListener() {

            @Override
            public void onClickYesListener() {
                dialog.dismiss();
                mPausing = true;
                setEnableAllButton(false);
                waitAndSubmitAnswer(button, answer);
            }

            @Override
            public void onClickNoListener() {
                dialog.dismiss();
                setEnableAllButton(true);
                button.setBackgroundResource(R.drawable.btn_option);
            }
        });
        String message = getResources().getString(R.string.dnt_final,
                (char) (answer + 65));
        dialog.setMessage(message);
        dialog.show();
    }

    public void startAnimationButtonRightOrFail(final Button button,
            boolean right) {
        final long delayMillis = 1000;
        int resId = R.drawable.btn_false;
        if (right) {
            resId = R.drawable.btn_true;
        }
        button.setBackgroundResource(resId);
        AnimationDrawable animationDrawable = (AnimationDrawable) button
                .getBackground();
        animationDrawable.start();
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                for (Button button : mbtOptions) {
                    button.setBackgroundResource(R.drawable.btn_option);
                }
            }
        }, delayMillis);
    }

    public void addResult(boolean result) {
        int id = mQuestionManager.getIdCurrentQuestion();
        int level = mQuestionManager.getLevelCurrentQuestion();
        mResultManager.addResult(id, level, TIMER - mTime, result);
    }

    @Override
    public void onClick(View view) {
        mAudioManager.playSound(AudioManager.TOUCH, false);
        switch (view.getId()) {
        case R.id.bt_play_option_0:
            confirmSubmitAnswer(mbtOptions[0], 0);
            break;

        case R.id.bt_play_option_1:
            confirmSubmitAnswer(mbtOptions[1], 1);
            break;

        case R.id.bt_play_option_2:
            confirmSubmitAnswer(mbtOptions[2], 2);
            break;

        case R.id.bt_play_option_3:
            confirmSubmitAnswer(mbtOptions[3], 3);
            break;

        default:
            break;
        }
    }

    @Override
    public void run() {
        while (mPlaying) {
            long startTime = System.currentTimeMillis();
            if (!mPausing) {
                mTime--;
                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        if (mTime < 0) {
                            mPausing = true;
                            ConfirmDialog.getInstance(mMainActivity).dismiss();
                            timeOut();
                        } else {
                            updateTimer();
                        }
                    }
                });
            }
            long finishTime = System.currentTimeMillis();
            long elapsedTime = finishTime - startTime;
            long sleepTime = PERIOD - elapsedTime;
            try {
                Thread.sleep(sleepTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateTimer() {
        int color = Color.WHITE;
        if (mTime <= WARNING) {
            if (mTime % 2 == 0 && mTime > 0) {
                color = Color.RED;
                mAudioManager.playSound(AudioManager.AND_COUNT, false);
            }
            mtvTimer.startAnimation(mTextAnimations[0]);
        }
        mtvTimer.setTextColor(color);
        mtvTimer.setText(mTime + "s");
    }

    public abstract void initExtendedVariables();

    public abstract void initExtendedViews(View view);

    public abstract void startGame();

    public abstract void requestQuestion();

    public abstract void displayContent();

    public abstract void finishGame();

    public abstract void timeOut();

    public abstract void handleAnswer(Button button, int answer);

    public abstract void handleRight(Button button);

    public abstract void handleFail(Button button);

    public abstract void submitResult();

    public abstract void setEnableAllButton(boolean enabled);

    public QuestionManager getQuestionManager() {
        return mQuestionManager;
    }

    public void setQuestionManager(QuestionManager mQuestionManager) {
        this.mQuestionManager = mQuestionManager;
    }

    public ResultManager getResultManager() {
        return mResultManager;
    }

    public void setResultManager(ResultManager mResultManager) {
        this.mResultManager = mResultManager;
    }

    public MoneyManager getMoneyManager() {
        return mMoneyManager;
    }

    public void setMoneyManager(MoneyManager mMoneyManager) {
        this.mMoneyManager = mMoneyManager;
    }

}
