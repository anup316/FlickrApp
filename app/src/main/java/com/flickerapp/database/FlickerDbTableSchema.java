package com.flickerapp.database;

import com.flickerapp.utility.Constants;

public class FlickerDbTableSchema {

    public static final String DATABASE_CREATE_PLATE_INFO = "create table if not exists " + Constants.FLICKER_PHOTO_TABLE + "("
            + Constants.V_ID + " string PRIMARY KEY null, " + Constants.V_IMAGE_PATH + " string " + ")";
}
