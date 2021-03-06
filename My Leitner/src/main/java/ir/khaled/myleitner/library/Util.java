package ir.khaled.myleitner.library;

import android.content.Context;
import android.graphics.Typeface;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by kh.bakhtiari on 5/25/2014.
 */
public class Util {
    public static final String SERVER_ADDRESS = "192.168.1.2";
    public static final int SERVER_PORT = 4455;
    private static final String FONT_UI = "font_ui.ttf";
    private static final String FONT_MULTI_LINE = "font_multi_line.ttf";
    private static Typeface fontUi;
    private static Typeface fontMultiLine;
    private static boolean isDownloadingFonts = false;

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

    public static boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toString((messageDigest[i] & 0xff) + 0x100, 16).substring(1));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
