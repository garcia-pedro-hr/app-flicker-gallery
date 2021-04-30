package com.garciaph.flickergallery.domain

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.garciaph.flickergallery.apis.flickr.FlickrApiService
import com.garciaph.flickergallery.apis.flickr.pagination.FlickrPagingSource
import com.garciaph.flickergallery.domain.entities.Photo

class PhotoRepository {

    fun fetchPhotosPager(
        tags: String,
        listener: FlickrPagingSource.IOnLoadListener
    ): LiveData<PagingData<Photo>> =
        Pager(
            config = PagingConfig(
                pageSize = FlickrApiService.PHOTOS_PER_PAGE,
                enablePlaceholders = true
            ),
            pagingSourceFactory = { FlickrPagingSource(tags, listener) }
        ).liveData
}