package com.ahanafi.id.myfavoritemovieapp.details

import android.annotation.SuppressLint
import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ahanafi.id.myfavoritemovieapp.R
import com.ahanafi.id.myfavoritemovieapp.adapters.movie.MovieFavoriteAdapter
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyMovieColumns.Companion.CONTENT_URI
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyMovieColumns.Companion.LANGUAGE
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyMovieColumns.Companion.OVERVIEW
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyMovieColumns.Companion.POSTER_PATH
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyMovieColumns.Companion.RELEASE_DATE
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyMovieColumns.Companion.TITLE
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyMovieColumns.Companion._ID
import com.ahanafi.id.myfavoritemovieapp.helper.MovieHelper
import com.ahanafi.id.myfavoritemovieapp.models.Movie
import com.ahanafi.id.myfavoritemovieapp.widget.MyFavoriteWidget
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detail.*
import java.util.*

class DetailMovieActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var movieHelper : MovieHelper
    private lateinit var favoriteMovieAdapter : MovieFavoriteAdapter
    private lateinit var uriWithId : Uri
    private var movieFavorite : Movie? = null

    companion object{
        const val EXTRA_MOVIE_DATA = "extra_movie_data"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        favoriteMovieAdapter =
            MovieFavoriteAdapter()
        movieFavorite = Movie()

        val movie = intent.getParcelableExtra(EXTRA_MOVIE_DATA) as Movie
        uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + movie.id)

        val tvTitle : TextView = findViewById(R.id.tv_title)
        val tvReleaseDate : TextView = findViewById(R.id.tv_release_date)
        val tvPopularity : TextView = findViewById(R.id.tv_popularity)
        val tvVoteCount : TextView= findViewById(R.id.tv_vote)
        val imgPoster : ImageView = findViewById(R.id.poster_image)
        val imgBackdrop : ImageView = findViewById(R.id.backdrop_image)
        val tvOverview : TextView = findViewById(R.id.tv_overview)

        tvTitle.text = movie.title
        val releaseDate : String = if(Locale.getDefault().language == "in") {
            movie.releaseDate!!.split("-").reversed().joinToString("-")
        } else {
            movie.releaseDate.toString()
        }
        tvReleaseDate.text = releaseDate
        tvPopularity.text = movie.popularity.toString()
        tvVoteCount.text = movie.voteCount.toString() + " Vote"
        tvOverview.text = movie.overview
        val urlPoster = movie.posterPath
        val urlBackdrop = movie.backdropPath
        Glide.with(this).load(urlBackdrop).into(imgBackdrop)
        Glide.with(this).load(urlPoster).into(imgPoster)

        title = movie.title
        val data = contentResolver?.query(uriWithId, null, null, null, null)
        if(data?.moveToFirst()!!) {
            showAddFavoriteButton(false)
            showFavoriteLabel(true)
        } else {
            showAddFavoriteButton(true)
            showFavoriteLabel(false)
        }

        btn_add_favorite.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.btn_add_favorite -> addMovieToFavorite()
        }
    }

    private fun addMovieToFavorite() {
        val movie = intent.getParcelableExtra(EXTRA_MOVIE_DATA) as Movie
        val id = movie.id
        val title = movie.title
        val releaseDate = movie.releaseDate
        val overview = movie.overview
        val posterPath = movie.posterPath
        val language = movie.language

        val data = contentResolver?.query(uriWithId, null, null, null, null)
        if(! data?.moveToNext()!!) {
            val values = ContentValues()
            values.put(_ID, id)
            values.put(TITLE, title)
            values.put(RELEASE_DATE, releaseDate)
            values.put(OVERVIEW, overview)
            values.put(LANGUAGE, language)
            values.put(POSTER_PATH, posterPath)

            contentResolver.insert(CONTENT_URI, values)
            Toast.makeText(this, R.string.success_added_to_favorite, Toast.LENGTH_SHORT).show()
            favoriteMovieAdapter.addItem(movie)
            showFavoriteLabel(true)
            showAddFavoriteButton(false)

            val widget = MyFavoriteWidget
            widget.sendUpdateFavoriteMovie(this)
        } else {
            Toast.makeText(this, R.string.exist_movie_in_favorite, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showAddFavoriteButton(type: Boolean){
        if(type) {
            btn_add_favorite.visibility = View.VISIBLE
        } else {
            btn_add_favorite.visibility = View.GONE
        }
    }

    private fun showFavoriteLabel(type: Boolean) {
        if(type) {
            tv_added_favorite.visibility = View.VISIBLE
        } else {
            tv_added_favorite.visibility = View.INVISIBLE
        }
    }
}
