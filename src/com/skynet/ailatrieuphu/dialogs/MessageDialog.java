package com.skynet.ailatrieuphu.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.skynet.ailatrieuphu.R;
import com.skynet.ailatrieuphu.audios.AudioManager;

public class MessageDialog extends BaseDialog implements OnCancelListener,
        android.view.View.OnClickListener {

    private static MessageDialog mInstance;

    private TextView mtvMessage;
    private Button mbtOk;
    private OnBackListener mOnBackListener;

    private MessageDialog(Context context) {
        super(context);
        setCancelable(true);
        setOnCancelListener(this);
    }

    public static MessageDialog getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MessageDialog(context);
        }
        return mInstance;
    }

    public void setMessage(String message) {
        mtvMessage.setText(message);
    }

    public void setOnBackListener(OnBackListener listener) {
        mOnBackListener = listener;
    }

    @Override
    public int getViewId() {
        return R.layout.dialog_message;
    }

    @Override
    public void bindView() {
        mtvMessage = (TextView) findViewById(R.id.tv_message);
        mbtOk = (Button) findViewById(R.id.bt_ok);
        mbtOk.setOnClickListener(this);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        mOnBackListener.onBack();
    }

    @Override
    public void onClick(View v) {
        AudioManager.getInstance().playSound(AudioManager.TOUCH, false);
        mOnBackListener.onBack();
    }
}
