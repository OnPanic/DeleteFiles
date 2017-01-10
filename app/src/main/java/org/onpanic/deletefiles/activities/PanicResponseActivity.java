package org.onpanic.deletefiles.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import org.onpanic.deletefiles.R;
import org.onpanic.deletefiles.notifications.TriggerNotification;

import info.guardianproject.panic.PanicResponder;

public class PanicResponseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (PanicResponder.receivedTriggerFromConnectedApp(this)) {

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            //  TODO

            if (prefs.getBoolean(getString(R.string.pref_runned_notification), false)) {
                TriggerNotification notification = new TriggerNotification(getApplicationContext());
                notification.show();
            }
        }

        finish();
    }
}
