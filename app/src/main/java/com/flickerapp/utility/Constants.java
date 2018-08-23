package com.flickerapp.utility;

public class Constants {
    public static final String DATABASE_NAME = "flicker-db.db";
    public static final int DATABASE_VERSION = 1;

    public static final String FLICKER_PHOTO_TABLE = "flickr_photo_table";
    public static final String V_ID = "id";
    public static final String V_IMAGE_PATH = "imagePath";

    public static final String FLICKR_DB_INSERT_REQ_FROM_KEY = "requestFor";
    public static final int FLICKR_PHOTO = 101;
    public static final String INSERT_PHOTO_KEY = "insert-photo";
    public static final String IMAGE_KEY = "image-photo";
    public static final int REQUEST_PERMISSION = 0;
    public static final String URL = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=3e7cc266ae2b0e0d78e279ce8e361736&format=json&nojsoncallback=1&safe_search=1&text=";

}
