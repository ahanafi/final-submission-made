package com.ahanafi.id.myfavoriteapp.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.ahanafi.id.myfavoriteapp.R
import com.ahanafi.id.myfavoriteapp.database.DatabaseContract.MyMovieColumns.Companion.CONTENT_URI
import com.ahanafi.id.myfavoriteapp.details.DetailMovieActivity
import com.ahanafi.id.myfavoriteapp.models.Movie
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_favorite_movie.view.*

class MovieFavoriteAdapter : RecyclerView.Adapter<MovieFavoriteAdapter.MovieFavoriteViewHolder>() {
    private lateinit var uriWithId : Uri

    var listFavoriteMovies = ArrayList<Movie>()
    set(listFavoriteMovies) {
        if (listFavoriteMovies.size > 0) {
            this.listFavoriteMovies.clear()
        }
        this.listFavoriteMovies.addAll(listFavoriteMovies)
        notifyDataSetChanged()
    }

    inner class MovieFavoriteViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        private val imgPoster : ImageView = itemView.findViewById(R.id.img_item_photo)
        private val tvTitle : TextView = itemView.findViewById(R.id.tv_item_name)

        internal fun bind(movie : Movie) {
            Glide.with(itemView).load(movie.posterPath).into(imgPoster)
            tvTitle.text = movie.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieFavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favorite_movie, parent, false)
        return MovieFavoriteViewHolder(view)
    }

    override fun getItemCount(): Int = this.listFavoriteMovies.size

    override fun onBindViewHolder(holder: MovieFavoriteViewHolder, position: Int) {
        val movie  = listFavoriteMovies[position]
        holder.bind(movie)
        holder.itemView.btn_remove_movie.setOnClickListener{
           val dialog = MaterialDialog(holder.itemView.context)
               .title(R.string.confirm_delete)
               .message(R.string.confirm_delete_message)
               .positiveButton(R.string.yes){
                   val contentResolver = holder.itemView.context.contentResolver
                   uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + movie.id)
                   contentResolver.delete(uriWithId, null, null)
                   removeItem(position)
                   Toast.makeText(holder.itemView.context, R.string.success_deleted_movie, Toast.LENGTH_SHORT).show()
               }
               .negativeButton(R.string.cancel){ materialDialog ->
                   materialDialog.cancel()
               }
            dialog.show()
        }

        holder.itemView.setOnClickListener{
            val movie = listFavoriteMovies[position]
            val detailMovieActivity = Intent(holder.itemView.context, DetailMovieActivity::class.java)
            detailMovieActivity.putExtra(DetailMovieActivity.EXTRA_MOVIE_DATA, movie)
            holder.itemView.context.startActivity(detailMovieActivity)
        }
    }

    private fun removeItem(position: Int) {
        this.listFavoriteMovies.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listFavoriteMovies.size)
    }

    fun addItem(movie: Movie) {
        this.listFavoriteMovies.add(movie)
        notifyItemInserted(this.listFavoriteMovies.size-1)
    }
}