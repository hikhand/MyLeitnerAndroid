package ir.khaled.myleitner.helper;

import android.util.Log;

/**
 * Created by kh.bakhtiari on 5/19/2014.
 */
public class Logger {

    public static void LogD(String tag, String log) {
        if (!Config.debugMode)
            return;

        Log.d(tag, log);
    }

    public static void LogI(String tag, String log) {
        if (!Config.debugMode)
            return;

        Log.i(tag, log);
    }
}
