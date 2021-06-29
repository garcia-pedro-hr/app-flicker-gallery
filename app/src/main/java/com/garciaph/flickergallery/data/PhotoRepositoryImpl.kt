package com.garciaph.flickergallery.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.garciaph.flickergallery.data.network.flickr.FlickrApiService
import com.garciaph.flickergallery.data.pagination.FlickrPagingSource
import com.garciaph.flickergallery.domain.IPhotoRepository
import com.garciaph.flickergallery.domain.entities.Photo
import kotlinx.coroutines.flow.Flow

class PhotoRepositoryImpl(private val apiService: FlickrApiService) : IPhotoRepository {

    private val pagingConfig = PagingConfig(
        pageSize = FlickrPagingSource.PHOTOS_PER_PAGE,
        enablePlaceholders = true
    )

    override fun fetchPhotosPager(
        tags: String,
        listener: IPhotoRepository.IOnLoadListener
    ): Flow<PagingData<Photo>> =
        Pager(config = pagingConfig) { FlickrPagingSource(apiService, tags, listener) }.flow
}