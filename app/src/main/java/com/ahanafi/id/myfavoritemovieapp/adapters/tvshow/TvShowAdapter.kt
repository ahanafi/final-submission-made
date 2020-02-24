package com.ahanafi.id.myfavoritemovieapp.adapters.tvshow

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ahanafi.id.myfavoritemovieapp.R
import com.ahanafi.id.myfavoritemovieapp.details.DetailTvShowActivity
import com.ahanafi.id.myfavoritemovieapp.models.TvShow
import com.bumptech.glide.Glide
import java.util.*
import kotlin.collections.ArrayList

class TvShowAdapter : RecyclerView.Adapter<TvShowAdapter.CardViewViewHolder>() {

    private val mTvShowData = ArrayList<TvShow>()

    fun setData(tvShows : ArrayList<TvShow>) {
        mTvShowData.clear()
        mTvShowData.addAll(tvShows)
        notifyDataSetChanged()
    }

    inner class CardViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgPoster : ImageView = itemView.findViewById(R.id.img_item_photo)
        private val tvTitle : TextView = itemView.findViewById(R.id.tv_item_name)
        private val tvDescription : TextView = itemView.findViewById(R.id.tv_description)
        private val tvReleaseDate : TextView = itemView.findViewById(R.id.tv_item_release)

        internal fun bind(tvShow: TvShow) {
            Glide.with(itemView).load(tvShow.posterPath).into(imgPoster)
            tvTitle.text = tvShow.title
            tvDescription.text = tvShow.overview

            val releaseDate : String = if(Locale.getDefault().language == "in") {
                tvShow.releaseDate?.split("-")?.reversed()!!.joinToString("-")
            } else {
                tvShow.releaseDate.toString()
            }
            tvReleaseDate.text = releaseDate
        }
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        i: Int
    ): CardViewViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_tv_show, viewGroup, false)
        return CardViewViewHolder(view)
    }

    override fun getItemCount(): Int = mTvShowData.size

    override fun onBindViewHolder(holder: CardViewViewHolder, position: Int) {
        holder.bind(mTvShowData[position])
        holder.itemView.setOnClickListener{
            val tvShow = mTvShowData[position]
            val detailTvShowActivity = Intent(holder.itemView.context, DetailTvShowActivity::class.java)
            detailTvShowActivity.putExtra(DetailTvShowActivity.EXTRA_TV_SHOW, tvShow)
            holder.itemView.context.startActivity(detailTvShowActivity)
        }
    }
}