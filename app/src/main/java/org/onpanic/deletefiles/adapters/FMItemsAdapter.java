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
import java.util.ArrayList;

public class FMItemsAdapter extends RecyclerView.Adapter<FMItemsAdapter.ViewHolder> {
    private File[] dirContent;

    private ArrayList<File> prevDir;
    private File currentDir;

    public FMItemsAdapter(File current) {
        currentDir = current;
        dirContent = currentDir.listFiles();
        prevDir = new ArrayList<>();
    }

    @Override
    public FMItemsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fm_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FMItemsAdapter.ViewHolder holder, int position) {
        final File current = dirContent[position];
        holder.file = current;
        holder.name.setText(current.getName());
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (current.isDirectory()) {
                    prevDir.add(currentDir);
                    currentDir = current;
                    dirContent = current.listFiles();
                    notifyDataSetChanged();
                } else {
                    holder.selected.toggle();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dirContent.length;
    }

    public void goUp() {
        if (prevDir.size() >= 1) {
            File dir = prevDir.remove(prevDir.size() - 1);
            dirContent = dir.listFiles();
            notifyDataSetChanged();
        }
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
