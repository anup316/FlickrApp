package com.flickerapp.utility;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;

import com.flickerapp.CacheResponseService;
import com.flickerapp.models.Photo;

import java.io.ByteArrayOutputStream;

/**
 * Created by GUR47272 on 22-08-2018.
 */

public class FlickerUtils {

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }


    public static boolean isInternetAvailable(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi =
                connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        final android.net.NetworkInfo mobile =
                connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return wifi.isConnectedOrConnecting() || mobile.isConnectedOrConnecting();

    }

    public static void insertFlickerPhoto(Context context, Photo photo, byte[] image) {
        Intent responseServiceIntent = new Intent(context, CacheResponseService.class);
        responseServiceIntent.putExtra(Constants.FLICKR_DB_INSERT_REQ_FROM_KEY, Constants.FLICKR_PHOTO);
        responseServiceIntent.putExtra(Constants.INSERT_PHOTO_KEY, photo);
        responseServiceIntent.putExtra(Constants.IMAGE_KEY, image);
        context.startService(responseServiceIntent);
    }
}
