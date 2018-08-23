package com.flickerapp.architecture;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.view.View;

import com.flickerapp.R;
import com.flickerapp.database.FlickerDatabase;
import com.flickerapp.models.Photo;
import com.flickerapp.network.PhotoListAsync;
import com.flickerapp.utility.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GUR47272 on 23-08-2018.
 */

public class PhotoFramePresenterImpl implements PhotoFramePresenter, PhotoListAsync.OnTaskFinishListener {

    private PhotoFrameView mView;

    public PhotoFramePresenterImpl(PhotoFrameView view) {
        this.mView = view;

    }

    @Override
    public void onViewInitialized(final boolean isInternetAvailable, final FlickerDatabase database) {
        mView.setUpViews();
        if (isInternetAvailable) {
            mView.setSearchViewVisibility(View.VISIBLE);
        } else {
            mView.setSearchViewVisibility(View.GONE);
            final List<Photo> photoListInfoFromDatabase = getPhotoListInfoFromDatabase(database);
            if (photoListInfoFromDatabase.size() > 0) {
                mView.setAdapter(photoListInfoFromDatabase);
            } else {
                mView.showErrorMessage(R.string.error_text_db);
            }
        }
    }

    @Override
    public void bind(PhotoFrameView view) {
        this.mView = view;
    }

    @Override
    public void unbind() {
        this.mView = null;
    }

    @Override
    public void onSearchText(String query) {
        if (query != null && query.trim().length() > 0) {
            mView.showLoading();
            new PhotoListAsync(this).execute(query);
        }
    }


    @Override
    public void onSuccess(JSONObject object) {
        if (mView != null) {
            mView.hideLoading();
            final List<Photo> photoList = new ArrayList<>();
            try {
                final JSONObject photos = object.getJSONObject("photos");
                final JSONArray photosJSONArray = photos.getJSONArray("photo");
                for (int i = 0; i < photosJSONArray.length(); i++) {
                    final JSONObject jsonObject = photosJSONArray.getJSONObject(i);
                    final Photo photo = new Photo(jsonObject);
                    photoList.add(photo);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (photoList.size() > 0) {
                mView.setAdapter(photoList);
            }
        }

    }


    @Override
    public void onError() {
        mView.showErrorMessage(R.string.error_text);
    }

    private List<Photo> getPhotoListInfoFromDatabase(FlickerDatabase database) {
        final List<Photo> photoList = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = database.getAllPhotos();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    final String images = cursor.getString(cursor.getColumnIndex(Constants.V_IMAGE_PATH));
                    final Photo photo = new Photo(images);
                    photoList.add(photo);
                } while (cursor.moveToNext());

            }
        } catch (SQLiteException e) {
            Log.e("SQLiteException ", e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return photoList;
    }
}
