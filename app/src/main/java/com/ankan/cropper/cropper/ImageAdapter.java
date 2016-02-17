package com.ankan.cropper.cropper;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ank@n on 16-02-2016.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageViewHolder> {

    private List<String> images = new ArrayList<String>();
    private Activity mActivity;

    public ImageAdapter(Activity activity, File storageDir) {
        this.addAll(storageDir);
        this.mActivity = activity;
    }

    public void addAll(File dir) {
        final File[] found = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.startsWith(Utilities.IMAGE_PREFIX);
            }
        });

        if (found == null) return;

        // add already existing images
        for (int i = 0; i < found.length; i++)
            images.add(found[i].getAbsolutePath());

        // notify the adapter for new files
        notifyDataSetChanged();
    }

    public void add(String path) {
        images.add(path);
    }

    public int getCount() {
        return getItemCount();
    }

    public void clear() {
        this.images.clear();
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity)
                .inflate(R.layout.grid_item_images, parent, false);

        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder, int position) {
        holder.mBoundPath = images.get(position);

        // downsample the image
        Utilities.loadScaledBitmap(holder.mBoundPath, holder.mImageView, 100, 100);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (images == null)
            return 0;
        return images.size();
    }

}
