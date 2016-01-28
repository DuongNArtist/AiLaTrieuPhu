package com.skynet.ailatrieuphu.dialogs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.skynet.ailatrieuphu.R;

@SuppressLint("DefaultLocale")
public class LoginDialog extends BaseDialog implements
        android.view.View.OnClickListener, OnCancelListener {

    private static LoginDialog mInstance;

    public static final String TAG = LoginDialog.class.getSimpleName();
    public static final int MIN_LENGTH = 6;
    public static final int MAX_LENGTH = 20;

    private EditText mtvUsername;
    private EditText mtvPassword;
    private Button mbtClose;
    private Button mbtLogin;
    private Button mbtFacebook;
    private OnLoginListener mOnLoginListener;
    private String[] mMessages;

    public LoginDialog(Context context, OnLoginListener onLoginListener) {
        super(context);
        setCancelable(true);
        setOnCancelListener(this);
        mOnLoginListener = onLoginListener;
        mMessages = context.getResources().getStringArray(
                R.array.dtn_login_message);
    }

    @Override
    public int getViewId() {
        return R.layout.dialog_login;
    }

    @Override
    public void bindView() {
        mtvUsername = (EditText) findViewById(R.id.ed_username);
        mtvPassword = (EditText) findViewById(R.id.ed_password);
        mbtClose = (Button) findViewById(R.id.bt_close);
        mbtLogin = (Button) findViewById(R.id.bt_login);
        mbtFacebook = (Button) findViewById(R.id.bt_facebook);
        mbtClose.setOnClickListener(this);
        mbtLogin.setOnClickListener(this);
        mbtFacebook.setOnClickListener(this);
    }

    public void handleLoginNomal() {
        String strUsername = mtvUsername.getText().toString().trim();
        String strPassword = mtvPassword.getText().toString().trim();
        if (strUsername.length() >= MIN_LENGTH
                && strPassword.length() >= MIN_LENGTH
                && strUsername.length() <= MAX_LENGTH
                && strPassword.length() <= MAX_LENGTH
                && checkUsername(strUsername)) {
            closeDialog();
            mOnLoginListener.onClickNomal(strUsername, strPassword);
        } else {
            Toast.makeText(mContext, mMessages[1], Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean checkUsername(String userName) {
        try {
            Pattern p = Pattern.compile("[^a-z0-9_]", Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(userName.toLowerCase());
            boolean b = m.find();
            if (b) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void showDialog(Context context,
            OnLoginListener onLoginListener) {
        try {
            closeDialog();
            if (context != null) {
                mInstance = new LoginDialog(context, onLoginListener);
                mInstance.setCancelable(true);
                mInstance.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeDialog() {
        if (mInstance != null) {
            try {
                mInstance.dismiss();
                mInstance = null;
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.bt_login:
            handleLoginNomal();
            break;

        case R.id.bt_facebook:
            mOnLoginListener.onClickFacebook();
            break;

        case R.id.bt_close:
            LoadingDialog.getInstance(mContext).cancel();
            mOnLoginListener.onClickClose();
            break;

        default:
            break;
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        mOnLoginListener.onClickClose();
    }

}
