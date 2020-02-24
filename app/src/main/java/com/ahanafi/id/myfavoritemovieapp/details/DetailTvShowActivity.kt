package com.ahanafi.id.myfavoritemovieapp.details

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ahanafi.id.myfavoritemovieapp.R
import com.ahanafi.id.myfavoritemovieapp.adapters.tvshow.TvShowFavoriteAdapter
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyTvShowColumns.Companion.CONTENT_URI
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyTvShowColumns.Companion.LANGUAGE
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyTvShowColumns.Companion.OVERVIEW
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyTvShowColumns.Companion.POSTER_PATH
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyTvShowColumns.Companion.RELEASE_DATE
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyTvShowColumns.Companion.TITLE
import com.ahanafi.id.myfavoritemovieapp.database.DatabaseContract.MyTvShowColumns.Companion._ID
import com.ahanafi.id.myfavoritemovieapp.helper.TvShowHelper
import com.ahanafi.id.myfavoritemovieapp.models.TvShow
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detail.*
import java.util.*

class DetailTvShowActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var tvShowHelper : TvShowHelper
    private lateinit var favoriteTvShowAdapter : TvShowFavoriteAdapter
    private var tvShowFavorite : TvShow? = null

    companion object{
        const val EXTRA_TV_SHOW = "extra_tv_show_data"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        favoriteTvShowAdapter =
            TvShowFavoriteAdapter()
        tvShowHelper = TvShowHelper.getInstance(applicationContext)
        tvShowHelper.open()
        tvShowFavorite = TvShow()

        val tvShow = intent.getParcelableExtra(EXTRA_TV_SHOW) as TvShow

        val tvTitle : TextView = findViewById(R.id.tv_title)
        val tvReleaseDate : TextView = findViewById(R.id.tv_release_date)
        val tvPopularity : TextView = findViewById(R.id.tv_popularity)
        val tvVoteCount : TextView = findViewById(R.id.tv_vote)
        val imgPoster : ImageView = findViewById(R.id.poster_image)
        val imgBackdrop : ImageView = findViewById(R.id.backdrop_image)
        val tvOverview : TextView = findViewById(R.id.tv_overview)

        tvTitle.text = tvShow.title
        tvPopularity.text = tvShow.popularity.toString()
        tvVoteCount.text = tvShow.voteCount.toString() + " Vote"
        tvOverview.text = tvShow.overview

        val releaseDate : String = if(Locale.getDefault().language == "in") {
            tvShow.releaseDate!!.split("-").reversed().joinToString("-")
        } else {
            tvShow.releaseDate.toString()
        }
        tvReleaseDate.text = releaseDate
        val urlPoster = tvShow.posterPath
        val urlBackdrop = tvShow.backdropPath
        Glide.with(this).load(urlBackdrop).into(imgBackdrop)
        Glide.with(this).load(urlPoster).into(imgPoster)

        title = tvShow.title

        if(tvShowHelper.checkIfExists(tvShow.id)) {
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
            R.id.btn_add_favorite -> addTvShowToFavorite()
        }
    }

    private fun addTvShowToFavorite() {
        val tvShow = intent.getParcelableExtra(EXTRA_TV_SHOW) as TvShow

        if(!tvShowHelper.checkIfExists(tvShow.id)) {
            val values = ContentValues()
            values.put(_ID, tvShow.id)
            values.put(TITLE, tvShow.title)
            values.put(OVERVIEW, tvShow.overview)
            values.put(RELEASE_DATE, tvShow.releaseDate)
            values.put(LANGUAGE, tvShow.language)
            values.put(POSTER_PATH, tvShow.posterPath)

            contentResolver.insert(CONTENT_URI, values)
            Toast.makeText(this, R.string.success_added_to_favorite, Toast.LENGTH_SHORT).show()
            favoriteTvShowAdapter.addItem(tvShow)
            showFavoriteLabel(true)
            showAddFavoriteButton(false)
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