package com.skynet.ailatrieuphu.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Window;
import android.widget.LinearLayout;

import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.skynet.ailatrieuphu.MainApplication;
import com.skynet.ailatrieuphu.R;
import com.skynet.ailatrieuphu.constants.Constants;
import com.skynet.ailatrieuphu.preferences.PreferenceManager;
import com.startapp.android.publish.Ad;
import com.startapp.android.publish.AdDisplayListener;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;
import com.startapp.android.publish.banner.Banner;

public abstract class BaseFragmentActivity extends FragmentActivity {
    private final static String TAG = BaseFragmentActivity.class
            .getSimpleName();

    private final static String DAdmodeIdFull = "ca-app-pub-4580387094241197/3902076260";
    private final static String DAdmodeIdBanner = "ca-app-pub-4580387094241197/9948609861";
    private final static String DStartAppId = "103257478";
    private final static String DStartId = "203241888";
    private String AdmodeIdFull = DAdmodeIdFull;
    private String AdmodeIdBanner = DAdmodeIdBanner;
    private String StartAppId = DStartAppId;
    private String StartId = DStartId;

    public AdView adView = null;
    public InterstitialAd interstitial = null;
    public StartAppAd mStartAppAd;

    protected boolean mIsInitAdmode = false;
    protected boolean mIsShowAd = true;
    protected boolean mIsLoadAdsBanner = false;
    protected String mDeviceId;
    protected Context mContext;
    protected Fragment mFragment;
    protected MainApplication mMainApplication;
    protected FragmentManager mFragmentManager;
    protected UiLifecycleHelper uiHelper;
    protected GoogleAnalyticsTracker tracker;

    public static int mSocketState;
    public static boolean mRequesting;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = getApplicationContext();
        mMainApplication = (MainApplication) getApplication();
        mFragmentManager = getSupportFragmentManager();
        PreferenceManager mPreferenceUtility = PreferenceManager
                .getInstance(this);
        mFragmentManager
                .addOnBackStackChangedListener(mOnBackStackChangedListener);
        if (mPreferenceUtility.isFirstRunApp()) {
            mPreferenceUtility.setFirstRunApp(false);
            mMainApplication
                    .setAppState(MainApplication.APPLICATION_STATE.FIRST_TIME_STATE);
        } else {
            mMainApplication
                    .setAppState(MainApplication.APPLICATION_STATE.CLOSE_STATE);
        }
        mSocketState = Constants.SOCKET_STATE_DISCONNECTED;
        createUihelperFacebook(bundle);
        loadDeviceId();
        initGoogleAnalytics();
        initAdsBackground();
        initStartAd();
    }

    OnBackStackChangedListener mOnBackStackChangedListener = new OnBackStackChangedListener() {

        @Override
        public void onBackStackChanged() {
            try {
                mFragment = mFragmentManager.findFragmentById(R.id.fl_content);
                Log.d(TAG, "mContent" + "" + mFragment);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void createUihelperFacebook(final Bundle pSavedInstanceState) {
        uiHelper = new UiLifecycleHelper(this, new StatusCallback() {

            @Override
            public void call(Session session, SessionState state,
                    Exception exception) {

            }
        });
        uiHelper.onCreate(pSavedInstanceState);
    }

    private void initGoogleAnalytics() {
        tracker = GoogleAnalyticsTracker.getInstance();
        tracker.startNewSession("UA-33478140-6", getApplicationContext());
        tracker.trackTransactions();
    }

    public void trackView(String screenName) {
        tracker.trackPageView(screenName);
        tracker.dispatch();

    }

    public void trackEvent(String action) {
        tracker.trackEvent(getResources().getString(R.string.app_name), action,
                "", 0);
        tracker.dispatch();
    }

    private void loadDeviceId() {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) this
                    .getSystemService(Context.TELEPHONY_SERVICE);
            mDeviceId = telephonyManager.getDeviceId();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }

    }

    public static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            Log.d(TAG, "RegisterActivity"
                    + "I never expected this! Going down, going down!" + e);
        }
        return -1;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        uiHelper.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mMainApplication.isBackPress()) {
            mMainApplication.setBackPress(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mStartAppAd != null) {
            mStartAppAd.onResume();
        }
        mMainApplication.setAppRunning(true);
        uiHelper.onResume();
        trackView("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMainApplication.setAppRunning(true);
        uiHelper.onResume();
        if (mStartAppAd != null) {
            mStartAppAd.onPause();
        }
        trackView("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMainApplication
                .setAppState(MainApplication.APPLICATION_STATE.BACKGROUND_STATE);
        mMainApplication.setAppRunning(false);
        uiHelper.onStop();
    }

    @SuppressWarnings("unused")
    @Override
    protected void onRestart() {
        super.onRestart();
        if (mMainApplication.getAppState() == MainApplication.APPLICATION_STATE.BACKGROUND_STATE) {
            if (true/* mUserModel.isLogined() */) {
                // open app from closed state
            } else {
                if (!mMainApplication.isAppRunning()
                        && !mMainApplication.isBackPress()) {
                    // open app from closed state
                } else {
                    // back from previous screen
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data,
                new FacebookDialog.Callback() {
                    @Override
                    public void onError(FacebookDialog.PendingCall pendingCall,
                            Exception error, Bundle data) {
                        Log.e(TAG,
                                "on ac result facebook: "
                                        + String.format("Error: %s",
                                                error.toString()));
                    }

                    @Override
                    public void onComplete(
                            FacebookDialog.PendingCall pendingCall, Bundle data) {
                        Log.i(TAG, "on ac result facebook: " + "Success!");
                    }
                });
    }

    @Override
    protected void onDestroy() {
        uiHelper.onDestroy();
        tracker.stopSession();
        mFragmentManager
                .removeOnBackStackChangedListener(mOnBackStackChangedListener);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        mMainApplication.setBackPress(true);
        super.onBackPressed();
    }

    public void removeCurrentFragment() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (mFragment != null) {
            transaction.remove(mFragment);
        }
        transaction.commit();
    }

    public void removeCurrentFragmentInBackStack() {
        int length = mFragmentManager.getBackStackEntryCount();
        if (length > 0) {
            mFragmentManager.popBackStack();
        }
    }

    public void clearBackStackFragment() {
        int length = mFragmentManager.getBackStackEntryCount();
        for (int i = 0; i < length; i++) {
            mFragmentManager.popBackStack();
        }
    }

    public void switchContent(Fragment fragment, String tag,
            boolean isAnimation, Bundle bundle) {
        mFragment = fragment;
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (isAnimation) {
            transaction.setCustomAnimations(R.anim.anim_layout_left,
                    R.anim.anim_layout_right, R.anim.anim_layout_left,
                    R.anim.anim_layout_right);
        }
        transaction.replace(R.id.fl_content, fragment, tag);
        transaction.addToBackStack(tag);
        transaction.commitAllowingStateLoss();
    }

    public void initAdsBackground() {
        mIsInitAdmode = true;
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... arg0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                initAds();
                initAdFull();
            };
        }.execute();
    }

    public void initAds() {
        this.adView = new AdView(this);
        this.adView.setAdSize(AdSize.BANNER);
        this.adView.setAdUnitId(AdmodeIdBanner);
        adView.refreshDrawableState();
        adView.setVisibility(AdView.VISIBLE);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        mIsLoadAdsBanner = false;
        adView.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (mIsShowAd) {

                }
                mIsLoadAdsBanner = true;
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
                mIsLoadAdsBanner = false;
            }
        });
    }

    public void initAdFull() {
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(AdmodeIdFull);
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitial.loadAd(adRequest);
        interstitial.setAdListener(new AdListener() {

            @Override
            public void onAdClosed() {
                interstitial = null;
                requestNewAdsFull();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
            }
        });
    }

    public void requestNewAdsFull() {
        try {
            AdRequest adRequest = new AdRequest.Builder().build();
            interstitial.loadAd(adRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showFullAdmode() {
        if (interstitial != null && interstitial.isLoaded()) {
            interstitial.show();
        }
    }

    public void showBannerAdmode(LinearLayout linearLayout) {
        if (mIsShowAd) {
            if (adView != null) {
                linearLayout.removeAllViews();
                linearLayout.addView(adView);
            }
        }
    }

    private void initStartAd() {
        mStartAppAd = new StartAppAd(this);
        StartAppSDK.init(this, StartAppId, StartId, true);
    }

    public void showBannerStartApp(LinearLayout layout) {
        if (mIsShowAd) {
            Banner banner = new Banner(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layout.removeAllViews();
            layout.addView(banner, params);
        }
    }

    public void showFullStartApp() {
        if (mIsShowAd) {
            if (mStartAppAd != null && mStartAppAd.isReady()) {
                mStartAppAd.showAd(new AdDisplayListener() {

                    @Override
                    public void adHidden(Ad ad) {

                    }

                    @Override
                    public void adDisplayed(Ad ad) {

                    }

                    @Override
                    public void adClicked(Ad ad) {
                    }
                });
            }
        }
    }
}
