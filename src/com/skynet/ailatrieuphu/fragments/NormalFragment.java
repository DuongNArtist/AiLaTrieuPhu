package com.skynet.ailatrieuphu.fragments;

import java.util.ArrayList;
import java.util.Random;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.skynet.ailatrieuphu.R;
import com.skynet.ailatrieuphu.activities.AiLaTrieuPhuActivity;
import com.skynet.ailatrieuphu.adapters.BuddyAdapter;
import com.skynet.ailatrieuphu.adapters.LanguageAdapter;
import com.skynet.ailatrieuphu.audios.AudioManager;
import com.skynet.ailatrieuphu.dialogs.AdvisorDialog;
import com.skynet.ailatrieuphu.dialogs.AudienceDialog;
import com.skynet.ailatrieuphu.dialogs.BuddyDialog;
import com.skynet.ailatrieuphu.dialogs.ConfirmDialog;
import com.skynet.ailatrieuphu.dialogs.LanguageDialog;
import com.skynet.ailatrieuphu.dialogs.MessageDialog;
import com.skynet.ailatrieuphu.dialogs.OnBackListener;
import com.skynet.ailatrieuphu.dialogs.OnConfirmListener;
import com.skynet.ailatrieuphu.dialogs.SelectorDialog;
import com.skynet.ailatrieuphu.dialogs.SurveyDialog;
import com.skynet.ailatrieuphu.managers.DatabaseManager;
import com.skynet.ailatrieuphu.managers.HelperManager;
import com.skynet.ailatrieuphu.models.QuestionModel;
import com.skynet.ailatrieuphu.models.ResultModel;
import com.skynet.ailatrieuphu.sockets.IOUtility;
import com.skynet.ailatrieuphu.sockets.Message;
import com.skynet.ailatrieuphu.sockets.Protocol;
import com.skynet.ailatrieuphu.sockets.SendData;

public class NormalFragment extends PlayFragment {

	public static final String TAG = NormalFragment.class.getSimpleName();

	public static final int[] HELPER_IDS = { R.id.bt_fragment_normal_helper_0,
			R.id.bt_fragment_normal_helper_1, R.id.bt_fragment_normal_helper_2,
			R.id.bt_fragment_normal_helper_3 };
	public static final int[] DISABLE_HELPER_IDS = { R.drawable.btn_fifty_2,
			R.drawable.btn_buddy_2, R.drawable.btn_audience_2,
			R.drawable.btn_advisor_2 };
	public static final int[] ENABLE_HELPER_IDS = { R.drawable.btn_fifty,
			R.drawable.btn_buddy, R.drawable.btn_audience,
			R.drawable.btn_advisor };
	public static final int[] ANIM_GUIDE_IDS = { R.anim.anim_guide_right,
			R.anim.anim_guide_left };
	public static final int[] LAYOUT_IDS = { R.id.ll_guide_value_00,
			R.id.ll_guide_value_01, R.id.ll_guide_value_02,
			R.id.ll_guide_value_03, R.id.ll_guide_value_04,
			R.id.ll_guide_value_05, R.id.ll_guide_value_06,
			R.id.ll_guide_value_07, R.id.ll_guide_value_08,
			R.id.ll_guide_value_09, R.id.ll_guide_value_10,
			R.id.ll_guide_value_11, R.id.ll_guide_value_12,
			R.id.ll_guide_value_13, R.id.ll_guide_value_14 };
	public static final int[] INDEX_IDS = { R.id.tv_guide_index_00,
			R.id.tv_guide_index_01, R.id.tv_guide_index_02,
			R.id.tv_guide_index_03, R.id.tv_guide_index_04,
			R.id.tv_guide_index_05, R.id.tv_guide_index_06,
			R.id.tv_guide_index_07, R.id.tv_guide_index_08,
			R.id.tv_guide_index_09, R.id.tv_guide_index_10,
			R.id.tv_guide_index_11, R.id.tv_guide_index_12,
			R.id.tv_guide_index_13, R.id.tv_guide_index_14 };
	public static final int[] VALUE_IDS = { R.id.tv_guide_value_00,
			R.id.tv_guide_value_01, R.id.tv_guide_value_02,
			R.id.tv_guide_value_03, R.id.tv_guide_value_04,
			R.id.tv_guide_value_05, R.id.tv_guide_value_06,
			R.id.tv_guide_value_07, R.id.tv_guide_value_08,
			R.id.tv_guide_value_09, R.id.tv_guide_value_10,
			R.id.tv_guide_value_11, R.id.tv_guide_value_12,
			R.id.tv_guide_value_13, R.id.tv_guide_value_14 };
	public static final int[] HELP_IDS = { R.id.iv_guide_helper_0,
			R.id.iv_guide_helper_1, R.id.iv_guide_helper_2 };

	private HelperManager mHelperManager;
	private LinearLayout[] mllValues;
	private Animation[] mAnimations;
	private ImageView[] mivHelpes;
	private TextView[] mtvIndexs;
	private TextView[] mtvValues;
	private Button[] mbtLifes;
	private LinearLayout mllGuide;
	private LinearLayout mllPlay;
	private ImageView mivLogo;
	private Button mbtStart;
	private Button mbtQuit;
	private Animation mAnimation;
	private Random mRandom;
	private String[] mHelpers;
	private boolean mShow;
	private int[] mFailOptions;
	private int mCount;
	private int mSound;
	private DatabaseManager mDatabaseManager;
	private int mQuestion = 0;
	private int mLanguage = 0;
	String[] languages;

	/*-------------------------------- ON RECEIVED --------------------------------*/
	@Override
	public void onReceived(AiLaTrieuPhuActivity mainActivity, Message message) {
		int command = message.getServerCommand();
		switch (command) {
		case Protocol.CMD_REQUEST_GET_NORMAL_PLAY_PACKAGE_QUESTION:
			handleCommandRequestGetNormalPlayPackageQuestion(message);
			break;

		case Protocol.CMD_REQUEST_SUBMIT_NORMAL_PLAY_RESULT:
			handleCommandRequestSubmitNormalPlayResult(message);
			break;

		default:
			break;
		}
	}

	private void handleCommandRequestGetNormalPlayPackageQuestion(
			Message message) {
		if (mQuestionManager.parsePackageQuestion(message) != 0) {
			mResultManager.setId(mQuestionManager.getId());
			ArrayList<QuestionModel> questionModels = mQuestionManager
					.getQuestionModels();
			for (QuestionModel question : questionModels) {
				if (mDatabaseManager.insertQuestion(languages[mLanguage],
						question) != -1) {
					mQuestion++;
				}
			}
			mLanguage = ++mLanguage % languages.length;
			getQuestion(languages[mLanguage]);
			Log.i("index = " + mLanguage, "language = " + languages[mLanguage]);
			Log.i("number = ", mQuestion + "");
		}
	}

	private void handleCommandRequestSubmitNormalPlayResult(Message message) {
		if (IOUtility.readBoolean(message)) {
			Toast.makeText(mMainActivity,
					getResources().getString(R.string.dnt_saved),
					Toast.LENGTH_SHORT).show();
			if (mReplaying) {
				showReplayGameConfirmDialog();
			} else {
				finish();
			}
		} else {
			Toast.makeText(mMainActivity,
					getResources().getString(R.string.dnt_unsaved),
					Toast.LENGTH_SHORT).show();
			onFailed();
		}
	}

	@Override
	public void onFailed() {
		showReconnectConfirmDialog();
	}

	@Override
	public void initExtendedVariables() {
		languages = getResources().getStringArray(R.array.dnt_languages);
		mDatabaseManager = new DatabaseManager(mMainActivity);
		mShow = true;
		mReplaying = false;
		mCount = -1;
		mSound = 0;
		mRandom = new Random();
		mHelperManager = new HelperManager();
		mAnimation = AnimationUtils.loadAnimation(mMainActivity,
				R.anim.anim_helper);
		mAnimations = new Animation[ANIM_GUIDE_IDS.length];
		for (int index = 0; index < mAnimations.length; index++) {
			mAnimations[index] = AnimationUtils.loadAnimation(mMainActivity,
					ANIM_GUIDE_IDS[index]);
		}
		mHelpers = getResources().getStringArray(R.array.dnt_helper_names);
	}

	@Override
	public void initExtendedViews(View view) {
		mivLogo = (ImageView) view.findViewById(R.id.iv_normal_logo);
		if (AiLaTrieuPhuActivity.mEnglish) {
			mivLogo.setImageResource(R.drawable.img_logo_en);
		} else {
			mivLogo.setImageResource(R.drawable.img_logo_vi);
		}
		mbtLifes = new Button[HELPER_IDS.length];
		for (int i = 0; i < mbtLifes.length; i++) {
			mbtLifes[i] = (Button) view.findViewById(HELPER_IDS[i]);
			mbtLifes[i].setOnClickListener(this);
		}
		mllGuide = (LinearLayout) view.findViewById(R.id.ll_guide);
		mllPlay = (LinearLayout) view
				.findViewById(R.id.ll_fragment_normal_play);
		mbtStart = (Button) view.findViewById(R.id.bt_guide_start);
		mbtStart.setOnClickListener(this);
		mllValues = new LinearLayout[LAYOUT_IDS.length];
		mtvIndexs = new TextView[LAYOUT_IDS.length];
		mtvValues = new TextView[LAYOUT_IDS.length];
		mivHelpes = new ImageView[HELP_IDS.length];
		ArrayList<String> moneyValues = mMoneyManager.getMoneyStrings();
		for (int index = 0; index < LAYOUT_IDS.length; index++) {
			mllValues[index] = (LinearLayout) view
					.findViewById(LAYOUT_IDS[index]);
			mtvIndexs[index] = (TextView) view.findViewById(INDEX_IDS[index]);
			mtvIndexs[index].setText((index + 1) + "");
			mtvValues[index] = (TextView) view.findViewById(VALUE_IDS[index]);
			mtvValues[index].setText(moneyValues.get(index + 1));
		}
		for (int index = 0; index < mivHelpes.length; index++) {
			mivHelpes[index] = (ImageView) view.findViewById(HELP_IDS[index]);
		}
		mbtQuit = (Button) view.findViewById(R.id.bt_fragment_normal_quit);
		mbtQuit.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
		case R.id.bt_fragment_normal_helper_0:
			mbtLifes[0].startAnimation(mAnimation);
			showUseHelperConfirmDialog(0);
			break;

		case R.id.bt_fragment_normal_helper_1:
			mbtLifes[1].startAnimation(mAnimation);
			showUseHelperConfirmDialog(1);
			break;

		case R.id.bt_fragment_normal_helper_2:
			mbtLifes[2].startAnimation(mAnimation);
			showUseHelperConfirmDialog(2);
			break;

		case R.id.bt_fragment_normal_helper_3:
			mbtLifes[3].startAnimation(mAnimation);
			showUseHelperConfirmDialog(3);
			break;

		case R.id.bt_fragment_normal_quit:
			mReplaying = true;
			mStopping = true;
			showQuitGameConfirmDialog();
			break;

		case R.id.bt_guide_start:
			skipInstructions(true);
			break;

		default:
			break;
		}
	}

	@Override
	public void startGame() {
		mCount++;
		if (mCount > 0 && mCount % 2 == 0) {
			mMainActivity.showFullStartApp();
		}
		mFailOptions = new int[2];
		mMoneyManager.resetMoney();
		mHelperManager.resetLife();
		mResultManager.resetResult();
		mQuestionManager.resetQuestion();
		for (int index = 0; index < mbtLifes.length; index++) {
			mbtLifes[index].setBackgroundResource(ENABLE_HELPER_IDS[index]);
		}
		resetBaseTextView();
		setEnableAllButton(false);
		if (mShow) {
			showGuide();
		} else {
			getQuestion(languages[mLanguage]);
		}
	}

	@Override
	public void finishGame() {
		mPausing = true;
		releaseGame();
		if (mMoneyManager.getIndex() < 1) {
			if (mReplaying) {
				showReplayGameConfirmDialog();
			} else {
				finish();
			}
		} else {
			showSubmitResultConfirmDialog();
		}
	}

	@Override
	public void timeOut() {
		mAudioManager.playSound(AudioManager.TIME_OUT, false);
		addResult(false);
		clearQuestion();
		resetBaseTextView();
		setEnableAllButton(false);
		mReplaying = true;
		mStopping = false;
		mPausing = true;
		if (mQuestionManager.getCurrent() == 0) {
			showReplayGameConfirmDialog();
		} else {
			showSubmitResultConfirmDialog();
		}
	}

	@Override
	public void handleAnswer(Button button, int answer) {
		boolean result = mQuestionManager.submitAnswer(answer);
		if (result) {
			handleRight(button);
		} else {
			handleFail(button);
		}
	}

	@Override
	public void handleRight(Button button) {
		int answer = mQuestionManager.getAnswerCurrentQuestion();
		if (AiLaTrieuPhuActivity.mEnglish) {
			if (mQuestionManager.getLevelCurrentQuestion() >= 5) {
				mAudioManager.playSound(AudioManager.RIGHT_A_EN + 2 * answer,
						false);
			} else {
				mAudioManager.playSound(AudioManager.RIGHT_EN, false);
			}
		} else {
			if (mQuestionManager.getLevelCurrentQuestion() >= 5) {
				mAudioManager.playSound(AudioManager.RIGHT_A_VI + 2 * answer,
						false);
			} else {
				mAudioManager.playSound(AudioManager.RIGHT_VI, false);
			}
		}
		addResult(true);
		startAnimationButtonRightOrFail(button, true);
		mMoneyManager.nextIndex();
		mtvMoney.setText(mMoneyManager.getStringCurrentMoney());
		mtvMoney.startAnimation(mTextAnimations[2]);
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				mQuestionManager.nextQuestion();
				if (mQuestionManager.getCurrent() == 0) {
					mReplaying = true;
					mStopping = true;
					finishGame();
				} else {
					int index = mQuestionManager.getLevelCurrentQuestion();
					if (index % 5 == 0) {
						clearQuestion();
						showMessageDialog();
					} else {
						displayContent();
					}
				}
			}
		}, 3000);
	}

	@Override
	public void handleFail(Button button) {
		int rightAnswer = mQuestionManager.getAnswerCurrentQuestion();
		if (AiLaTrieuPhuActivity.mEnglish) {
			mAudioManager.playSound(AudioManager.FAIL_A_EN + 2 * rightAnswer,
					false);
		} else {
			mAudioManager.playSound(AudioManager.FAIL_A_VI + 2 * rightAnswer,
					false);
		}
		mPausing = true;
		addResult(false);
		startAnimationButtonRightOrFail(button, false);
		startAnimationButtonRightOrFail(mbtOptions[rightAnswer], true);
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				mReplaying = true;
				mStopping = false;
				finishGame();
			}
		}, 4000);
	}

	@Override
	public void setEnableAllButton(boolean enabled) {
		setEnableOptionButton(enabled);
		setEnableHelperButton(enabled);
		mbtStart.setEnabled(enabled);
		mbtQuit.setEnabled(enabled);
	}

	@Override
	public int getViewId() {
		return R.layout.fragment_normal;
	}

	@Override
	public void confirmFinish() {
		mAudioManager.playSound(AudioManager.TOUCH, false);
		mReplaying = false;
		mStopping = true;
		showQuitGameConfirmDialog();
	}

	private void showGuide() {
		mbtStart.setEnabled(true);
		mllGuide.setVisibility(LinearLayout.INVISIBLE);
		mllPlay.setVisibility(LinearLayout.GONE);
		for (int index = 0; index < mivHelpes.length; index++) {
			mivHelpes[index].setBackgroundResource(ENABLE_HELPER_IDS[index]);
		}
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				mllGuide.setVisibility(LinearLayout.VISIBLE);
				mllGuide.startAnimation(mAnimations[0]);
				if (AiLaTrieuPhuActivity.mEnglish) {
					mAudioManager.playSound(AudioManager.RULE_EN, false);
				} else {
					mAudioManager.playSound(AudioManager.RULE_VI, false);
				}
				final int delayMillis = 100;
				int elapsedTime = delayMillis;
				for (int i = 0; i < LAYOUT_IDS.length; i++) {
					animateMoney(i, i * delayMillis);
					elapsedTime += delayMillis;
				}
				mHandler.postDelayed(new Runnable() {

					@Override
					public void run() {
						mllValues[LAYOUT_IDS.length - 1]
								.setBackgroundResource(R.drawable.bkg_level_0);
					}
				}, elapsedTime);
				elapsedTime += 2500;
				for (int i = 0; i < 3; i++) {
					elapsedTime += 400;
					animateMoney(i * 5 + 4, elapsedTime);
				}
				elapsedTime += 2000;
				for (int i = 0; i < mivHelpes.length; i++) {
					elapsedTime += 1000;
					animateHelp(i, elapsedTime);
				}
				elapsedTime += 2500;
				mHandler.postDelayed(new Runnable() {

					@Override
					public void run() {
						hideGuide();
						showPlayReadyConfirmDialog();
					}
				}, elapsedTime);
			}
		}, mAnimations[0].getDuration());
	}

	private void hideGuide() {
		mShow = false;
		mHandler.removeCallbacksAndMessages(null);
		mllGuide.startAnimation(mAnimations[1]);
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				mllPlay.setVisibility(LinearLayout.VISIBLE);
				mllGuide.setVisibility(ListView.INVISIBLE);
			}
		}, mAnimations[1].getDuration());
	}

	private void animateMoney(final int index, final int delayMillis) {
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				mllValues[index].setBackgroundResource(R.drawable.bkg_level_1);
				if (index > 0) {
					mllValues[index - 1]
							.setBackgroundResource(R.drawable.bkg_level_0);
				}
			}
		}, delayMillis);
	}

	private void animateHelp(final int index, final int delayMillis) {
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				mivHelpes[index]
						.setBackgroundResource(DISABLE_HELPER_IDS[index]);
				mivHelpes[index].startAnimation(mAnimation);
			}
		}, delayMillis);
	}

	public void skipInstructions(final boolean now) {
		if (mShow) {
			mShow = false;
			hideGuide();
		}
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				mbtStart.setEnabled(true);
			}
		}, 500);
		if (AiLaTrieuPhuActivity.mEnglish) {
			mAudioManager.pauseSound(AudioManager.RULE_EN);
			if (!now) {
				mAudioManager.playSound(AudioManager.START_EN, false);
			}
		} else {
			mAudioManager.pauseSound(AudioManager.RULE_VI);
			if (!now) {
				mAudioManager.playSound(AudioManager.START_VI, false);
			}
		}
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				getQuestion(languages[mLanguage]);
			}
		}, 2000);
	}

	public void displayAdvisorHelper() {
		if (mQuestionManager.getLevelCurrentQuestion() > 5) {
			mbtLifes[3].setVisibility(Button.VISIBLE);
			if (mQuestionManager.getLevelCurrentQuestion() == 6) {
				mbtLifes[3].startAnimation(mAnimation);
			}
		} else {
			mbtLifes[3].setVisibility(Button.GONE);
		}
	}

	public void disableHelperButton(int index) {
		if ((-1 < index) && (mbtLifes.length > index)) {
			mbtLifes[index].setEnabled(false);
			mbtLifes[index].setBackgroundResource(DISABLE_HELPER_IDS[index]);
		}
	}

	public void setEnableHelperButton(boolean enabled) {
		for (int index = 0; index < mbtLifes.length; index++) {
			if (mHelperManager.isAvailable(index)) {
				mbtLifes[index].setEnabled(enabled);
			} else {
				mbtLifes[index].setEnabled(false);
			}
		}
	}

	@Override
	public void displayContent() {
		mFailOptions = null;
		displayQuestion(TIMER);
		displayAdvisorHelper();
	}

	@Override
	public void submitResult() {
		long id = mResultManager.getId();
		ArrayList<ResultModel> resultModels = mResultManager.getResultModels();
		Message message = SendData.requestSubmitNormalPlayResult(id,
				resultModels, mStopping);
		mMainActivity.sendRequest(message, true, true);
	}

	@Override
	public void requestQuestion() {
		// Message message = SendData.requestNormalPackageQuestion(mLanguage);
		// mMainActivity.sendRequest(message, false, false);
	}

	void getQuestion(String lang) {
		Message message = SendData.requestNormalPackageQuestion(lang);
		mMainActivity.sendRequest(message, false, false);
	}

	/*------------------------------------------------ SHOW PLAY READY DIALOG ------------------------------------------------*/
	public void showPlayReadyConfirmDialog() {
		final ConfirmDialog dialog = ConfirmDialog.getInstance(mMainActivity);
		dialog.setMessage(getResources().getString(R.string.dnt_ready));
		dialog.setOnConfirmListener(new OnConfirmListener() {

			@Override
			public void onClickYesListener() {
				dialog.dismiss();
				skipInstructions(false);
			}

			@Override
			public void onClickNoListener() {
				dialog.dismiss();
				finish();
			}
		});
		dialog.show();
	}

	/*------------------------------------------------ SHOW SUBMIT RESULT DIALOG ------------------------------------------------*/
	public void showSubmitResultConfirmDialog() {
		mPausing = true;
		if (mMoneyManager.getIndex() < 15) {
			if (AiLaTrieuPhuActivity.mEnglish) {
				mAudioManager.playSound(AudioManager.THANKS_EN, false);
			} else {
				mAudioManager.playSound(AudioManager.THANKS_VI, false);
			}
		} else {
			if (AiLaTrieuPhuActivity.mEnglish) {
				mAudioManager.playSound(AudioManager.VICTORY_EN, false);
			} else {
				mAudioManager.playSound(AudioManager.VICTORY_VI, false);
			}
		}
		final ConfirmDialog dialog = ConfirmDialog.getInstance(mMainActivity);
		String finalMoney = "";
		if (mMoneyManager.getIndex() <= 5) {
			finalMoney = mMoneyManager.getStringCurrentMoney();
		} else {
			if (!mStopping) {
				int index = mMoneyManager.getIndex();
				index = index - (index % 5);
				finalMoney = mMoneyManager.getMoneyStrings().get(index);
			} else {
				finalMoney = mMoneyManager.getStringCurrentMoney();
			}
		}
		String message = getResources()
				.getString(R.string.dnt_save, finalMoney);
		dialog.setMessage(message);
		dialog.setOnConfirmListener(new OnConfirmListener() {

			@Override
			public void onClickYesListener() {
				dialog.dismiss();
				submitResult();
			}

			@Override
			public void onClickNoListener() {
				dialog.dismiss();
				if (mReplaying) {
					showReplayGameConfirmDialog();
				} else {
					finish();
				}
			}
		});
		dialog.show();
	}

	/*------------------------------------------------ SHOW RECONNECT DIALOG ------------------------------------------------*/
	public void showReconnectConfirmDialog() {
		final ConfirmDialog dialog = ConfirmDialog.getInstance(mMainActivity);
		dialog.setMessage(getResources().getString(R.string.bmv_connect_false));
		dialog.setOnConfirmListener(new OnConfirmListener() {

			@Override
			public void onClickYesListener() {
				dialog.dismiss();
				if (mMoneyManager.getIndex() == 0) {
					requestQuestion();
				} else {
					submitResult();
				}
			}

			@Override
			public void onClickNoListener() {
				dialog.dismiss();
				finish();
			}
		});
		dialog.show();
	}

	/*------------------------------------------------ SHOW REPLAY GAME DIALOG ------------------------------------------------*/
	public void showReplayGameConfirmDialog() {
		final ConfirmDialog dialog = ConfirmDialog.getInstance(mMainActivity);
		dialog.setMessage(getResources().getString(R.string.dnt_replay));
		dialog.setOnConfirmListener(new OnConfirmListener() {

			@Override
			public void onClickYesListener() {
				dialog.dismiss();
				startGame();
				mLanguage = 0;
				mQuestion = 0;
			}

			@Override
			public void onClickNoListener() {
				dialog.dismiss();
				finish();
			}
		});
		dialog.show();
	}

	/*------------------------------------------------ SHOW REPICK LANGUAGE DIALOG ------------------------------------------------*/
	public void showRepickLanguageDialog() {
		final MessageDialog dialog = MessageDialog.getInstance(mMainActivity);
		dialog.setMessage(getResources().getString(R.string.dnt_no_question));
		dialog.setOnBackListener(new OnBackListener() {

			@Override
			public void onBack() {
				mAudioManager.playSound(AudioManager.TOUCH, false);
				dialog.dismiss();
				finish();
				final LanguageDialog dialog = LanguageDialog
						.getInstance(mMainActivity);
				dialog.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> adapterView,
							View view, int position, long id) {
						dialog.dismiss();
						mAudioManager.playSound(AudioManager.TOUCH, false);
						LanguageAdapter adapter = dialog.getAdapter();
						PlayFragment.mLanguage = adapter.getItem(position)
								.getLanguage();
						mMainActivity.switchContent(new NormalFragment(),
								NormalFragment.TAG, true, null);
						mAudioManager.pauseSound(AudioManager.INTRO);
					}
				});
				dialog.show();
			}
		});
		dialog.show();
	}

	/*------------------------------------------------ SHOW MESSAGE DIALOG ------------------------------------------------*/
	public void showMessageDialog() {
		mPausing = true;
		int level = mQuestionManager.getLevelCurrentQuestion();
		if ((level == 5 || level == 10) && !AiLaTrieuPhuActivity.mEnglish) {
			mSound = AudioManager.AND_IMPORTANT;
		}
		if (level == 15 && !AiLaTrieuPhuActivity.mEnglish) {
			mSound = AudioManager.AND_PASS;
		}
		if (mSound != 0) {
			mAudioManager.playSound(mSound, false);
		}
		final MessageDialog dialog = MessageDialog.getInstance(mMainActivity);
		String money = mMoneyManager.getStringNextMoney();
		dialog.setMessage(getResources().getString(
				R.string.dnt_index_05 + (level / 5 - 1), money));
		dialog.setOnBackListener(new OnBackListener() {

			@Override
			public void onBack() {
				if (mSound != 0) {
					mAudioManager.pauseSound(mSound);
					mSound = 0;
				}
				dialog.dismiss();
				mAudioManager.playSound(AudioManager.TOUCH, false);
				mPausing = false;
				displayContent();
			}
		});
		dialog.show();
	}

	/*------------------------------------------------ SHOW QUIT GAME DIALOG ------------------------------------------------*/
	public void showQuitGameConfirmDialog() {
		final ConfirmDialog dialog = ConfirmDialog.getInstance(mMainActivity);
		dialog.setMessage(getResources().getString(R.string.dnt_quit));
		dialog.setOnConfirmListener(new OnConfirmListener() {

			@Override
			public void onClickYesListener() {
				dialog.dismiss();
				finishGame();
			}

			@Override
			public void onClickNoListener() {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	/*------------------------------------------------ SHOW USE HELPER DIALOG ------------------------------------------------*/
	public void showUseHelperConfirmDialog(final int index) {
		final ConfirmDialog dialog = ConfirmDialog.getInstance(mMainActivity);
		dialog.setMessage(getResources().getString(R.string.dnt_helper,
				mHelpers[index]));
		dialog.setOnConfirmListener(new OnConfirmListener() {

			@Override
			public void onClickYesListener() {
				dialog.dismiss();
				switch (index) {
				case 0:
					useFiftyHelper();
					break;

				case 1:
					useBuddyHelper();
					break;

				case 2:
					useAudienceHelper();
					break;

				case 3:
					useAdvisorHelper();
					break;

				default:
					break;
				}
			}

			@Override
			public void onClickNoListener() {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	/*------------------------------------------------ USE FIFTY HELPER ------------------------------------------------*/
	public void useFiftyHelper() {
		mPausing = true;
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				mAudioManager.playSound(AudioManager.TOUCH, false);
				if (AiLaTrieuPhuActivity.mEnglish) {
					mAudioManager.playSound(AudioManager.HELPER_50_EN, false);
				} else {
					mAudioManager.playSound(AudioManager.HELPER_50_VI, false);
				}
				int answer = mQuestionManager.getAnswerCurrentQuestion();
				ArrayList<Integer> integers = new ArrayList<Integer>();
				for (int index = 0; index < mbtOptions.length; index++) {
					if (answer != index) {
						integers.add(index);
					}
				}
				mFailOptions = new int[2];
				for (int index = 0; index < 2; index++) {
					int failIndex = mRandom.nextInt(integers.size());
					int hiddenIndex = integers.get(index);
					mbtOptions[hiddenIndex].setVisibility(Button.INVISIBLE);
					mFailOptions[index] = hiddenIndex;
					integers.remove(failIndex);
				}
				disableHelperButton(0);
				mHelperManager.usedLife(0);
				mPausing = false;
			}
		}, 500);
	}

	/*------------------------------------------------ USE BUDDY HELPER ------------------------------------------------*/
	public void useBuddyHelper() {
		final SelectorDialog selectorDialog = new SelectorDialog(mMainActivity);
		selectorDialog.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				mPausing = true;
				mAudioManager.playSound(AudioManager.TOUCH, false);
				BuddyAdapter buddyAdapter = (BuddyAdapter) selectorDialog
						.getGridView().getAdapter();
				int resId = 0;
				if (AiLaTrieuPhuActivity.mEnglish) {
					resId = R.drawable.img_buddy_en_00 + position;
				} else {
					resId = R.drawable.img_buddy_vi_00 + position;
				}
				int rightAnswer = mQuestionManager.getAnswerCurrentQuestion();
				int currentLevel = mQuestionManager.getLevelCurrentQuestion();
				String nextMoney = mMoneyManager.getStringNextMoney();
				final BuddyDialog buddyDialog = new BuddyDialog(mMainActivity,
						resId, buddyAdapter.getItem(position), rightAnswer,
						mFailOptions, currentLevel, nextMoney);
				buddyDialog.setOnBackListener(new OnBackListener() {

					@Override
					public void onBack() {
						mAudioManager.playSound(AudioManager.TOUCH, false);
						buddyDialog.dismiss();
						mPausing = false;
					}
				});
				mHandler.postDelayed(new Runnable() {

					@Override
					public void run() {
						mPausing = false;
					}
				}, 6000);
				mHandler.postDelayed(new Runnable() {

					@Override
					public void run() {
						buddyDialog.dismiss();
					}
				}, 11000);
				buddyDialog.show();
				selectorDialog.dismiss();
			}
		});
		disableHelperButton(1);
	}

	/*------------------------------------------------ USE AUDIENCE HELPER ------------------------------------------------*/
	public void useAudienceHelper() {
		mPausing = true;
		new SurveyDialog(mMainActivity).show();
		final int delayMillis = 10000;
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				int rightAnswer = mQuestionManager.getAnswerCurrentQuestion();
				final AudienceDialog audienceDialog = new AudienceDialog(
						mMainActivity, rightAnswer, mFailOptions);
				audienceDialog.setOnBackListener(new OnBackListener() {

					@Override
					public void onBack() {
						mAudioManager.playSound(AudioManager.TOUCH, false);
						audienceDialog.dismiss();
						mPausing = false;
					}
				});
				mHandler.postDelayed(new Runnable() {

					@Override
					public void run() {
						mPausing = false;
					}
				}, 6000);
				mHandler.postDelayed(new Runnable() {

					@Override
					public void run() {
						audienceDialog.dismiss();
					}
				}, 11000);
				audienceDialog.show();
			}
		}, delayMillis);
		disableHelperButton(2);
	}

	/*------------------------------------------------ USE ADVISOR HELPER ------------------------------------------------*/
	public void useAdvisorHelper() {
		mPausing = true;
		int rightAnswer = mQuestionManager.getAnswerCurrentQuestion();
		final AdvisorDialog advisorDialog = new AdvisorDialog(mMainActivity,
				rightAnswer, mFailOptions);
		advisorDialog.setOnBackListener(new OnBackListener() {

			@Override
			public void onBack() {
				mAudioManager.playSound(AudioManager.TOUCH, false);
				advisorDialog.dismiss();
				mPausing = false;
			}
		});
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				mPausing = false;
			}
		}, 6000);
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				advisorDialog.dismiss();
			}
		}, 11000);
		advisorDialog.show();
		disableHelperButton(3);
	}
}
