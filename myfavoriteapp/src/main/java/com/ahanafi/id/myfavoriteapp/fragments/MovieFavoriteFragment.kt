package com.ahanafi.id.myfavoriteapp.fragments

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ahanafi.id.myfavoriteapp.R
import com.ahanafi.id.myfavoriteapp.adapters.MovieFavoriteAdapter
import com.ahanafi.id.myfavoriteapp.database.DatabaseContract.MyMovieColumns.Companion.CONTENT_URI
import com.ahanafi.id.myfavoriteapp.helper.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MovieFavoriteFragment : Fragment() {
    private lateinit var favoriteMovieAdapter : MovieFavoriteAdapter
    private lateinit var tvNothing : TextView
    private lateinit var rvFavoriteMovie : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvNothing = view.findViewById(R.id.tv_nothing_movie)
        rvFavoriteMovie = view.findViewById(R.id.rv_favorite_movie)

        rvFavoriteMovie.setHasFixedSize(true)
        rvFavoriteMovie.layoutManager = LinearLayoutManager(this.context, RecyclerView.HORIZONTAL, isInLayout)
        favoriteMovieAdapter = MovieFavoriteAdapter()
        rvFavoriteMovie.adapter = favoriteMovieAdapter

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler){
            override fun onChange(selfChange: Boolean) {
                loadFavoriteMovies()
            }
        }

        activity?.contentResolver?.registerContentObserver(CONTENT_URI, true, myObserver)
        loadFavoriteMovies()
    }

    private fun loadFavoriteMovies() {
        GlobalScope.launch(Dispatchers.Main){
            val deferredMovie = async(Dispatchers.IO) {
                val movieCursor = activity?.contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapMovieCursorToArrayList(movieCursor)
            }
            val movies = deferredMovie.await()
            if(movies.size > 0) {
                tvNothing.visibility = View.GONE
                favoriteMovieAdapter.listFavoriteMovies = movies
                rvFavoriteMovie.adapter = favoriteMovieAdapter
            } else {
                tvNothing.visibility = View.VISIBLE
                favoriteMovieAdapter.listFavoriteMovies = ArrayList()
            }
        }
    }


}
