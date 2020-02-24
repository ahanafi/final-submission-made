package com.ahanafi.id.myfavoritemovieapp.helper

import android.database.Cursor
import com.ahanafi.id.myfavoritemovieapp.models.Movie
import com.ahanafi.id.myfavoritemovieapp.models.TvShow
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyMovieColumns.Companion.LANGUAGE as movieLanguage
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyMovieColumns.Companion.OVERVIEW as movieOverview
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyMovieColumns.Companion.POSTER_PATH as moviePosterPath
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyMovieColumns.Companion.RELEASE_DATE as movieReleaseDate
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyMovieColumns.Companion.TITLE as movieTitle
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyMovieColumns.Companion._ID as movieId
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyTvShowColumns.Companion.LANGUAGE as tvShowLanguage
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyTvShowColumns.Companion.OVERVIEW as tvShowOverview
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyTvShowColumns.Companion.POSTER_PATH as tvShowPosterPath
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyTvShowColumns.Companion.RELEASE_DATE as tvShowReleaseDate
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyTvShowColumns.Companion.TITLE as tvShowTitle
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyTvShowColumns.Companion._ID as tvShowId

object MappingHelper {
    fun mapMovieCursorToArrayList(movieCursor: Cursor?) : ArrayList<Movie> {
        val movieList = ArrayList<Movie>()
        movieCursor?.apply {
            while (movieCursor.moveToNext()) {
                val id = movieCursor.getInt(movieCursor.getColumnIndexOrThrow(movieId))
                val title = movieCursor.getString(movieCursor.getColumnIndexOrThrow(movieTitle))
                val overview = movieCursor.getString(movieCursor.getColumnIndexOrThrow(movieOverview))
                val releaseDate = movieCursor.getString(movieCursor.getColumnIndexOrThrow(movieReleaseDate))
                val posterPath = movieCursor.getString(movieCursor.getColumnIndexOrThrow(moviePosterPath))
                val language = movieCursor.getString(movieCursor.getColumnIndexOrThrow(movieLanguage))
                movieList.add(
                    Movie(
                        title = title,
                        posterPath = posterPath,
                        language = language,
                        releaseDate = releaseDate,
                        id = id,
                        overview = overview
                    )
                )
            }
        }
        return movieList
    }

    fun mapTvShowCursorToArrayList(tvShowCursor: Cursor?) : ArrayList<TvShow> {
        val tvShowList = ArrayList<TvShow>()
        tvShowCursor?.apply{
            while (tvShowCursor.moveToNext()) {
                val id = tvShowCursor.getInt(tvShowCursor.getColumnIndexOrThrow(tvShowId))
                val title = tvShowCursor.getString(tvShowCursor.getColumnIndexOrThrow(tvShowTitle))
                val overview = tvShowCursor.getString(tvShowCursor.getColumnIndexOrThrow(tvShowOverview))
                val releaseDate= tvShowCursor.getString(tvShowCursor.getColumnIndexOrThrow(tvShowReleaseDate))
                val posterPath= tvShowCursor.getString(tvShowCursor.getColumnIndexOrThrow(tvShowPosterPath))
                val language = tvShowCursor.getString(tvShowCursor.getColumnIndexOrThrow(tvShowLanguage))
                tvShowList.add(
                    TvShow(
                        id = id, title = title, overview = overview, releaseDate = releaseDate,
                        posterPath = posterPath, language = language
                    )
                )
            }
        }
        return tvShowList
    }
}