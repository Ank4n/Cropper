package com.ankan.cropper.cropper;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by Ank@n on 16-02-2016.
 */
public class ImageViewHolder extends RecyclerView.ViewHolder {

    public final View mView;
    public final ImageView mImageView;
    public String mBoundPath;

    public ImageViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mImageView = (ImageView) itemView.findViewById(R.id.grid_item_imageview);
    }
}
