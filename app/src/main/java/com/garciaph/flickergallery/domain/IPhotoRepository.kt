package com.garciaph.flickergallery.domain

import androidx.paging.PagingData
import com.garciaph.flickergallery.domain.entities.Photo
import kotlinx.coroutines.flow.Flow

interface IPhotoRepository {
    fun fetchPhotosPager(tags: String, listener: IOnLoadListener): Flow<PagingData<Photo>>

    interface IOnLoadListener {
        fun onLoadStarted()
        fun onLoadFinished()
    }
}