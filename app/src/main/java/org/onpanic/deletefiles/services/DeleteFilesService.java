package org.onpanic.deletefiles.services;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.IBinder;
import android.preference.PreferenceManager;

import org.onpanic.deletefiles.R;
import org.onpanic.deletefiles.constants.DeleteFilesConstants;
import org.onpanic.deletefiles.notifications.TriggerNotification;
import org.onpanic.deletefiles.providers.PathsProvider;

import java.io.File;

import static android.os.Environment.getExternalStorageDirectory;

public class DeleteFilesService extends Service {
    private SharedPreferences prefs;

    public DeleteFilesService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {

        if (intent != null && intent.getAction().equals(DeleteFilesConstants.DELETE_FILES)) {

            prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            if (prefs.getBoolean(getString(R.string.pref_delete_all), false)) {
                new Thread(new Runnable() {
                    public void run() {
                        File root = getExternalStorageDirectory();
                        for (File f : root.listFiles()) {
                            deleteRecursive(f);
                        }
                        notifyRun();
                        stopSelf(startId);
                    }
                }).start();
            } else {
                new Thread(new Runnable() {
                    public void run() {
                        ContentResolver cr = getContentResolver();
                        String[] mProjection = new String[]{
                                PathsProvider.Path._ID,
                                PathsProvider.Path.PATH,
                                PathsProvider.Path.ENABLED
                        };

                        Cursor files = cr.query(PathsProvider.CONTENT_URI, mProjection, PathsProvider.Path.ENABLED + "=1", null, null);

                        if (files != null && files.getCount() > 0) {
                            while (files.moveToNext()) {
                                File current = new File(files.getString(files.getColumnIndex(PathsProvider.Path.PATH)));

                                if (current.exists()) {
                                    if (current.isDirectory()) {
                                        deleteRecursive(current);
                                    } else {
                                        current.delete();
                                    }
                                }

                                cr.delete(PathsProvider.CONTENT_URI,
                                        PathsProvider.Path._ID + "=" + files.getInt(files.getColumnIndex(PathsProvider.Path._ID)),
                                        null);
                            }

                            files.close();
                            notifyRun();
                        }

                        stopSelf(startId);
                    }
                }).start();
            }
        }

        return Service.START_STICKY;
    }

    private void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursive(child);
            }
        }

        fileOrDirectory.delete();
    }

    private void notifyRun() {
        if (prefs.getBoolean(getString(R.string.pref_runned_notification), false)) {
            TriggerNotification notification = new TriggerNotification(getApplicationContext());
            notification.show();
        }
    }
}
