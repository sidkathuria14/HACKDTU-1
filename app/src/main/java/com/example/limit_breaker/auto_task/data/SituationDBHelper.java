package com.example.limit_breaker.auto_task.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.limit_breaker.auto_task.utils.SituattionDBUtils;

/**
 * Created by limit-breaker on 31/7/17.
 */

public class SituationDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VER=1;
    public static final String DATABASE_NAME="situations.db";


    public SituationDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String create_table_situations=
                SituattionDBUtils.SQL_CREATE_TABLE+
                        SituationContract.SituationEntry.TABLE_SITUATIONS+
                        SituattionDBUtils.BRACKET_OPEN+
                        SituationContract.SituationEntry.COLUMN_ID+
                        SituattionDBUtils.PRIME_KEY_AUTO_INC+","+
                        SituationContract.SituationEntry.COLUMN_APP_NAME+SituattionDBUtils.TEXT_NOT_NULL+","+
                        SituationContract.SituationEntry.COLUMN_ACTIVITY+SituattionDBUtils.TEXT_NOT_NULL+","+
                        SituationContract.SituationEntry.COLUMN_TIME+SituattionDBUtils.TEXT_NOT_NULL+","+
                        SituationContract.SituationEntry.COLUMN_PLACE+SituattionDBUtils.TEXT_NOT_NULL+","+
                        SituationContract.SituationEntry.COLUMN_LATITUDE+SituattionDBUtils.TEXT_NOT_NULL+","+
                        SituationContract.SituationEntry.COLUMN_LONGITUDE+SituattionDBUtils.TEXT_NOT_NULL+","+
                        SituationContract.SituationEntry.COLUMN_HEADPHONESTATE+SituattionDBUtils.TEXT_NOT_NULL+","+
                        SituationContract.SituationEntry.COLUMN_WEATHER_STATE+SituattionDBUtils.TEXT_NOT_NULL+SituattionDBUtils.BRACKET_CLOSE+
                        ";";
                        sqLiteDatabase.execSQL(create_table_situations);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SituattionDBUtils.DROP_TABLE+"IF EXISTS "+SituationContract.SituationEntry.TABLE_SITUATIONS);
        sqLiteDatabase.execSQL(SituattionDBUtils.DELETE+"FROM"+SituattionDBUtils.SQLITE_SEQUENCE+"WHERE NAME = '"
                +SituationContract.SituationEntry.TABLE_SITUATIONS+"'");
        onCreate(sqLiteDatabase);
    }
}
