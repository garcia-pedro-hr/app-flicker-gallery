package com.garciaph.flickergallery.ui.gallery.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.garciaph.flickergallery.R
import com.garciaph.flickergallery.domain.entities.Photo

class PhotosAdapter(private val clickListener: IOnItemClickListener<Photo>?) :
    PagingDataAdapter<Photo, PhotosAdapter.ViewHolder>(PhotoDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it, position, clickListener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(parent)

    class ViewHolder private constructor(view: View) : RecyclerView.ViewHolder(view) {
        private val thumbnail: ImageView = itemView.findViewById(R.id.iv_thumbnail)
        private val title: TextView = itemView.findViewById(R.id.tv_title)
        private val owner: TextView = itemView.findViewById(R.id.tv_owner)

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.rv_item_photo, parent, false)
                return ViewHolder(view)
            }
        }

        fun bind(item: Photo, position: Int, itemClickListener: IOnItemClickListener<Photo>?) {
            Glide.with(itemView).load(item.thumbnailUrl).into(thumbnail)

            title.text = item.title
            owner.text = item.owner

            itemClickListener?.let { listener ->
                itemView.setOnClickListener { listener.onItemClicked(item, position) }
            }
        }
    }

    interface IOnItemClickListener<T> {
        fun onItemClicked(obj: T, pos: Int)
    }
}