package com.ahanafi.id.myfavoritemovieapp.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Binder
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.ahanafi.id.myfavoritemovieapp.R
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyMovieColumns.Companion.CONTENT_URI
import com.ahanafi.id.myfavoritemovieapp.helper.MappingHelper
import com.ahanafi.id.myfavoritemovieapp.models.Movie
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import java.util.*

class StackRemoteViewsFactory(private val context: Context) : RemoteViewsService.RemoteViewsFactory {
    private val favoriteMovie = ArrayList<Movie>()

    override fun onCreate() {
    }

    override fun onDataSetChanged() {
        val token = Binder.clearCallingIdentity()
        favoriteMovie.clear()
        val movieCursor = context.contentResolver.query(CONTENT_URI, null, null, null, null)
        val arrayMovies = MappingHelper.mapMovieCursorToArrayList(movieCursor)
        arrayMovies.forEach { movie ->
            favoriteMovie.add(movie)
        }
        Binder.restoreCallingIdentity(token)
    }

    override fun onDestroy() {
    }

    override fun getCount(): Int = favoriteMovie.size

    override fun getViewAt(position: Int): RemoteViews {
        val movie = favoriteMovie[position]
        val rv = RemoteViews(context.packageName, R.layout.widget_item)
        val bitmap : Bitmap = Glide.with(context)
            .asBitmap()
            .load(movie.posterPath)
            .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
            .get()

        rv.setImageViewBitmap(R.id.imageView, bitmap)

        val extras = bundleOf(
            MyFavoriteWidget.EXTRA_ITEM to movie.title
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false

}