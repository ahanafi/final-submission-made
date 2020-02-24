package com.ahanafi.id.myfavoritemovieapp.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyMovieColumns.Companion.TABLE_NAME as movieTable
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyMovieColumns.Companion._ID as movieId
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyMovieColumns.Companion.TITLE as movieTitle
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyMovieColumns.Companion.LANGUAGE as movieLanguage
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyMovieColumns.Companion.RELEASE_DATE as movieReleaseDate
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyMovieColumns.Companion.OVERVIEW as movieOverview
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyMovieColumns.Companion.POSTER_PATH as moviePosterPath

import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyTvShowColumns.Companion.TABLE_NAME as tvShowTable
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyTvShowColumns.Companion._ID as tvShowId
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyTvShowColumns.Companion.TITLE as tvShowTitle
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyTvShowColumns.Companion.RELEASE_DATE as tvShowReleaseDate
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyTvShowColumns.Companion.LANGUAGE as tvShowLanguage
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyTvShowColumns.Companion.OVERVIEW as tvShowOverview
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyTvShowColumns.Companion.POSTER_PATH as tvShowPosterPath

internal class DatabaseHelper(context : Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "db_favorite_movie_app"
        private const val DATABASE_VERSION = 1
        private val SQL_CREATE_MOVIE_TABLE = "CREATE TABLE IF NOT EXISTS $movieTable" +
                " (${movieId} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " $movieTitle TEXT NOT NULL," +
                " $movieReleaseDate TEXT NOT NULL," +
                " $movieLanguage TEXT NOT NULL," +
                " $movieOverview TEXT NOT NULL, " +
                " $moviePosterPath TEXT NOT NULL)"

        private val SQL_CREATE_TV_SHOW_TABLE = "CREATE TABLE IF NOT EXISTS $tvShowTable" +
                " ($tvShowId INTEGER PRIMARY KEY AUTOINCREMENT," +
                " $tvShowTitle TEXT NOT NULL," +
                " $tvShowReleaseDate TEXT NOT NULL," +
                " $tvShowLanguage TEXT NOT NULL," +
                " $tvShowOverview TEXT NOT NULL, " +
                " $tvShowPosterPath TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TV_SHOW_TABLE)
        db.execSQL(SQL_CREATE_MOVIE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $movieTable")
        db.execSQL("DROP TABLE IF EXISTS $tvShowTable")
        onCreate(db)
    }
}