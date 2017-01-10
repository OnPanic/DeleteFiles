package org.onpanic.deletefiles.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.onpanic.deletefiles.database.PathsDB;


public class PathsProvider extends ContentProvider {

    private static final String AUTH = "org.onpanic.deletefiles.FILE_PATHS_PROVIDER";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTH + "/paths");

    //UriMatcher
    private static final int PATHS = 1;
    private static final int PATH_ID = 2;
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTH, "paths", PATHS);
        uriMatcher.addURI(AUTH, "paths/#", PATH_ID);
    }

    private PathsDB pathsDB;
    private Context mContext;

    @Override
    public boolean onCreate() {
        mContext = getContext();
        pathsDB = new PathsDB(mContext);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String where = selection;
        if (uriMatcher.match(uri) == PATH_ID) {
            where = "_id=" + uri.getLastPathSegment();
        }

        SQLiteDatabase db = pathsDB.getReadableDatabase();

        return db.query(PathsDB.PATHS_TABLE_NAME, projection, where,
                selectionArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = uriMatcher.match(uri);

        switch (match) {
            case PATHS:
                return "vnd.android.cursor.dir/vnd.deletefiles.paths";
            case PATH_ID:
                return "vnd.android.cursor.item/vnd.deletefiles.path";
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        long regId;

        SQLiteDatabase db = pathsDB.getWritableDatabase();

        regId = db.insert(PathsDB.PATHS_TABLE_NAME, null, values);

        mContext.getContentResolver().notifyChange(CONTENT_URI, null);

        return ContentUris.withAppendedId(CONTENT_URI, regId);
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        String where = selection;
        if (uriMatcher.match(uri) == PATH_ID) {
            where = "_id=" + uri.getLastPathSegment();
        }

        SQLiteDatabase db = pathsDB.getWritableDatabase();

        Integer rows = db.delete(PathsDB.PATHS_TABLE_NAME, where, selectionArgs);

        mContext.getContentResolver().notifyChange(CONTENT_URI, null);

        return rows;

    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = pathsDB.getWritableDatabase();
        Integer rows = db.update(PathsDB.PATHS_TABLE_NAME, values, selection, selectionArgs);
        mContext.getContentResolver().notifyChange(CONTENT_URI, null);
        return rows;
    }

    public static final class Path implements BaseColumns {

        public static final String ENABLED = "enabled";
        public static final String PATH = "path";

        private Path() {
        }
    }
}
