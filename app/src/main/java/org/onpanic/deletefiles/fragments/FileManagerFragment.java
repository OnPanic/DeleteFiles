package org.onpanic.deletefiles.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import org.onpanic.deletefiles.R;
import org.onpanic.deletefiles.adapters.FMItemsAdapter;
import org.onpanic.deletefiles.ui.SimpleDividerItemDecoration;

import java.util.ArrayList;

import static android.os.Environment.getExternalStorageDirectory;

public class FileManagerFragment extends Fragment {
    private Context mContext;
    private RecyclerView recyclerView;
    private FMItemsAdapter adapter;
    private ImageButton fmBack;
    private Button fmCancel;
    private Button fmSave;
    private OnSavePaths mListener;

    public FileManagerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.filemanager_layout, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.fm_list);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(mContext));
        recyclerView.setHasFixedSize(true); // does not change, except in onResume()
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(adapter);

        fmBack = (ImageButton) v.findViewById(R.id.fm_back);
        fmBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.goUp();
            }
        });

        fmCancel = (Button) v.findViewById(R.id.fm_cancel);
        fmCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getFragmentManager().popBackStack();
            }
        });

        fmSave = (Button) v.findViewById(R.id.fm_save);
        fmSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.save(adapter.getSelectedFiles());
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        adapter = new FMItemsAdapter(getExternalStorageDirectory());

        if (context instanceof OnSavePaths) {
            mListener = (OnSavePaths) mContext;
        } else {
            throw new RuntimeException(mContext.toString()
                    + " must implement OnSavePaths");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        adapter = null;
        mListener = null;
    }

    public interface OnSavePaths {
        void save(ArrayList<String> files);
    }
}
