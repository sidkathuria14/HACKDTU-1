package com.example.limit_breaker.auto_task.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import java.net.URI;

/**
 * Created by limit-breaker on 31/7/17.
 */

public class SituationContract {

    public static final String CONTENT_AUTHORITY=
            "com.example.limit_breaker.provider.SituationProvider";
    public static final Uri BASE_CONTENT_URI= Uri.parse("content://"+CONTENT_AUTHORITY);

    private SituationContract() {
    }

    public static class SituationEntry implements BaseColumns{
        public static final String TABLE_SITUATIONS="situations";
        public static final String COLUMN_ID="_id";
        public static final String COLUMN_HEADPHONESTATE="headphone_state";
        public static final String COLUMN_WEATHER_STATE="weather_state";
        public static final String COLUMN_PLACE="place";
        public static final String COLUMN_LATITUDE="latitude";
        public static final String COLUMN_LONGITUDE="longitude";
        public static final String COLUMN_TIME="time";
        public static final String COLUMN_APP_NAME="app_name";
        public static final String COLUMN_ACTIVITY="activity";

        public static final String [] projections=
                {COLUMN_ID,COLUMN_APP_NAME,COLUMN_ACTIVITY,COLUMN_TIME,
                COLUMN_PLACE,COLUMN_LATITUDE,COLUMN_LONGITUDE,
                        COLUMN_HEADPHONESTATE,COLUMN_WEATHER_STATE};

        public static final Uri CONTENT_URI=BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_SITUATIONS).build();

        public static final String CONTENT_DIR_TYPE=
                ContentResolver.CURSOR_DIR_BASE_TYPE+"/"
                        +CONTENT_AUTHORITY+"/"+TABLE_SITUATIONS;

    }
}
