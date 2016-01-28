package com.skynet.ailatrieuphu.managers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import com.skynet.ailatrieuphu.models.QuestionModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseManager extends SQLiteOpenHelper {

	private static final int DB_VERSION = 1;
	private static final String DB_NAME = "database.db";
	private static final String TABLE = "questions";
	private static final String FLD_0 = "id";
	private static final String FLD_1 = "question";
	private static final String FLD_2 = "optiona";
	private static final String FLD_3 = "optionb";
	private static final String FLD_4 = "optionc";
	private static final String FLD_5 = "optiond";
	private static final String FLD_6 = "answer";
	private static final String FLD_7 = "category";
	private static final String FLD_8 = "link";
	private static final String FLD_9 = "level";
	private static final String[] FLDS = new String[] { FLD_0, FLD_1, FLD_2,
			FLD_3, FLD_4, FLD_5, FLD_6, FLD_7, FLD_8, FLD_9 };

	private String path;
	private Context context;

	public DatabaseManager(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		this.context = context;
		path = context.getFilesDir().getPath().replace("files", "databases/");
		if (!isExistDatabase()) {
			copyNewDatabase();
		}
	}

	public long insertQuestion(String tbl, QuestionModel model) {
		long result = 0;
		SQLiteDatabase database = getWritableDatabase();
		result = database.insert(tbl, null, parseContentValue(model));
		database.close();
		return result;
	}

	private QuestionModel parseQuestion(Cursor cursor) {
		QuestionModel question = new QuestionModel();
		question.setId(cursor.getInt(cursor.getColumnIndex(FLD_0)));
		question.setQuestion(cursor.getString(cursor.getColumnIndex(FLD_1)));
		question.setOption(0, cursor.getString(cursor.getColumnIndex(FLD_2)));
		question.setOption(2, cursor.getString(cursor.getColumnIndex(FLD_3)));
		question.setOption(3, cursor.getString(cursor.getColumnIndex(FLD_4)));
		question.setOption(4, cursor.getString(cursor.getColumnIndex(FLD_5)));
		question.setAnswer(cursor.getInt(cursor.getColumnIndex(FLD_6)));
		question.setCategoryId(cursor.getInt(cursor.getColumnIndex(FLD_7)));
		question.setLink(cursor.getString(cursor.getColumnIndex(FLD_8)));
		question.setLevel(cursor.getInt(cursor.getColumnIndex(FLD_9)));
		return question;
	}

	public ContentValues parseContentValue(QuestionModel question) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(FLD_0, question.getId());
		contentValues.put(FLD_1, question.getQuestion());
		contentValues.put(FLD_2, question.getOptions()[0]);
		contentValues.put(FLD_3, question.getOptions()[1]);
		contentValues.put(FLD_4, question.getOptions()[2]);
		contentValues.put(FLD_5, question.getOptions()[3]);
		contentValues.put(FLD_6, question.getAnswer());
		contentValues.put(FLD_7, question.getCategoryId());
		contentValues.put(FLD_8, question.getLink());
		contentValues.put(FLD_9, question.getLevel());
		return contentValues;
	}

	public ArrayList<QuestionModel> selectAll() {
		ArrayList<QuestionModel> questionModels = new ArrayList<QuestionModel>();
		SQLiteDatabase database = getReadableDatabase();
		Cursor cursor = database.query(TABLE, FLDS, null, null, null, null,
				null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			questionModels.add(parseQuestion(cursor));
			cursor.moveToNext();
		}
		database.close();
		return questionModels;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	private boolean isExistDatabase() {
		return new File(path + DB_NAME).exists();
	}

	private void copyNewDatabase() {
		try {
			InputStream inputStream = context.getAssets().open(DB_NAME);
			OutputStream outputStream = new FileOutputStream(path + DB_NAME);
			File file = new File(path);
			byte[] buffer = new byte[inputStream.available()];
			if (!file.exists()) {
				file.mkdir();
			}
			inputStream.read(buffer);
			outputStream.write(buffer);
			outputStream.flush();
			inputStream.close();
			outputStream.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
