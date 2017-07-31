package com.example.limit_breaker.auto_task.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by limit-breaker on 31/7/17.
 */

public class SituationProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private SituationDBHelper mOpenHelper;
    private final static int sCode=111;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority=SituationContract.CONTENT_AUTHORITY;
        matcher.addURI(authority,SituationContract.SituationEntry.TABLE_SITUATIONS,sCode);
        return matcher;
    }
    @Override
    public boolean onCreate() {
        mOpenHelper=new SituationDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] projections,
                        @Nullable String selection,
                        @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)){
            case sCode:
                retCursor=mOpenHelper.getReadableDatabase().query(
                        SituationContract.SituationEntry.TABLE_SITUATIONS,
                        projections,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                retCursor.setNotificationUri(getContext().getContentResolver(),uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return retCursor;

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match){
            case sCode:{
                return SituationContract.SituationEntry.CONTENT_DIR_TYPE;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri;
        switch (sUriMatcher.match(uri)){
            case sCode:
                long _id= db.insert(SituationContract.SituationEntry.TABLE_SITUATIONS,
                        null,
                        contentValues);
                if(_id>0){
                    returnUri= ContentUris.withAppendedId(SituationContract.
                            SituationEntry.CONTENT_URI,_id);
                }else{
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri,
                      @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int numDeleted;
        switch(match){
            case sCode:
                numDeleted = db.delete(
                        SituationContract.SituationEntry.TABLE_SITUATIONS, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return numDeleted;
    }

    @Override
    public int update(@NonNull Uri uri,
                      @Nullable ContentValues contentValues,
                      @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int numUpdated;

        if (contentValues == null){
            throw new IllegalArgumentException("Cannot have null content values");
        }

        switch(sUriMatcher.match(uri)){
            case sCode:{
                numUpdated = db.update(SituationContract.SituationEntry.TABLE_SITUATIONS,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        if (numUpdated > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numUpdated;
    }
}
