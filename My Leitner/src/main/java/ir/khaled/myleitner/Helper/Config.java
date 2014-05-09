package ir.khaled.myleitner.Helper;

import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import ir.khaled.myleitner.view.AppEditText;

/**
 * Created by kh.bakhtiari on 5/6/2014.
 */
public class Config {
    public static final String SERVER_ADDRESS = "192.168.1.41";
    public static final int SERVER_PORT = 44485;
    public static final String urlWebService = "http://ws.iranapps.ir/v2/";
    public static final String SHARED_PREFS_NAME = "iranapps";
    public static final String FONT_UI = "font_ui.ttf";
    public static final String FONT_MULTI_LINE = "font_multi_line.ttf";
    private static Typeface fontUi;
    private static Typeface fontMultiLine;
    private static boolean isDownloadingFonts = false;
    public static boolean debugMode = true;//TODO make this false for realese

    public static Typeface getAppFontUI(Context context) {
        String filePath = context.getFilesDir() + "/";
        File font = new File(filePath + "font/" + FONT_UI);

//        return Typeface.createFromAsset(context.getAssets(), "font/font_ui.ttf");

        if (font.exists()) {
            if (fontUi == null) {
                try {
                    fontUi = Typeface.createFromFile(font);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return fontUi == null ? Typeface.DEFAULT : fontUi;
        } else {
            downloadFont(context);
            return Typeface.DEFAULT;
        }
    }

    public static Typeface getAppFontMultiple(Context context) {
        String filePath = context.getFilesDir() + "/";
        File font = new File(filePath + "font/" + FONT_MULTI_LINE);

//        return Typeface.createFromAsset(context.getAssets(), "font/font_multi_line.ttf");

        if (font.exists()) {
            if (fontMultiLine == null) {
                try {
                    fontMultiLine = Typeface.createFromFile(font);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return fontMultiLine == null ? Typeface.DEFAULT : fontMultiLine;
        } else {
            downloadFont(context);
            return Typeface.DEFAULT;
        }
    }

    public static void downloadFont(Context context) {
        String filesPath = context.getFilesDir() + "/";
        final File fontUI = new File(filesPath + "font/" + FONT_UI + ".tmp");
        final File fontMultiLine = new File(filesPath + "font/" + FONT_MULTI_LINE + ".tmp");

        (new File(filesPath + "font")).mkdir();

        try {
            if (fontUI.createNewFile()) {
                isDownloadingFonts = true;

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getFileFromWeb("http://developer.iranapps.ir/app/font_ui.ttf", fontUI);
                        getFileFromWeb("http://developer.iranapps.ir/app/font_multi_line.ttf", fontMultiLine);
                    }
                }).start();
            } else {
                if (!isDownloadingFonts) {
                    fontUI.delete();
                    fontMultiLine.delete();
//                    downloadFont(context);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getFileFromWeb(String urlStr, File file) {
        try {
            URL url = new URL(urlStr);
            URLConnection connection = url.openConnection();
            connection.connect();

            BufferedInputStream input = new BufferedInputStream(url.openStream(), 8192);
            BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file));

            byte data[] = new byte[1024];
            int count = 0;
            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }
            output.flush();
            output.close();
            input.close();

            File renamedFile = new File(file.getPath().substring(0, file.getPath().length() - 4));
            file.renameTo(renamedFile);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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