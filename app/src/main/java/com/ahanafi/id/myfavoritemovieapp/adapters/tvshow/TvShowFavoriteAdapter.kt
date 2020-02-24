package com.ahanafi.id.myfavoritemovieapp.adapters.tvshow

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.ahanafi.id.myfavoritemovieapp.R
import com.ahanafi.id.myfavoritemovieapp.details.DetailTvShowActivity
import com.ahanafi.id.myfavoritemovieapp.helper.TvShowHelper
import com.ahanafi.id.myfavoritemovieapp.models.TvShow
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_favorite_tv_show.view.*

class TvShowFavoriteAdapter : RecyclerView.Adapter<TvShowFavoriteAdapter.TvShowFavoriteViewHolder>() {

    private lateinit var tvShowHelper: TvShowHelper

    var listFavoriteTvShow = ArrayList<TvShow>()
    set(listFavoriteTvShow){
        if(listFavoriteTvShow.size > 0) {
            this.listFavoriteTvShow.clear()
        }
        this.listFavoriteTvShow.addAll(listFavoriteTvShow)
        notifyDataSetChanged()
    }

    inner class TvShowFavoriteViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        private val imgPoster : ImageView = itemView.findViewById(R.id.img_item_photo)
        private val tvTitle : TextView = itemView.findViewById(R.id.tv_item_name)

        internal fun bind(tvShow : TvShow) {
            Glide.with(itemView).load(tvShow.posterPath).into(imgPoster)
            tvTitle.text = tvShow.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowFavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favorite_tv_show, parent, false)
        return TvShowFavoriteViewHolder(view)
    }

    override fun getItemCount(): Int = this.listFavoriteTvShow.size

    override fun onBindViewHolder(holder: TvShowFavoriteViewHolder, position: Int) {
        val tvShow = listFavoriteTvShow[position]
        holder.bind(tvShow)
        tvShowHelper = TvShowHelper.getInstance(holder.itemView.context.applicationContext)
        tvShowHelper.open()
        holder.itemView.btn_remove_tv_show.setOnClickListener{
            val dialog = MaterialDialog(holder.itemView.context)
                .title(R.string.confirm_delete)
                .message(R.string.confirm_delete_message)
                .positiveButton(R.string.yes){
                    val result = tvShowHelper.deleteById(tvShow.id.toString()).toLong()
                    if (result > 0) {
                        removeItem(position)
                        Toast.makeText(holder.itemView.context, R.string.success_deleted_movie, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(holder.itemView.context, R.string.failed_deleted_movie, Toast.LENGTH_SHORT).show()
                    }
                }
                .negativeButton(R.string.cancel){ materialDialog ->
                    materialDialog.cancel()
                }
            dialog.show()
        }

        holder.itemView.setOnClickListener{
            val detailTvShowActivity = Intent(holder.itemView.context, DetailTvShowActivity::class.java)
            detailTvShowActivity.putExtra(DetailTvShowActivity.EXTRA_TV_SHOW, tvShow)
            holder.itemView.context.startActivity(detailTvShowActivity)
        }
    }

    private fun removeItem(position: Int) {
        this.listFavoriteTvShow.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listFavoriteTvShow.size)
    }

    fun addItem(tvShow: TvShow) {
        this.listFavoriteTvShow.add(tvShow)
        notifyItemInserted(this.listFavoriteTvShow.size-1)
    }
}