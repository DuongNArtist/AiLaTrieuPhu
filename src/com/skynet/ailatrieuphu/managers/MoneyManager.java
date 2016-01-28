package com.skynet.ailatrieuphu.managers;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.content.res.Resources;

import com.skynet.ailatrieuphu.R;

public class MoneyManager {

    private int mIndex;
    private ArrayList<Integer> mMoneyValues;
    private ArrayList<String> mMoneyStrings;

    public MoneyManager(Resources res) {
        mMoneyValues = new ArrayList<Integer>();
        mMoneyStrings = new ArrayList<String>();
        mMoneyStrings.add("0");
        mMoneyValues.add(0);
        String[] moneyValues = res.getStringArray(R.array.dnt_money_values);
        for (int index = 0; index < moneyValues.length; index++) {
            mMoneyValues.add(Integer.parseInt(moneyValues[index].replace(".",
                    "")));
            mMoneyStrings.add(moneyValues[index]);
        }
    }

    public void setIndex(int index) {
        mIndex = index;
    }

    public void resetMoney() {
        mIndex = 0;
    }

    public void nextIndex() {
        if (mIndex < mMoneyValues.size() - 1) {
            mIndex++;
        }
    }

    public int getIndex() {
        return mIndex;
    }

    public ArrayList<Integer> getMoneyValues() {
        return mMoneyValues;
    }

    public ArrayList<String> getMoneyStrings() {
        return mMoneyStrings;
    }

    public int getCurrentMoney() {
        return mMoneyValues.get(mIndex);
    }

    public int getMoneyTotal() {
        return mMoneyValues.get(mIndex);
    }

    public int getNextMoney() {
        if (mIndex < mMoneyValues.size() - 1) {
            return mMoneyValues.get(mIndex + 1);
        }
        return 0;
    }

    public String getStringCurrentMoney() {
        return mMoneyStrings.get(mIndex);
    }

    public String getStringNextMoney() {
        if (mIndex < mMoneyStrings.size() - 1) {
            return mMoneyStrings.get(mIndex + 1);
        }
        return "";
    }

    public static String parseMoney(int money) {
        Locale locale = new Locale("en", "us");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        System.out.println(fmt.format(money));
        String b = fmt.format(money).substring(1,
                fmt.format(money).length() - 3);
        return b.replace(",", ".");
    }
}
