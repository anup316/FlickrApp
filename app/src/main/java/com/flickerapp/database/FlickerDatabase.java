package com.flickerapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.flickerapp.utility.Constants;
import com.flickerapp.models.Photo;

import static com.flickerapp.utility.Constants.V_ID;

public class FlickerDatabase extends SQLiteOpenHelper {

    private Context mContext;
    private static FlickerDatabase mInstance = null;

    public FlickerDatabase(Context context, String val) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
        mContext = context;
    }

    public static FlickerDatabase getInstance(Context context) {

        if (mInstance == null) {
            mInstance = new FlickerDatabase(context.getApplicationContext(), "val");
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FlickerDbTableSchema.DATABASE_CREATE_PLATE_INFO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + Constants.FLICKER_PHOTO_TABLE);
            onCreate(db);
        }
    }

    public void insertPhoto(Photo photo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        db.beginTransaction();
        try {
            contentValues.put(V_ID, photo.getId());
            contentValues.put(Constants.V_IMAGE_PATH, photo.getImagePath());
            db.replaceOrThrow(Constants.FLICKER_PHOTO_TABLE, null, contentValues);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public Cursor getAllPhotos() {
        SQLiteDatabase db = this.getReadableDatabase();
        String buildSQL = "SELECT * FROM " + Constants.FLICKER_PHOTO_TABLE;
        return db.rawQuery(buildSQL, null);
    }

}
