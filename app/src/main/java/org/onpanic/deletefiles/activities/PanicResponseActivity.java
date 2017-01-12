package org.onpanic.deletefiles.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import org.onpanic.deletefiles.R;
import org.onpanic.deletefiles.constants.DeleteFilesConstants;
import org.onpanic.deletefiles.notifications.TriggerNotification;
import org.onpanic.deletefiles.services.DeleteFilesService;

import info.guardianproject.panic.PanicResponder;

public class PanicResponseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (PanicResponder.receivedTriggerFromConnectedApp(this)) {

            // File deletion can take a while
            Intent intent = new Intent(this, DeleteFilesService.class);
            intent.setAction(DeleteFilesConstants.DELETE_FILES);

            startService(intent);

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            if (prefs.getBoolean(getString(R.string.pref_runned_notification), false)) {
                TriggerNotification notification = new TriggerNotification(getApplicationContext());
                notification.show();
            }
        }

        finish();
    }
}
