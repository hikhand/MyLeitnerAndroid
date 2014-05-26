package ir.khaled.myleitner.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.File;

/**
 * Created by kh.bakhtiari on 4/28/2014.
 */
public class StorageHelper {
    /**
     * iranapps global SharedPreferences with the key of {@code iranapps}
     */
    private static SharedPreferences globalSharedPreferences;
    /**
     * iranapps {@link #globalSharedPreferences} editor
     */
    private static SharedPreferences.Editor editorGlobalSharedPreferences;

    /**
     * iranapps default SharedPreferences used in settings
     */
    private static SharedPreferences defaultSharedPreferences;

    /**
     * tries to get cache directory in external storage if succeed returns it
     *  other wise gets the cache directory in internal storage <br/>
     * also creates the folder requested into the cache directory.
     *
     * @param context used to get the cache directory from.
     * @param folderName the folder name to be created and included in the path.
     * @return path to the cache directory.
     */
    public static String getCacheDir(Context context, String folderName) {
        File cacheDir = null;
        try {
            cacheDir = context.getExternalCacheDir();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (cacheDir == null) {
            //TODO check is it ok to return internal cache dir concerning cache sizes
            File file = new File(context.getCacheDir() + "/" + folderName + "/");
            file.mkdirs();
            return file.getPath() + "/";
        } else {
            File file = new File(cacheDir + "/" + folderName + "/");
            file.mkdirs();
            return file.getPath() + "/";
        }
    }

    public static SharedPreferences.Editor getSharedPreferencesEditor(Context context) {
        if (editorGlobalSharedPreferences == null)
            editorGlobalSharedPreferences = getSharedPreferences(context).edit();

        return editorGlobalSharedPreferences;
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        if (defaultSharedPreferences == null)
            defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return defaultSharedPreferences;
    }
}
