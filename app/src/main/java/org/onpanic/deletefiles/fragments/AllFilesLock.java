package org.onpanic.deletefiles.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.onpanic.deletefiles.R;

public class AllFilesLock extends Fragment {


    public AllFilesLock() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.all_files_layout, container, false);
    }

}
