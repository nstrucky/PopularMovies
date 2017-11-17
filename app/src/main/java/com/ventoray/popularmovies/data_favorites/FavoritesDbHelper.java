package com.ventoray.popularmovies.data_favorites;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.ventoray.popularmovies.data_favorites.FavoritesContract.FavoritesEntry.COLUMN_ADULT;
import static com.ventoray.popularmovies.data_favorites.FavoritesContract.FavoritesEntry.COLUMN_BACKDROP_PATH;
import static com.ventoray.popularmovies.data_favorites.FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID;
import static com.ventoray.popularmovies.data_favorites.FavoritesContract.FavoritesEntry.COLUMN_ORIGINAL_LANGUAGE;
import static com.ventoray.popularmovies.data_favorites.FavoritesContract.FavoritesEntry.COLUMN_ORIGINAL_TITLE;
import static com.ventoray.popularmovies.data_favorites.FavoritesContract.FavoritesEntry.COLUMN_OVERVIEW;
import static com.ventoray.popularmovies.data_favorites.FavoritesContract.FavoritesEntry.COLUMN_POPULARITY;
import static com.ventoray.popularmovies.data_favorites.FavoritesContract.FavoritesEntry.COLUMN_POSTER_PATH;
import static com.ventoray.popularmovies.data_favorites.FavoritesContract.FavoritesEntry.COLUMN_RELEASE_DATE;
import static com.ventoray.popularmovies.data_favorites.FavoritesContract.FavoritesEntry.COLUMN_TITLE;
import static com.ventoray.popularmovies.data_favorites.FavoritesContract.FavoritesEntry.COLUMN_VIDEO;
import static com.ventoray.popularmovies.data_favorites.FavoritesContract.FavoritesEntry.COLUMN_VOTE_AVERAGE;
import static com.ventoray.popularmovies.data_favorites.FavoritesContract.FavoritesEntry.COLUMN_VOTE_COUNT;
import static com.ventoray.popularmovies.data_favorites.FavoritesContract.FavoritesEntry.TABLE_NAME;

/**
 * Created by nicks on 11/16/2017.
 */

public class FavoritesDbHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "favorites.db";

    public FavoritesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID                     + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MOVIE_ID         + " INTEGER," +
                COLUMN_VOTE_COUNT       + " INTEGER NOT NULL," +
                COLUMN_VOTE_AVERAGE     + " REAL NOT NULL," +
                COLUMN_TITLE            + " TEXT NOT NULL," +
                COLUMN_OVERVIEW         + " TEXT NOT NULL," +
                COLUMN_RELEASE_DATE     + " TEXT NOT NULL," +
                COLUMN_POSTER_PATH      + " TEXT NOT NULL," +
                COLUMN_ORIGINAL_LANGUAGE + " TEXT NOT NULL," +
                COLUMN_ORIGINAL_TITLE   + " TEXT NOT NULL," +
                COLUMN_BACKDROP_PATH    + " TEXT NOT NULL," +
                COLUMN_ADULT            + " BOOLEAN," +
                COLUMN_VIDEO            + " BOOLEAN," +
                COLUMN_POPULARITY       + " REAL NOT NULL" +
                ");";

        db.execSQL(CREATE_TABLE);
    }

    /**
     * Will need to edit this
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
