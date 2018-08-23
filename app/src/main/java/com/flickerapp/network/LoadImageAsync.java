package com.flickerapp.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.flickerapp.R;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.graphics.BitmapFactory.decodeStream;

/**
 * Created by Anup Lal on 21-08-2018.
 */

public class LoadImageAsync extends AsyncTask<String, Void, Bitmap> {

    private final WeakReference<ImageView> imageViewReference;
    private final OnImageDownloadCallback callback;
    private final int pos;
    private final boolean isNetworkAvailable;
    private boolean isCachedVersionUsed;

    public LoadImageAsync(ImageView imageView, OnImageDownloadCallback callback, int pos, boolean internetAvailable) {
        this.imageViewReference = new WeakReference<>(imageView);
        this.callback = callback;
        this.pos = pos;
        this.isNetworkAvailable = internetAvailable;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        try {
            final Bitmap image = getCachedImage(params[1]);
            if (isNetworkAvailable) {
                if (image == null) {
                    isCachedVersionUsed = false;
                    return downloadBitmap(params[0]);
                } else {
                    isCachedVersionUsed = true;
                    return image;
                }
            } else {
                return image;
            }
        } catch (Exception e) {
            // Error Handling if required
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }

        if (imageViewReference != null) {
            ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                    if (isNetworkAvailable)
                        callback.onSuccess(bitmap, pos, isCachedVersionUsed);
                } else {
                    Drawable placeholder = imageView.getContext().getResources().getDrawable(R.drawable.ic_launcher_foreground);
                    imageView.setImageDrawable(placeholder);
                }
            }
        }
    }

    private Bitmap downloadBitmap(String url) {
        HttpURLConnection urlConnection = null;
        try {
            final URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();
            final int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                return null;
            }

            final InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null) {
                return decodeStream(inputStream);
            }
        } catch (Exception e) {
            urlConnection.disconnect();
            Log.w("ImageDownloader", "Error downloading image from " + url);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    private Bitmap getCachedImage(String absolutePath) {
        final String photoPath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/FlickrPhotos" + "/" + absolutePath;
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        final Bitmap bmp = BitmapFactory.decodeFile(photoPath, options);
        return bmp;
    }

    public interface OnImageDownloadCallback {
        void onSuccess(Bitmap bitmap, int pos, boolean isCachedVersionUsed);
    }
}