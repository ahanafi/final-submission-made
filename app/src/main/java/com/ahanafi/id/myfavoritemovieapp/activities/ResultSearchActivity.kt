package com.ahanafi.id.myfavoritemovieapp.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahanafi.id.myfavoritemovieapp.R
import com.ahanafi.id.myfavoritemovieapp.adapters.movie.MovieResultSearchAdapter
import com.ahanafi.id.myfavoritemovieapp.adapters.tvshow.TvShowAdapter
import com.ahanafi.id.myfavoritemovieapp.viewmodels.SearchViewModel
import kotlinx.android.synthetic.main.activity_result_search.*

class ResultSearchActivity : AppCompatActivity() {
    private lateinit var searchViewModel : SearchViewModel

    companion object{
        const val EXTRA_KEYWORD = "extra_keyword"
        const val EXTRA_TYPE = "extra_type"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_search)
        val keyword = intent.getStringExtra(EXTRA_KEYWORD)
        val title = "${resources.getString(R.string.search_result)} : $keyword"
        setTitle(title)
        loadResultData()
    }

    private fun loadResultData() {
        val keyword = intent.getStringExtra(EXTRA_KEYWORD)
        when(val dataType = intent.getStringExtra(EXTRA_TYPE)) {
            "Tv Show" -> {
                val tvShowAdapter =
                    TvShowAdapter()
                tvShowAdapter.notifyDataSetChanged()
                rv_result.layoutManager = LinearLayoutManager(this)
                searchViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(SearchViewModel::class.java)
                searchViewModel.setSearchResult(keyword, dataType)
                searchViewModel.getSearchTvShowResult().observe(this, Observer { tv_show ->
                    tvShowAdapter.setData(tv_show)
                    if(tv_show.size > 0) {
                        tv_item_not_found.visibility = View.GONE
                        progressbar.visibility = View.GONE
                    } else {
                        val text : String =  "${resources.getString(R.string.item_not_found)} with keyword : " + keyword + " in " + dataType
                        tv_item_not_found.text = text
                        progressbar.visibility = View.GONE
                        tv_item_not_found.visibility = View.VISIBLE
                    }
                })
                rv_result.adapter = tvShowAdapter
            }
            "Movie" -> {
                val movieAdapter = MovieResultSearchAdapter()
                movieAdapter.notifyDataSetChanged()
                rv_result.layoutManager = LinearLayoutManager(this)
                searchViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(SearchViewModel::class.java)
                searchViewModel.setSearchResult(keyword, dataType)
                searchViewModel.getSearchMovieResult().observe(this, Observer { movie ->
                    movieAdapter.setData(movie)
                    if(movie.size > 0) {
                        tv_item_not_found.visibility = View.GONE
                        progressbar.visibility = View.GONE
                    } else {
                        val text : String =  "${resources.getString(R.string.item_not_found)} with keyword : " + keyword + " in " + dataType
                        tv_item_not_found.text = text
                        progressbar.visibility = View.GONE
                        tv_item_not_found.visibility = View.VISIBLE
                    }
                })
                rv_result.adapter = movieAdapter
            }
        }
    }
}