package com.flickerapp;

import android.app.IntentService;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.flickerapp.database.FlickerDatabase;
import com.flickerapp.utility.Constants;
import com.flickerapp.utility.FlickerUtils;
import com.flickerapp.models.Photo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CacheResponseService extends IntentService {

    public CacheResponseService() {
        super("CacheResponseService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            if (bundle == null) {
                return;
            }
            switch (bundle.getInt(Constants.FLICKR_DB_INSERT_REQ_FROM_KEY)) {
                case Constants.FLICKR_PHOTO:
                    final Photo photo = intent.getParcelableExtra(Constants.INSERT_PHOTO_KEY);
                    final byte[] image = intent.getByteArrayExtra(Constants.IMAGE_KEY);
                    FlickerDatabase.getInstance(getApplicationContext()).insertPhoto(photo);
                    saveImageToFile(photo, image);
                    break;
            }

        } catch (SQLiteException e) {
            Log.e("SQLiteException ", e.getMessage());
        }
    }

    void saveImageToFile(Photo photo, byte[] image) {
        final String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/FlickrPhotos";
        final File dir = new File(file_path);
        if (!dir.exists())
            dir.mkdirs();
        final File file = new File(dir, photo.getId() + "_" + photo.getSecret() + "_" + photo.getFarm() + ".png");

        try {
            final FileOutputStream fOut = new FileOutputStream(file);
            final Bitmap bmp = FlickerUtils.getImage(image);
            bmp.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
