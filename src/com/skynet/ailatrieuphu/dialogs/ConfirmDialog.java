package com.skynet.ailatrieuphu.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.skynet.ailatrieuphu.R;
import com.skynet.ailatrieuphu.audios.AudioManager;

public class ConfirmDialog extends BaseDialog implements OnClickListener,
        OnCancelListener {

    public static final String TAG = ConfirmDialog.class.getSimpleName();

    private static ConfirmDialog mInstance;
    private TextView mtvMessage;
    private Button mbtYes;
    private Button mbtNo;
    private OnConfirmListener mOnConfirmListener;

    private ConfirmDialog(Context context) {
        super(context);
        setCancelable(true);
        setOnCancelListener(this);
    }

    public static ConfirmDialog getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ConfirmDialog(context);
        }
        return mInstance;
    }

    public void setOnConfirmListener(OnConfirmListener listener) {
        mOnConfirmListener = listener;
    }

    public void setMessage(String message) {
        mtvMessage.setText(message);
    }

    @Override
    public int getViewId() {
        return R.layout.dialog_confirm;
    }

    @Override
    public void bindView() {
        mtvMessage = (TextView) findViewById(R.id.tv_confirm_message);
        mbtYes = (Button) findViewById(R.id.bt_confirm_yes);
        mbtNo = (Button) findViewById(R.id.bt_confirm_no);
        mbtYes.setOnClickListener(this);
        mbtNo.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        AudioManager.getInstance().playSound(AudioManager.TOUCH, false);
        switch (view.getId()) {
        case R.id.bt_confirm_yes:
            mOnConfirmListener.onClickYesListener();
            break;

        case R.id.bt_confirm_no:
            mOnConfirmListener.onClickNoListener();
            break;
        default:
            break;
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        AudioManager.getInstance().playSound(AudioManager.TOUCH, false);
        mOnConfirmListener.onClickNoListener();
    }

}
