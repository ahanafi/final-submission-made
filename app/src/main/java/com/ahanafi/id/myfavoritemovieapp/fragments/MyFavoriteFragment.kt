package com.ahanafi.id.myfavoritemovieapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ahanafi.id.myfavoritemovieapp.R
import com.ahanafi.id.myfavoritemovieapp.adapters.movie.MovieFavoriteAdapter
import com.ahanafi.id.myfavoritemovieapp.adapters.tvshow.TvShowFavoriteAdapter
import com.ahanafi.id.myfavoritemovieapp.helper.MappingHelper
import com.ahanafi.id.myfavoritemovieapp.helper.MovieHelper
import com.ahanafi.id.myfavoritemovieapp.helper.TvShowHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_my_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MyFavoriteFragment : Fragment() {
    private lateinit var favoriteMovieAdapter : MovieFavoriteAdapter
    private lateinit var movieHelper : MovieHelper

    private lateinit var favoriteTvShowAdapter: TvShowFavoriteAdapter
    private lateinit var tvShowHelper: TvShowHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_favorite, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_favorite_movie.setHasFixedSize(true)
        rv_favorite_tv_show.setHasFixedSize(true)

        rv_favorite_movie.layoutManager = LinearLayoutManager(this.context, RecyclerView.HORIZONTAL, isInLayout)
        rv_favorite_tv_show.layoutManager = LinearLayoutManager(this.activity, RecyclerView.HORIZONTAL, isInLayout)

        //Adapter
        favoriteMovieAdapter =
            MovieFavoriteAdapter()
        favoriteTvShowAdapter =
            TvShowFavoriteAdapter()
        rv_favorite_movie.adapter = favoriteMovieAdapter
        rv_favorite_tv_show.adapter = favoriteTvShowAdapter

        //Load Helper
        movieHelper = MovieHelper.getInstance(activity!!.applicationContext)
        movieHelper.open()
        tvShowHelper = TvShowHelper.getInstance(activity!!.applicationContext)
        tvShowHelper.open()

        loadFavoriteTvSows()
        loadFavoriteMovies()

        if (savedInstanceState == null) {
            loadFavoriteMovies()
            loadFavoriteTvSows()
        } else {
            favoriteMovieAdapter =
                MovieFavoriteAdapter()
            favoriteTvShowAdapter =
                TvShowFavoriteAdapter()
            rv_favorite_movie.adapter = favoriteMovieAdapter
            rv_favorite_tv_show.adapter = favoriteTvShowAdapter
        }
    }

    private fun loadFavoriteMovies() {
        GlobalScope.launch(Dispatchers.Main){
            progressbar_favorite_movie.visibility = View.VISIBLE
            val deferredMovie = async(Dispatchers.IO) {
                val movieCursor = movieHelper.queryAll()
                MappingHelper.mapMovieCursorToArrayList(movieCursor)
            }
            val movies = deferredMovie.await()
            progressbar_favorite_movie.visibility = View.GONE
            if(movies.size > 0) {
                favoriteMovieAdapter.listFavoriteMovies = movies
                rv_favorite_movie.adapter = favoriteMovieAdapter
            } else {
                favoriteMovieAdapter.listFavoriteMovies = ArrayList()
               Snackbar.make(rv_favorite_movie, getString(R.string.no_favorite_movie), Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadFavoriteTvSows() {
        GlobalScope.launch(Dispatchers.Main){
            progressbar_favorite_tv_show.visibility = View.VISIBLE
            val deferredTvShow = async(Dispatchers.IO) {
                val tvShowCursor = tvShowHelper.queryAll()
                MappingHelper.mapTvShowCursorToArrayList(tvShowCursor)
            }
            val tvShow = deferredTvShow.await()
            progressbar_favorite_tv_show.visibility = View.GONE
            if(tvShow.size > 0) {
                favoriteTvShowAdapter.listFavoriteTvShow = tvShow
                rv_favorite_tv_show.adapter = favoriteTvShowAdapter
            } else {
                favoriteTvShowAdapter.listFavoriteTvShow = ArrayList()
                Snackbar.make(progressbar_favorite_tv_show, getString(R.string.no_favorite_tv_show), Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}
