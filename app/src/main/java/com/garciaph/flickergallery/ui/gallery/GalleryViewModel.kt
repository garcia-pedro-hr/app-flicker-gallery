package com.garciaph.flickergallery.ui.gallery

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.garciaph.flickergallery.data.pagination.FlickrPagingSource
import com.garciaph.flickergallery.domain.IPhotoRepository
import com.garciaph.flickergallery.domain.entities.Photo
import timber.log.Timber

class GalleryViewModel(private val repository: IPhotoRepository) : ViewModel() {

    private var pagingDataSource: LiveData<PagingData<Photo>>? = null

    private val _photosPager = MediatorLiveData<PagingData<Photo>>()
    internal val photosPager: LiveData<PagingData<Photo>>
        get() = _photosPager

    private val _loading = MutableLiveData<Boolean>()
    internal val loading: LiveData<Boolean>
        get() = _loading

    private val loadListener = object : IPhotoRepository.IOnLoadListener {
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
        Timber.d("fetchPhotos: tags=$tags")
        pagingDataSource?.let { _photosPager.removeSource(it) }

        /* Flow<PagingData> has a handy cachedIn() method that makes the data stream shareable and
           allows you to cache the content of a Flow<PagingData> in a CoroutineScope.
           That way if you implement any transformations on the data stream, they will not be
           triggered again each time you collect the flow after Activity recreation.
           The caching should be done as close to the UI layer as possible, but not in the UI layer,
           as we want to make sure it persists beyond configuration change.
           The best place for this would be in a ViewModel, using the viewModelScope. */

        pagingDataSource = repository
            .fetchPhotosPager(tags, loadListener)
            .cachedIn(viewModelScope)
            .asLiveData()

        _photosPager.addSource(pagingDataSource!!) { _photosPager.value = it }
    }
}