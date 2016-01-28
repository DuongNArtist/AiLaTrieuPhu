package com.skynet.ailatrieuphu.utilities;

import java.util.Locale;

import android.content.Context;
import android.content.res.Resources;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

public class LanguageUtility {

    public static final String VI = "vi";
    public static final String EN = "en";

    public static String getDefaultLanguage(Context context) {
        String defaultLanguage = "vi";
        try {

            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            defaultLanguage = tm.getNetworkCountryIso();
        } catch (Exception e) {
            e.printStackTrace();
            defaultLanguage = context.getResources().getConfiguration().locale
                    .getCountry();
        }
        return defaultLanguage;
    }

    public static void setLanguage(Context context, String language) {
        Resources resources = context.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        android.content.res.Configuration configuration = resources
                .getConfiguration();
        configuration.locale = new Locale(language.toLowerCase());
        resources.updateConfiguration(configuration, displayMetrics);
    }
}
