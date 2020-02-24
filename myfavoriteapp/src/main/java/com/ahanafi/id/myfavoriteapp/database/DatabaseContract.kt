package com.ahanafi.id.myfavoriteapp.database

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "com.ahanafi.id.myfavoritemovieapp"
    const val SCHEME = "content"

    internal class MyMovieColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "movies"
            const val _ID = "_id"
            const val TITLE = "title"
            const val OVERVIEW = "overview"
            const val RELEASE_DATE = "release_date"
            const val LANGUAGE = "language"
            const val POSTER_PATH = "poster_path"

            val CONTENT_URI : Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }

    internal class MyTvShowColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "tv_shows"
            const val _ID = "_id"
            const val TITLE = "title"
            const val OVERVIEW = "overview"
            const val RELEASE_DATE = "release_date"
            const val LANGUAGE = "language"
            const val POSTER_PATH = "poster_path"

            val CONTENT_URI : Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}