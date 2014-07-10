package ir.khaled.myleitner.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kh.bakhtiari on 7/10/2014.
 */
public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "leitner";
    private static final String TABLE_USER = "user";
    private static final int DATABASE_VERSION = 1;
    private static Database instance;

    private Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized Database getInstance(Context context) {
        if (instance == null)
            instance = new Database(context);

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tableUser = "CREATE TABLE IF NO EXISTS 'user' (\n" +
                "'USER_ID'  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "'FIRST_NAME'  TEXT,\n" +
                "'LAST_NAME'  TEXT,\n" +
                "'DISPLAY_NAME'  TEXT,\n" +
                "'EMAIL_ADDRESS'  TEXT,\n" +
                "'PICTURE'  TEXT\n" +
                ");";

        db.execSQL(tableUser);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    
}
