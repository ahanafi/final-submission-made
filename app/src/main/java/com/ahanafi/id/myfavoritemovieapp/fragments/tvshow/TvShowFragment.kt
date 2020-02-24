package com.ahanafi.id.myfavoritemovieapp.fragments.tvshow

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ahanafi.id.myfavoritemovieapp.R
import com.ahanafi.id.myfavoritemovieapp.adapters.tvshow.TvShowAdapter
import com.ahanafi.id.myfavoritemovieapp.viewmodels.TvShowViewModel

class TvShowFragment : Fragment() {
    private lateinit var tvShowViewModel : TvShowViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tv_show, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rvTvShow : RecyclerView = view.findViewById(R.id.rv_tv_show)
        rvTvShow.setHasFixedSize(true)

        val progressbar : ProgressBar = view.findViewById(R.id.progressbar)
        progressbar.visibility = View.VISIBLE

        val tvShowAdapter =
            TvShowAdapter()
        tvShowAdapter.notifyDataSetChanged()
        rvTvShow.layoutManager = LinearLayoutManager(this.context)
        tvShowViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            TvShowViewModel::class.java)
        tvShowViewModel.setTvShow()
        tvShowViewModel.getTvShow().observe(viewLifecycleOwner, Observer { tvShow ->
            tvShowAdapter.setData(tvShow)
            progressbar.visibility = View.GONE
        })
        rvTvShow.adapter = tvShowAdapter
    }
}