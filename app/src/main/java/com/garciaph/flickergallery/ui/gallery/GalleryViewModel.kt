package com.garciaph.flickergallery.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.garciaph.flickergallery.apis.flickr.pagination.FlickrPagingSource
import com.garciaph.flickergallery.domain.PhotoRepository
import com.garciaph.flickergallery.domain.entities.Photo
import timber.log.Timber

class GalleryViewModel : ViewModel() {

    private val repository = PhotoRepository()

    private var pagingDataSource: LiveData<PagingData<Photo>>? = null

    private val _photosPager = MediatorLiveData<PagingData<Photo>>()
    internal val photosPager: LiveData<PagingData<Photo>>
        get() = _photosPager

    private val _loading = MutableLiveData<Boolean>()
    internal val loading: LiveData<Boolean>
        get() = _loading

    private val loadListener = object : FlickrPagingSource.IOnLoadListener {
        override fun onLoadStarted() {
            Timber.d("onLoadStarted")
            _loading.value = true
        }

        override fun onLoadFinished() {
            Timber.d("onLoadFinished")
            _loading.value = false
        }
    }

    fun fetchPhotos(tags: String) {
        pagingDataSource?.let { _photosPager.removeSource(it) }
        pagingDataSource = repository.fetchPhotosPager(tags, loadListener).cachedIn(this)
        _photosPager.addSource(pagingDataSource!!) { _photosPager.value = it }
    }
}