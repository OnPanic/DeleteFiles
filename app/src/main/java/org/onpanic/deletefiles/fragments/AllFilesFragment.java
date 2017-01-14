package org.onpanic.deletefiles.fragments;


import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.onpanic.deletefiles.R;

public class AllFilesFragment extends Fragment {
    private Context mContext;

    public AllFilesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Snackbar.make(getActivity().findViewById(android.R.id.content),
                R.string.all_files_will_be_deleted,
                Snackbar.LENGTH_INDEFINITE).setAction(R.string.disable,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);

                        SharedPreferences.Editor edit = preferences.edit();
                        edit.putBoolean(mContext.getString(R.string.pref_delete_all), false);
                        edit.apply();
                        getFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new PathsListFragment())
                                .commit();
                    }
                }).show();

        return inflater.inflate(R.layout.fragment_locked, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
