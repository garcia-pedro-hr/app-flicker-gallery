package com.garciaph.flickergallery.domain

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.garciaph.flickergallery.data.pagination.FlickrPagingSource
import com.garciaph.flickergallery.domain.entities.Photo

interface IPhotoRepository {
    fun fetchPhotosPager(
        tags: String,
        listener: FlickrPagingSource.IOnLoadListener
    ): LiveData<PagingData<Photo>>
}