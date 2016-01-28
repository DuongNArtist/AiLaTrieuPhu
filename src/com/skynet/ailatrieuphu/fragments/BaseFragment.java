package com.skynet.ailatrieuphu.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.skynet.ailatrieuphu.MainApplication;
import com.skynet.ailatrieuphu.R;
import com.skynet.ailatrieuphu.activities.AiLaTrieuPhuActivity;
import com.skynet.ailatrieuphu.audios.AudioManager;
import com.skynet.ailatrieuphu.preferences.PreferenceManager;
import com.skynet.ailatrieuphu.sockets.SocketCallback;

public abstract class BaseFragment extends Fragment implements SocketCallback {

    private final static String TAG = BaseFragment.class.getSimpleName();

    protected MainApplication mMainApplication;
    protected AiLaTrieuPhuActivity mMainActivity;
    protected PreferenceManager mPreferenceUtility;
    protected FragmentManager mFragmentManager;
    protected ImageLoader mImageLoader;
    protected DisplayImageOptions mDisplayImageOptions;
    protected AudioManager mAudioManager;
    protected Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainActivity = (AiLaTrieuPhuActivity) getActivity();
        mPreferenceUtility = PreferenceManager.getInstance(mMainActivity);
        mMainApplication = (MainApplication) getActivity().getApplication();
        mFragmentManager = getFragmentManager();
        mDisplayImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.img_player)
                .showImageForEmptyUri(R.drawable.img_player)
                .showImageOnFail(R.drawable.img_player).cacheInMemory(true)
                .cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoaderConfiguration imageLoaderConfiguration = new ImageLoaderConfiguration.Builder(
                getActivity()).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(mDisplayImageOptions).build();
        if (mImageLoader == null) {
            mImageLoader = ImageLoader.getInstance();
        }
        mImageLoader.init(imageLoaderConfiguration);
        mAudioManager = AudioManager.getInstance();
        mHandler = new Handler();
        createVariables();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        int getViewId = getViewId();
        View view = inflater.inflate(getViewId, null, false);
        createViews(view);
        return view;
    }

    public abstract void createVariables();

    public abstract int getViewId();

    public abstract void createViews(View view);

    public abstract void confirmFinish();

    public void finish() {
        try {
            mFragmentManager.popBackStack();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
