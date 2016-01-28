package com.skynet.ailatrieuphu.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.skynet.ailatrieuphu.R;
import com.skynet.ailatrieuphu.activities.AiLaTrieuPhuActivity;
import com.skynet.ailatrieuphu.audios.AudioManager;
import com.skynet.ailatrieuphu.sockets.IOUtility;
import com.skynet.ailatrieuphu.sockets.Message;

public class AppDialog extends BaseDialog implements
        android.view.View.OnClickListener, OnCancelListener {

    private static AppDialog mInstance;

    public static String TAG = AppDialog.class.getSimpleName();

    private TextView mtvTitle;
    private TextView mtvContent;
    private Button mbtDownload;
    private Button mbtClose;
    private String mLink;

    private AppDialog(Context context) {
        super(context);
        setCancelable(true);
        setOnCancelListener(this);
    }

    public static AppDialog getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new AppDialog(context);
        }
        return mInstance;
    }

    public void parseMessage(Message message) {
        String title = IOUtility.readString(message);
        String content = IOUtility.readString(message);
        mLink = IOUtility.readString(message);
        mtvTitle.setText(title);
        mtvContent.setText(content);
        show();
    }

    @Override
    public void show() {
        super.show();
        Log.i("Quảng cáo", "Show");
    }

    @Override
    public int getViewId() {
        return R.layout.dialog_app;
    }

    @Override
    public void bindView() {
        mtvTitle = (TextView) findViewById(R.id.tv_app_title);
        mtvContent = (TextView) findViewById(R.id.tv_app_content);
        mbtDownload = (Button) findViewById(R.id.bt_app_download);
        mbtClose = (Button) findViewById(R.id.bt_app_close);
        mbtDownload.setOnClickListener(this);
        mbtClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        AudioManager.getInstance().playSound(AudioManager.TOUCH, false);
        switch (v.getId()) {
        case R.id.bt_app_close:
            dismiss();
            break;

        case R.id.bt_app_download:
            Toast.makeText(mContext, mLink, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
            intent.setData(Uri.parse(mLink));
            ((AiLaTrieuPhuActivity) mContext).startActivity(intent);
            cancel();
            break;

        default:
            break;
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        dismiss();
    }

}
