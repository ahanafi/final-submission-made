package com.ahanafi.id.myfavoritemovieapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ahanafi.id.myfavoritemovieapp.BuildConfig
import com.ahanafi.id.myfavoritemovieapp.models.Movie
import com.ahanafi.id.myfavoritemovieapp.models.TvShow
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class SearchViewModel : ViewModel() {
    companion object{
        private const val API_KEY = BuildConfig.TheMovieDB_ApiKey
    }

    val listTvShowResult = MutableLiveData<ArrayList<TvShow>>()
    var listMovieResult = MutableLiveData<ArrayList<Movie>>()

    internal fun setSearchResult(keyword : String?, type: String?) {
        val client = AsyncHttpClient()
        val listTvShow = ArrayList<TvShow>()
        val listMovie = ArrayList<Movie>()
        when(type) {
            "Tv Show" -> {
                val url = "https://api.themoviedb.org/3/search/tv?api_key=$API_KEY&language=en-US&query=$keyword"

                client.get(url, object : AsyncHttpResponseHandler() {
                    override fun onSuccess(
                        statusCode: Int,
                        headers: Array<out Header>?,
                        responseBody: ByteArray
                    ) {
                        try {
                            val resultSearch = String(responseBody)
                            val responseObject = JSONObject(resultSearch)
                            val responseData = responseObject.getJSONArray("results")
                            for (i in 0 until responseData.length()) {
                                val resultData = responseData.getJSONObject(i)
                                val tvShow = TvShow()
                                tvShow.id = resultData.getInt("id")
                                tvShow.title = resultData.getString("name")
                                tvShow.overview = resultData.getString("overview")
                                tvShow.releaseDate = resultData.getString("first_air_date")
                                tvShow.language = resultData.getString("original_language")
                                val posterName = resultData.getString("poster_path")
                                val backdropName = resultData.getString("backdrop_path")
                                tvShow.posterPath = "https://image.tmdb.org/t/p/w185/$posterName"
                                tvShow.backdropPath = "https://image.tmdb.org/t/p/w342/$backdropName"
                                tvShow.voteCount = resultData.getInt("vote_count")
                                tvShow.voteAverage = resultData.getInt("vote_average")
                                tvShow.popularity = resultData.getInt("popularity")
                                listTvShow.add(tvShow)
                            }
                            listTvShowResult.postValue(listTvShow)
                        } catch (e: Exception) {
                            Log.d("Exception: ", e.message.toString())
                        }
                    }

                    override fun onFailure(
                        statusCode: Int,
                        headers: Array<out Header>?,
                        responseBody: ByteArray?,
                        error: Throwable
                    ) {
                        Log.d("onFailure", error.message.toString())
                    }

                })
            }
            "Movie" -> {
                val url = "https://api.themoviedb.org/3/search/movie?api_key=$API_KEY&language=en-US&query=$keyword"

                client.get(url, object : AsyncHttpResponseHandler() {
                    override fun onSuccess(
                        statusCode: Int,
                        headers: Array<out Header>?,
                        responseBody: ByteArray
                    ) {
                        try {
                            val resultSearch = String(responseBody)
                            val responseObject = JSONObject(resultSearch)
                            val responseData = responseObject.getJSONArray("results")
                            for (i in 0 until responseData.length()) {
                                val resultData = responseData.getJSONObject(i)
                                val movie = Movie()
                                movie.id = resultData.getInt("id")
                                movie.title = resultData.getString("title")
                                movie.overview = resultData.getString("overview")
                                movie.releaseDate = resultData.getString("release_date")
                                movie.language = resultData.getString("original_language")
                                val posterName = resultData.getString("poster_path")
                                val backdropName = resultData.getString("backdrop_path")
                                movie.posterPath = "https://image.tmdb.org/t/p/w185/$posterName"
                                movie.backdropPath = "https://image.tmdb.org/t/p/w342/$backdropName"
                                movie.voteCount = resultData.getInt("vote_count")
                                movie.voteAverage = resultData.getInt("vote_average")
                                movie.popularity = resultData.getInt("popularity")
                                listMovie.add(movie)
                            }
                            listMovieResult.postValue(listMovie)
                        } catch (e: Exception) {
                            Log.d("Exception: ", e.message.toString())
                        }
                    }

                    override fun onFailure(
                        statusCode: Int,
                        headers: Array<out Header>?,
                        responseBody: ByteArray?,
                        error: Throwable
                    ) {
                        Log.d("onFailure", error.message.toString())
                    }

                })
            }
        }
    }

    internal fun getSearchMovieResult() : LiveData<ArrayList<Movie>>{
        return listMovieResult
    }

    internal fun getSearchTvShowResult() : LiveData<ArrayList<TvShow>>{
        return listTvShowResult
    }


}