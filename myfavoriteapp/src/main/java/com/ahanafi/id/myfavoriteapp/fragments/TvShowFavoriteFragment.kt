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
import com.ahanafi.id.myfavoriteapp.adapters.TvShowFavoriteAdapter
import com.ahanafi.id.myfavoriteapp.database.DatabaseContract.MyTvShowColumns.Companion.CONTENT_URI
import com.ahanafi.id.myfavoriteapp.helper.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TvShowFavoriteFragment : Fragment() {
    private lateinit var favoriteTvShowAdapter: TvShowFavoriteAdapter
    private lateinit var tvNothing : TextView
    private lateinit var rvFavoriteTvShow : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tv_show_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvNothing = view.findViewById(R.id.tv_nothing_tv_show)
        rvFavoriteTvShow = view.findViewById(R.id.rv_favorite_tv_show)

        rvFavoriteTvShow.setHasFixedSize(true)
        rvFavoriteTvShow.layoutManager = LinearLayoutManager(this.activity, RecyclerView.HORIZONTAL, isInLayout)
        favoriteTvShowAdapter = TvShowFavoriteAdapter()
        rvFavoriteTvShow.adapter = favoriteTvShowAdapter

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler){
            override fun onChange(selfChange: Boolean) {
                loadFavoriteTvSows()
            }
        }

        context?.contentResolver?.registerContentObserver(CONTENT_URI, true, myObserver)
        loadFavoriteTvSows()
    }

    private fun loadFavoriteTvSows() {
        GlobalScope.launch(Dispatchers.Main){
            val deferredTvShow = async(Dispatchers.IO) {
                val tvShowCursor = activity?.contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapTvShowCursorToArrayList(tvShowCursor)
            }
            val tvShow = deferredTvShow.await()
            if(tvShow.size > 0) {
                tvNothing.visibility = View.GONE
                favoriteTvShowAdapter.listFavoriteTvShow = tvShow
                rvFavoriteTvShow.adapter = favoriteTvShowAdapter
            } else {
                tvNothing.visibility = View.VISIBLE
                favoriteTvShowAdapter.listFavoriteTvShow = ArrayList()
            }
        }
    }

}
