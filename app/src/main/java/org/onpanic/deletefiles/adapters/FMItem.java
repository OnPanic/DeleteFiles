package org.onpanic.deletefiles.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import org.onpanic.deletefiles.R;

import java.io.File;

public class FMItem extends RecyclerView.Adapter<FMItem.ViewHolder> {
    private File[] dirContent;

    public FMItem(File[] dir_content) {
        dirContent = dir_content;
    }

    @Override
    public FMItem.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fm_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FMItem.ViewHolder holder, int position) {
        File current = dirContent[position];
        holder.file = current;
        holder.name.setText(current.getName());
    }

    @Override
    public int getItemCount() {
        return dirContent.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public final CheckBox selected;
        public final ImageView type;
        public final TextView name;
        public File file;

        ViewHolder(final View row) {
            super(row);
            type = (ImageView) row.findViewById(R.id.fm_item_type);
            name = (TextView) row.findViewById(R.id.fm_item_name);
            selected = (CheckBox) row.findViewById(R.id.fm_item_selected);
        }
    }
}
