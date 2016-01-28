package com.skynet.ailatrieuphu.utilities;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateTimeUtility {
    public static String getHour() {
        Calendar calendar = new GregorianCalendar();
        return calendar.get(Calendar.HOUR_OF_DAY) < 10 ? "0"
                + calendar.get(Calendar.HOUR_OF_DAY) : ""
                + calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static String getMinute() {
        Calendar calendar = new GregorianCalendar();
        return calendar.get(Calendar.MINUTE) < 10 ? "0"
                + calendar.get(Calendar.MINUTE) : ""
                + calendar.get(Calendar.MINUTE);
    }

    public static String getSecond() {
        Calendar calendar = new GregorianCalendar();
        return calendar.get(Calendar.SECOND) < 10 ? "0"
                + calendar.get(Calendar.SECOND) : ""
                + calendar.get(Calendar.SECOND);
    }

    public static String getDate() {
        Calendar calendar = new GregorianCalendar();
        return calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0"
                + calendar.get(Calendar.DAY_OF_MONTH) : ""
                + calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static String getMonth() {
        Calendar calendar = new GregorianCalendar();
        return (1 + calendar.get(Calendar.MONTH)) < 10 ? "0"
                + (1 + calendar.get(Calendar.MONTH)) : ""
                + (1 + calendar.get(Calendar.MONTH));
    }

    public static String getYear() {
        Calendar calendar = new GregorianCalendar();
        return calendar.get(Calendar.YEAR) + "";
    }

    public static String getCurrentDateTimeWithDotSeprator() {
        return getHour() + "." + getMinute() + "-" + getDate() + "."
                + getMonth() + "." + getYear();
    }

    public static String getCurrentDateTime() {
        return getHour() + ":" + getMinute() + "-" + getDate() + "/"
                + getMonth() + "/" + getYear();
    }

    public static String getCurrentDate() {
        return getDate() + "/" + getMonth() + "/" + getYear();
    }

    public static String getCurrentDateWithDotSeprator() {
        return getDate() + "." + getMonth() + "." + getYear();
    }

    @SuppressLint("SimpleDateFormat")
    public static boolean isValidDate(String date) {
        boolean result = false;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                    "dd/MM/yyy");
            simpleDateFormat.setLenient(false);
            simpleDateFormat.parse(date);
            result = true;
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
}
