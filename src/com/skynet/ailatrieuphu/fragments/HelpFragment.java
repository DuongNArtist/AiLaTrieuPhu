package com.skynet.ailatrieuphu.fragments;

import android.annotation.SuppressLint;
import android.view.View;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.skynet.ailatrieuphu.R;
import com.skynet.ailatrieuphu.activities.AiLaTrieuPhuActivity;
import com.skynet.ailatrieuphu.dialogs.LoadingDialog;
import com.skynet.ailatrieuphu.preferences.PreferenceManager;
import com.skynet.ailatrieuphu.sockets.Message;
import com.skynet.ailatrieuphu.utilities.NetworkUtility;

@SuppressLint("SetJavaScriptEnabled")
public class HelpFragment extends BaseFragment {

    public static final String TAG = HelpFragment.class.getSimpleName();

    private ImageView mivTitle;
    private ImageView mivLogo;
    private WebView mwvHelp;
    private LinearLayout mllHelp;

    @Override
    public void onReceived(AiLaTrieuPhuActivity mainActivity, Message message) {

    }

    @Override
    public void onFailed() {

    }

    @Override
    public void createVariables() {

    }

    @Override
    public int getViewId() {
        return R.layout.fragment_help;
    }

    @Override
    public void createViews(View view) {
        mivTitle = (ImageView) view.findViewById(R.id.iv_help_title);
        mivLogo = (ImageView) view.findViewById(R.id.iv_help_logo);
        if (AiLaTrieuPhuActivity.mEnglish) {
            mivTitle.setImageResource(R.drawable.img_help_en);
            mivLogo.setImageResource(R.drawable.img_logo_en);
        } else {
            mivTitle.setImageResource(R.drawable.img_help_vi);
            mivLogo.setImageResource(R.drawable.img_logo_vi);
        }
        mllHelp = (LinearLayout) view.findViewById(R.id.ll_help);
        mwvHelp = (WebView) view.findViewById(R.id.wv_help);
        showContent();
    }

    @Override
    public void confirmFinish() {
        finish();
    }

    private void showContent() {
        mwvHelp.setVisibility(WebView.GONE);
        String url = PreferenceManager.getInstance(mMainActivity).getLink();
        if (url.equals("")) {
            url = "http://210.211.124.135:7982/GoFunService/ailatrieuphu/Wapgame/index.html";
        }
        boolean hasNetwork = NetworkUtility.getInstance(mMainActivity)
                .isConnect();
        boolean isValidUrl = URLUtil.isValidUrl(url);
        if (hasNetwork && isValidUrl) {
            LoadingDialog.getInstance(mMainActivity).show();
            mllHelp.setVisibility(LinearLayout.GONE);
            mwvHelp.setVisibility(WebView.VISIBLE);
            mwvHelp.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    LoadingDialog.getInstance(mMainActivity).cancel();
                }
            });
            mwvHelp.getSettings().setJavaScriptEnabled(true);
            mwvHelp.getSettings()
                    .setJavaScriptCanOpenWindowsAutomatically(true);
            mwvHelp.loadUrl(url);
        } else {
            mllHelp.setVisibility(LinearLayout.VISIBLE);
            mwvHelp.setVisibility(WebView.GONE);
        }
    }
}
