package com.skynet.ailatrieuphu;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class MainApplication extends Application {

    public static enum APPLICATION_STATE {
        BACKGROUND_STATE, // open app from background state
        CLOSE_STATE, // open app from close state
        FIRST_TIME_STATE; // open app from run first time
    }

    public APPLICATION_STATE appState;
    public boolean appPurchasing;
    public boolean appRunning;
    public boolean startNewScreen;
    public boolean backPress;
    public boolean initAppBilling = false;
    public boolean appBilling = false;
    private static MainApplication mInstance;

    public static MainApplication get() {
        return mInstance;
    }

    public APPLICATION_STATE getAppState() {
        return appState;
    }

    public void setAppState(APPLICATION_STATE appState) {
        this.appState = appState;
    }

    public boolean isAppPurchasing() {
        return appPurchasing;
    }

    public void setAppPurchasing(boolean appPurchasing) {
        this.appPurchasing = appPurchasing;
    }

    public boolean isAppRunning() {
        return appRunning;
    }

    public void setAppRunning(boolean appRunning) {
        this.appRunning = appRunning;
    }

    public boolean isStartNewScreen() {
        return startNewScreen;
    }

    public void setStartNewScreen(boolean startNewScreen) {
        this.startNewScreen = startNewScreen;
    }

    public boolean isBackPress() {
        return backPress;
    }

    public void setBackPress(boolean backPress) {
        this.backPress = backPress;
    }

    public boolean isInitAppBilling() {
        return initAppBilling;
    }

    public void setInitAppBilling(boolean initAppBilling) {
        this.initAppBilling = initAppBilling;
    }

    public boolean isAppBilling() {
        return appBilling;
    }

    public void setAppBilling(boolean appBilling) {
        this.appBilling = appBilling;
    }

    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initImageLoader(getApplicationContext());
    }

    public void initImageLoader(Context context) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_launcher)
                .showImageForEmptyUri(R.drawable.ic_launcher)
                .showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
                .cacheOnDisc(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(options).build();
        ImageLoader.getInstance().init(config);
    }
}
