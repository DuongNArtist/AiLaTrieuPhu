package com.skynet.ailatrieuphu.managers;

import java.util.ArrayList;

import android.util.Log;

import com.skynet.ailatrieuphu.models.QuestionModel;
import com.skynet.ailatrieuphu.sockets.IOUtility;
import com.skynet.ailatrieuphu.sockets.Message;

public class QuestionManager {

	public static final int NUMBER_OF_OPTIONS = 4;

	public static final int TYPE_TEXT = 0;
	public static final int TYPE_IMAGE = 1;
	public static final int TYPE_AUDIO = 2;
	public static final int TYPE_VIDEO = 3;

	private ArrayList<QuestionModel> mQuestionModels;
	private QuestionModel mQuestionModel;
	private boolean mConfirmed;
	private long mId;
	private int mCurrent;

	public QuestionManager() {
		resetQuestion();
	}

	public void resetQuestion() {
		mCurrent = 0;
		mConfirmed = false;
		mQuestionModels = new ArrayList<QuestionModel>();
		mQuestionModel = null;
	}

	public int getGroupQuestion() {
		return mCurrent / 5;
	}

	public void nextQuestion() {
		if (mCurrent < mQuestionModels.size() - 1) {
			mCurrent++;
			mQuestionModel = mQuestionModels.get(mCurrent);
		} else {
			resetQuestion();
		}
	}

	public boolean submitAnswer(int answer) {
		boolean result = false;
		if (mQuestionModel.getAnswer() == answer) {
			result = true;
		}
		return result;
	}

	public int parsePackageQuestion(Message message) {
		mCurrent = 0;
		int size = IOUtility.readInt(message);
		mQuestionModels = new ArrayList<QuestionModel>();
		if (size > 0) {
			mId = IOUtility.readLong(message);
			for (int i = 0; i < size; i++) {
				QuestionModel question = new QuestionModel();
				question.setId(IOUtility.readInt(message));
				question.setQuestion(IOUtility.readString(message));
				for (int j = 0; j < NUMBER_OF_OPTIONS; j++) {
					question.setOption(j, IOUtility.readString(message));
				}
				question.setAnswer(IOUtility.readInt(message) - 1);
				question.setCategoryId(IOUtility.readInt(message));
				question.setLink(IOUtility.readString(message));
				question.setLevel(IOUtility.readInt(message));
				mQuestionModels.add(question);
			}
			mQuestionModel = mQuestionModels.get(mCurrent);
		}
		return size;
	}

	public int parsePackageQuestionIndirectVtv3(Message message) {
		mCurrent = 0;
		mQuestionModels = new ArrayList<QuestionModel>();
		int size = IOUtility.readInt(message);
		if (size > 0) {
			mId = IOUtility.readLong(message);
			for (int i = 0; i < size; i++) {
				QuestionModel question = new QuestionModel();
				question.setId(IOUtility.readInt(message));
				question.setQuestion(IOUtility.readString(message));
				for (int j = 0; j < NUMBER_OF_OPTIONS; j++) {
					question.setOption(j, IOUtility.readString(message));
				}
				question.setAnswer(IOUtility.readInt(message) - 1);
				question.setCategoryId(IOUtility.readInt(message));
				question.setLink(IOUtility.readString(message));
				question.setLevel(IOUtility.readInt(message));
				question.setNamePlayer(IOUtility.readString(message));
				question.setAvatarPlayer(IOUtility.readString(message));
				question.setAnswerPlayer(IOUtility.readBoolean(message));
				mQuestionModels.add(question);
			}
			mQuestionModel = mQuestionModels.get(mCurrent);
		}
		return size;
	}

	public void parseQuestionDirectVtv3(Message message) {
		mCurrent = 1;
		mQuestionModel = new QuestionModel();
		mQuestionModel.setId(IOUtility.readInt(message));
		mQuestionModel.setQuestion(IOUtility.readString(message));
		for (int i = 0; i < NUMBER_OF_OPTIONS; i++) {
			mQuestionModel.setOption(i, IOUtility.readString(message));
		}
		mQuestionModel.setAnswer(IOUtility.readInt(message) - 1);
		mQuestionModel.setCategoryId(IOUtility.readInt(message));
		mQuestionModel.setLink(IOUtility.readString(message));
		mQuestionModel.setLevel(IOUtility.readInt(message));
		mQuestionModel.setNamePlayer(IOUtility.readString(message));
		mQuestionModel.setAvatarPlayer(IOUtility.readString(message));
		mQuestionModel.setAnswerPlayer(IOUtility.readBoolean(message));
	}

	public int getCategoryIdCurrentQuestion() {
		return mQuestionModel.getCategoryId();
	}

	public String getNamePlayerQuestion() {
		return mQuestionModel.getNamePlayer();
	}

	public String getAvatarPlayerQuestion() {
		return mQuestionModel.getAvatarPlayer();
	}

	public boolean getAnswerPlayerQuestion() {
		return mQuestionModel.isAnswerPlayer();
	}

	public int getIdCurrentQuestion() {
		return mQuestionModel.getId();
	}

	public String getLinkCurrentQuestion() {
		return mQuestionModel.getLink();
	}

	public String[] getOptionsCurrentQuestion() {
		return mQuestionModel.getOptions();
	}

	public int getLevelCurrentQuestion() {
		return mQuestionModel.getLevel();
	}

	public int getAnswerCurrentQuestion() {
		return mQuestionModel.getAnswer();
	}

	public String getQuestionCurrentQuestion() {
		return mQuestionModel.getQuestion();
	}

	public String getOptionCurrentQuestion(int index) {
		if (index > -1 && index < 4) {
			return mQuestionModel.getOption(index);
		}
		return "";
	}

	public QuestionModel getQuestionModel() {
		return mQuestionModel;
	}

	public boolean isConfirmed() {
		if (getLevelCurrentQuestion() >= 5) {
			mConfirmed = true;
		} else {
			mConfirmed = false;
		}
		return mConfirmed;
	}

	public ArrayList<QuestionModel> getQuestionModels() {
		return mQuestionModels;
	}

	public int getCurrent() {
		return mCurrent;
	}

	public void setCurrent(int current) {
		this.mCurrent = current;
	}

	public long getId() {
		return mId;
	}

	public void setId(long id) {
		this.mId = id;
	}

}
