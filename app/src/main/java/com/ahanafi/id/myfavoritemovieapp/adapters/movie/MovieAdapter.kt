package com.ahanafi.id.myfavoritemovieapp.adapters.movie

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ahanafi.id.myfavoritemovieapp.R
import com.ahanafi.id.myfavoritemovieapp.details.DetailMovieActivity
import com.ahanafi.id.myfavoritemovieapp.models.Movie
import com.bumptech.glide.Glide
import java.util.*
import kotlin.collections.ArrayList

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.CardViewViewHolder>() {

    private val movieData = ArrayList<Movie>()

    fun setData(movies: ArrayList<Movie>) {
        movieData.clear()
        movieData.addAll(movies)
        notifyDataSetChanged()
    }

    inner class CardViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgPoster : ImageView = itemView.findViewById(R.id.img_item_photo)
        private val tvTitle : TextView = itemView.findViewById(R.id.tv_item_name)
        private val tvDescription : TextView = itemView.findViewById(R.id.tv_description)
        private val tvReleaseDate : TextView = itemView.findViewById(R.id.tv_item_release)

        init {
            itemView.setOnClickListener{
                val movie = movieData[adapterPosition]
                val detailMovieActivity = Intent(itemView.context, DetailMovieActivity::class.java)
                detailMovieActivity.putExtra(DetailMovieActivity.EXTRA_MOVIE_DATA, movie)
                itemView.context.startActivity(detailMovieActivity)
            }
        }

        internal fun bind(movie: Movie) {
            Glide.with(itemView).load(movie.posterPath).into(imgPoster)
            tvTitle.text = movie.title
            tvDescription.text = movie.overview

            val releaseDate : String = if(Locale.getDefault().language == "in") {
                movie.releaseDate!!.split("-").reversed().joinToString("-")
            } else {
                movie.releaseDate.toString()
            }
            tvReleaseDate.text = releaseDate
        }
    }

    override fun onCreateViewHolder(
        vGroup: ViewGroup,
        viewType: Int
    ): CardViewViewHolder {
        val view = LayoutInflater.from(vGroup.context).inflate(R.layout.item_movie, vGroup, false)
        return CardViewViewHolder(view)
    }

    override fun getItemCount(): Int = movieData.size

    override fun onBindViewHolder(holder: CardViewViewHolder, position: Int) {
        holder.bind(movieData[position])
    }

}