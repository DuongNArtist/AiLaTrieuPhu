package com.skynet.ailatrieuphu.utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;

public class Utilities {

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) > Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static String[] split(String original, String separator) {
        Vector<String> nodes = new Vector<String>();
        String result[];
        int index = original.indexOf(separator);
        while (index >= 0) {
            nodes.addElement(original.substring(0, index));
            original = original.substring(index + separator.length());
            index = original.indexOf(separator);
        }
        nodes.addElement(original);
        result = new String[nodes.size()];
        if (nodes.size() > 0) {
            for (int loop = 0; loop < nodes.size(); loop++)
                result[loop] = nodes.elementAt(loop);
        }
        return result;
    }

    public static String captureScreenForFaceBook(Activity activity, View view) {
        try {
            view.setDrawingCacheEnabled(true);
            Bitmap bm = Bitmap.createBitmap(view.getDrawingCache());
            view.setDrawingCacheEnabled(false);
            if (bm.isRecycled()) {
                return "";
            }
            OutputStream fout = null;
            String mPath = Environment.getExternalStorageDirectory().toString()
                    + "/skynet";
            File imageFile = new File(mPath);
            imageFile.mkdir();
            File outputFile = new File(mPath, "share.png");
            try {
                fout = new FileOutputStream(outputFile);
                bm.compress(Bitmap.CompressFormat.PNG, 100, fout);
                fout.flush();
                fout.close();
                bm.recycle();
                bm = null;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return (mPath + "/share.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String heart = Integer.toHexString(0xFF & aMessageDigest);
                while (heart.length() < 2)
                    heart = "0" + heart;
                hexString.append(heart);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
