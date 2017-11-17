package com.ventoray.popularmovies.data_favorites;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import static com.ventoray.popularmovies.data_favorites.FavoritesContract.FavoritesEntry.CONTENT_URI;
import static com.ventoray.popularmovies.data_favorites.FavoritesContract.FavoritesEntry.TABLE_NAME;
import static com.ventoray.popularmovies.data_favorites.FavoritesContract.PATH_FAVORITES;

/**
 * Created by nicks on 11/16/2017.
 */

public class FavoritesContentProvider extends ContentProvider {

    private FavoritesDbHelper mFavoritesDbHelper;
    public static final int URI_FAVORITES = 1000;
    public static final int URI_FAVORITE_WITH_ID = 1001;

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(FavoritesContract.AUTHORITY, PATH_FAVORITES, URI_FAVORITES);
        uriMatcher.addURI(FavoritesContract.AUTHORITY, PATH_FAVORITES + "/#", URI_FAVORITE_WITH_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mFavoritesDbHelper = new FavoritesDbHelper(context);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        final SQLiteDatabase database = mFavoritesDbHelper.getReadableDatabase();
        int match = buildUriMatcher().match(uri);

        switch (match) {
            case URI_FAVORITES:
                cursor = database.query(
                        TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
                default:
                    throw new UnsupportedOperationException("Could not match Uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Cursor cursor;
        Uri returnedUri;
        final SQLiteDatabase database = mFavoritesDbHelper.getReadableDatabase();

        long id = database.insert(TABLE_NAME, null, values);

        if (id > 0) {
            returnedUri = ContentUris.withAppendedId(CONTENT_URI, id);

        } else {
            throw new SQLException("Could not insert row into Uri: " + uri);
        }

        return returnedUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
