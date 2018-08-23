package com.flickerapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.flickerapp.network.LoadImageAsync;
import com.flickerapp.utility.FlickerUtils;
import com.flickerapp.models.Photo;

import java.util.List;

/**
 * Created by GUR47272 on 21-08-2018.
 */

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> implements LoadImageAsync.OnImageDownloadCallback {

    private final List<Photo> mPhotoList;
    private final LayoutInflater mInflater;
    private final boolean isNetworkAvailable;

    GridAdapter(Context context, List<Photo> photoList) {
        this.mInflater = LayoutInflater.from(context);
        this.mPhotoList = photoList;
        this.isNetworkAvailable = FlickerUtils.isInternetAvailable(mInflater.getContext());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.view_photo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Photo photo = mPhotoList.get(position);
        String builder = "http://farm" +
                photo.getFarm() +
                ".static.flickr.com/" +
                photo.getServer() +
                "/" +
                photo.getId() +
                "_" +
                photo.getSecret() +
                ".jpg";
        // holder.photoView.setImageBitmap(null);
        new LoadImageAsync(holder.photoView, this, position, this.isNetworkAvailable).execute(builder, photo.getImagePath());
    }


    @Override
    public int getItemCount() {
        return mPhotoList.size();
    }

    @Override
    public void onSuccess(Bitmap bitmap, int pos, boolean isCachedVersionUsed) {
        if (!isCachedVersionUsed)
            FlickerUtils.insertFlickerPhoto(mInflater.getContext(), mPhotoList.get(pos), FlickerUtils.getBytes(bitmap));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView photoView;

        ViewHolder(View itemView) {
            super(itemView);
            photoView = itemView.findViewById(R.id.info_text);
        }
    }
}