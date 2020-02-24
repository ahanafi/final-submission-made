package com.ahanafi.id.myfavoritemovieapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ahanafi.id.myfavoritemovieapp.BuildConfig
import com.ahanafi.id.myfavoritemovieapp.models.TvShow
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class TvShowViewModel : ViewModel() {
    companion object {
        private const val API_KEY = BuildConfig.TheMovieDB_ApiKey
    }

    val listAllTvShow = MutableLiveData<ArrayList<TvShow>>()

    internal fun setTvShow() {
        val client = AsyncHttpClient()
        val listTvShow = ArrayList<TvShow>()
        val url = "https://api.themoviedb.org/3/discover/tv?api_key=$API_KEY&language=en-US"

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val listData = responseObject.getJSONArray("results")
                    for (i in 0 until listData.length()) {
                        val tvShowData = listData.getJSONObject(i)
                        val tvShow = TvShow()
                        tvShow.id = tvShowData.getInt("id")
                        tvShow.title = tvShowData.getString("name")
                        tvShow.overview = tvShowData.getString("overview")
                        tvShow.releaseDate = tvShowData.getString("first_air_date")
                        tvShow.language = tvShowData.getString("original_language")
                        val posterName = tvShowData.getString("poster_path")
                        val backdropName = tvShowData.getString("backdrop_path")
                        tvShow.posterPath = "https://image.tmdb.org/t/p/w185/$posterName"
                        tvShow.backdropPath = "https://image.tmdb.org/t/p/w342/$backdropName"
                        tvShow.voteCount = tvShowData.getInt("vote_count")
                        tvShow.voteAverage = tvShowData.getInt("vote_average")
                        tvShow.popularity = tvShowData.getInt("popularity")
                        listTvShow.add(tvShow)
                    }

                    listAllTvShow.postValue(listTvShow)
                } catch (e: Exception) {
                    Log.d("Exception: ", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d("onFailure", error?.message.toString())
            }
        })
    }

    internal fun getTvShow() : LiveData<ArrayList<TvShow>> {
        return listAllTvShow
    }
}