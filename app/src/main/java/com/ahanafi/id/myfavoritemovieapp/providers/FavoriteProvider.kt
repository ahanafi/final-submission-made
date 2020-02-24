package com.ahanafi.id.myfavoritemovieapp.providers

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.AUTHORITY
import com.ahanafi.id.myfavoritemovieapp.helper.MovieHelper
import com.ahanafi.id.myfavoritemovieapp.helper.TvShowHelper
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyMovieColumns.Companion.CONTENT_URI as movieContentUri
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyMovieColumns.Companion.TABLE_NAME as movieTable
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyTvShowColumns.Companion.CONTENT_URI as tvShowContentUri
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyTvShowColumns.Companion.TABLE_NAME as tvShowTable

class FavoriteProvider : ContentProvider() {
    companion object{
        private const val MOVIE = 100
        private const val MOVIE_ID = 101

        private const val TV_SHOW = 200
        private const val TV_SHOW_ID = 201

        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        private lateinit var movieHelper: MovieHelper
        private lateinit var tvShowHelper: TvShowHelper

        init {
            sUriMatcher.addURI(AUTHORITY, movieTable, MOVIE)
            sUriMatcher.addURI(AUTHORITY, "${movieTable}/#", MOVIE_ID)
            sUriMatcher.addURI(AUTHORITY, tvShowTable, TV_SHOW)
            sUriMatcher.addURI(AUTHORITY, "${tvShowTable}/#", MOVIE_ID)
        }
    }

    override fun onCreate(): Boolean {
        movieHelper = MovieHelper.getInstance(context as Context)
        movieHelper.open()
        tvShowHelper = TvShowHelper.getInstance(context as Context)
        tvShowHelper.open()
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return when(sUriMatcher.match(uri)) {
            MOVIE -> movieHelper.queryAll()
            MOVIE_ID -> movieHelper.queryById(uri.lastPathSegment.toString())
            TV_SHOW -> tvShowHelper.queryAll()
            TV_SHOW_ID -> tvShowHelper.queryById(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        var uriString = ""
        when(sUriMatcher.match(uri)) {
            MOVIE -> {
                val added : Long = movieHelper.insert(values)
                context?.contentResolver?.notifyChange(movieContentUri, null)
                uriString = "${movieContentUri}/$added"
            }
            TV_SHOW -> {
                val added : Long = tvShowHelper.insert(values)
                context?.contentResolver?.notifyChange(tvShowContentUri, null)
                uriString = "${tvShowContentUri}/$added"
            }
        }
        return Uri.parse(uriString)
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ) : Int {

        val updated : Int = when (sUriMatcher.match(uri)) {
            MOVIE_ID -> movieHelper.update(uri.lastPathSegment.toString(), values)
            TV_SHOW_ID -> tvShowHelper.update(uri.lastPathSegment.toString(), values)
            else -> 0
        }

        when(sUriMatcher.match(uri)) {
            MOVIE_ID -> context?.contentResolver?.notifyChange(movieContentUri, null)
            TV_SHOW_ID -> context?.contentResolver?.notifyChange(tvShowContentUri, null)
        }

        return updated
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val deleted = when(sUriMatcher.match(uri)) {
            MOVIE_ID -> movieHelper.deleteById(uri.lastPathSegment.toString())
            TV_SHOW_ID -> tvShowHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }

        when(sUriMatcher.match(uri)) {
            MOVIE_ID -> context?.contentResolver?.notifyChange(movieContentUri, null)
            TV_SHOW_ID -> context?.contentResolver?.notifyChange(tvShowContentUri, null)
        }

        return deleted
    }

    override fun getType(uri: Uri): String? {
        return null
    }
}