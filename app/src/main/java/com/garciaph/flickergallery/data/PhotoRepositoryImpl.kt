package com.garciaph.flickergallery.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.garciaph.flickergallery.data.apis.flickr.FlickrApiService
import com.garciaph.flickergallery.data.pagination.FlickrPagingSource
import com.garciaph.flickergallery.domain.IPhotoRepository
import com.garciaph.flickergallery.domain.entities.Photo

class PhotoRepositoryImpl: IPhotoRepository {

    override fun fetchPhotosPager(
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