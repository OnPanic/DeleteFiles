package org.onpanic.deletefiles.fragments;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import org.onpanic.deletefiles.R;


public class DeleteFilesSettings extends PreferenceFragment {

    public DeleteFilesSettings() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.deletefiles_settings);

        Preference triggerApp = (Preference) findPreference(getString(R.string.pref_trigger_app));
        triggerApp.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                getFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.fragment_container, new TriggerApps())
                        .commit();

                return false;
            }
        });
    }
}
