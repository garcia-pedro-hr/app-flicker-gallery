package com.garciaph.flickergallery.ui.gallery.adapters

import androidx.recyclerview.widget.DiffUtil
import com.garciaph.flickergallery.domain.entities.Photo

internal class PhotoDiffCallback : DiffUtil.ItemCallback<Photo>() {
    override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean =
        oldItem == newItem
}