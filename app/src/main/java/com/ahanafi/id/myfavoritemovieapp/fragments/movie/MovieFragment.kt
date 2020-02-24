package com.ahanafi.id.myfavoritemovieapp.fragments.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahanafi.id.myfavoritemovieapp.R
import com.ahanafi.id.myfavoritemovieapp.adapters.movie.MovieAdapter
import com.ahanafi.id.myfavoritemovieapp.viewmodels.MovieViewModel
import kotlinx.android.synthetic.main.fragment_movie.*

class MovieFragment : Fragment() {
    private lateinit var movieViewModel : MovieViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_movie.setHasFixedSize(true)

        val progressbar : ProgressBar = view.findViewById(R.id.progressbar)
        progressbar.visibility = View.VISIBLE

        val moviePopularAdapter = MovieAdapter()
        moviePopularAdapter.notifyDataSetChanged()
        rv_movie.layoutManager = LinearLayoutManager(this.context)
        movieViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MovieViewModel::class.java)
        movieViewModel.setMovie()
        movieViewModel.getMovies().observe(viewLifecycleOwner, Observer { movies ->
            moviePopularAdapter.setData(movies)
            progressbar.visibility = View.GONE
        })
        rv_movie.adapter = moviePopularAdapter
    }
}