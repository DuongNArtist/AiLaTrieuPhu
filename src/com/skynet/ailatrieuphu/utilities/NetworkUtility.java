package com.skynet.ailatrieuphu.utilities;

import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class NetworkUtility {

    private static NetworkUtility mInstance = null;

    private ConnectivityManager mConnectivityManager;
    private static Context mContext;

    private NetworkUtility(Context context) {
        mContext = context;
        mConnectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public static NetworkUtility getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new NetworkUtility(context);
        }
        return mInstance;
    }

    public static String getMacAddress() {
        WifiManager wifiManager = (WifiManager) mContext
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String address = wifiInfo.getMacAddress();
        return address;
    }

    public boolean isConnection(String urlAddress) {
        boolean isOnline = false;
        try {
            URL url = new URL(urlAddress);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setConnectTimeout(1000);
            connection.connect();
            isOnline = connection.getResponseCode() == 200;
        } catch (Exception e) {
        }
        return isOnline;
    }

    public boolean isConnect() {
        return isConnectWifi() || isConnect3G();
    }

    public boolean isConnectWifi() {
        NetworkInfo wifi = mConnectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifi != null) {
            return wifi.isConnected();
        }
        return false;
    }

    public boolean isConnect3G() {
        NetworkInfo mobile3G = mConnectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobile3G != null) {
            return mobile3G.isConnected();
        }
        return false;
    }

}
