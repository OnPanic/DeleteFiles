package org.onpanic.deletefiles.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PathsDB extends SQLiteOpenHelper {

    public static final String PATHS_TABLE_NAME = "paths";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "delete_files";

    private static final String PATHS_TABLE_CREATE =
            "CREATE TABLE " + PATHS_TABLE_NAME + " (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "enabled INTEGER DEFAULT 1, " +
                    "path TEXT );";

    public PathsDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PATHS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

