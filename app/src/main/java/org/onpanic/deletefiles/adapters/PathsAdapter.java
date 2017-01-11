package org.onpanic.deletefiles.adapters;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import org.onpanic.deletefiles.R;
import org.onpanic.deletefiles.fragments.PathsListFragment;
import org.onpanic.deletefiles.providers.PathsProvider;

import java.io.File;


public class PathsAdapter extends CursorRecyclerViewAdapter<PathsAdapter.ViewHolder> {

    private final PathsListFragment.OnPathListener mListener;
    private Context mContext;

    public PathsAdapter(Context context, Cursor cursor, PathsListFragment.OnPathListener listener) {
        super(cursor);
        mContext = context;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_path, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, Cursor cursor) {
        final int id = cursor.getInt(cursor.getColumnIndex(PathsProvider.Path._ID));
        final String file_name = cursor.getString(cursor.getColumnIndex(PathsProvider.Path.PATH));
        final Boolean active = (cursor.getInt(cursor.getColumnIndex(PathsProvider.Path.ENABLED)) == 1);

        viewHolder.mActive.setChecked(active);
        viewHolder.mActive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ContentResolver resolver = mContext.getContentResolver();
                ContentValues fields = new ContentValues();
                fields.put(PathsProvider.Path.ENABLED, isChecked);
                resolver.update(
                        PathsProvider.CONTENT_URI, fields, "_ID=" + id, null
                );
            }
        });

        File file = new File(file_name);

        if (file.isDirectory()) {
            viewHolder.mImage.setImageResource(R.drawable.ic_file_folder_open);
        } else {
            viewHolder.mImage.setImageResource(R.drawable.ic_regular_file);
        }

        viewHolder.mName.setText(file.getName());
        viewHolder.mName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onPathListenerCallback(id);
                }
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView mImage;
        final Switch mActive;
        final TextView mName;

        ViewHolder(View view) {
            super(view);
            mImage = (ImageView) view.findViewById(R.id.contact_image);
            mActive = (Switch) view.findViewById(R.id.contact_active);
            mName = (TextView) view.findViewById(R.id.contact_name);
        }
    }
}
