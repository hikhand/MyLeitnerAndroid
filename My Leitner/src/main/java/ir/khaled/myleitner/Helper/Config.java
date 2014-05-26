package ir.khaled.myleitner.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.util.List;

import ir.khaled.myleitner.view.AppEditText;

/**
 * Created by kh.bakhtiari on 5/6/2014.
 */
public class Config {
    public static final int SERVER_PORT = 44485;
    public static final String urlWebService = "http://ws.iranapps.ir/v2/";
    public static final String SHARED_PREFS_NAME = "iranapps";
    public static boolean debugMode = true;//TODO make this false for realese


    public static String getKeyFromUrl(String url, String key) {
        if (url != null) {
            Uri uri = Uri.parse(url);
            String host = uri.getHost();
            if (host != null) {
                if (host.equals("iranapps.ir")) {
                    List<String> segments = uri.getPathSegments();
                    if (segments != null && segments.size() >= 3) {
                        if (segments.get(1).equals(key)) {
                            return uri.getPathSegments().get(2);
                        }
                    }
                }
            }
        }
        return null;
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static void showKeyboard(AppEditText editText, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        editText.requestFocus();
    }

    public static int currentTimeSeconds() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    /**
     * calculates the size of a directory by checking the length og each file,
     * this could be a long process its recommended to run it in background
     * @return the size of directory or 0 on failure
     */
    public static long getDirSize(File dir) {
        if (dir == null)
            return 0;
        if (dir.exists()) {
            long result = 0;
            File[] fileList = dir.listFiles();
            if (fileList == null)
                return 0;
            for (File aFile : fileList) {
                // Recursive call if it's a directory
                if (aFile.isDirectory()) {
                    result += getDirSize(aFile);
                } else {
                    // Sum the file size in bytes
                    result += aFile.length();
                }
            }
            return result; // return the file size
        }
        return 0;
    }

    public static int getDirFileCount(File dir) {
        if (dir == null)
            return 0;
        if (dir.exists()) {
            File[] fileList = dir.listFiles();
            if (fileList == null)
                return 0;
            return fileList.length;
        }
        return 0;
    }

    public static void logD(String tag, String message) {
        if (debugMode)
            Log.d(tag, message);
    }

}