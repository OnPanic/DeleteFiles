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

public class DeleteFilesService extends Service {
    public DeleteFilesService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {

        if (intent != null && intent.getAction().equals(DeleteFilesConstants.DELETE_FILES)) {
            final ContentResolver cr = getContentResolver();
            String[] mProjection = new String[]{
                    PathsProvider.Path._ID,
                    PathsProvider.Path.PATH,
                    PathsProvider.Path.ENABLED
            };

            final Cursor files = cr.query(PathsProvider.CONTENT_URI, mProjection, PathsProvider.Path.ENABLED + "=1", null, null);

            if (files == null || files.getCount() < 1) {
                stopSelf(startId);
            }

            new Thread(new Runnable() {
                public void run() {

                    while (files.moveToNext()) {
                        File current = new File(files.getString(files.getColumnIndex(PathsProvider.Path.PATH)));
                        if (current.exists()) current.delete();
                        cr.delete(PathsProvider.CONTENT_URI,
                                PathsProvider.Path._ID + "=" + files.getInt(files.getColumnIndex(PathsProvider.Path._ID)),
                                null);
                    }

                    files.close();

                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    if (prefs.getBoolean(getString(R.string.pref_runned_notification), false)) {
                        TriggerNotification notification = new TriggerNotification(getApplicationContext());
                        notification.show();
                    }

                    stopSelf(startId);
                }
            }).start();
        }

        return Service.START_STICKY;
    }
}
