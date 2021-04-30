package com.garciaph.flickergallery.apis.flickr.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.garciaph.flickergallery.apis.flickr.FlickrApi
import com.garciaph.flickergallery.apis.flickr.responses.SizeLabel
import com.garciaph.flickergallery.domain.entities.Photo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

class FlickrPagingSource(
    private val tags: String,
    private val listener: IOnLoadListener
) : PagingSource<Int, Photo>() {

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? =
        state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        val position = params.key ?: 1

        return try {
            listener.onLoadStarted()

            val photos = refreshPhotos(tags, position)

            listener.onLoadFinished()

            LoadResult.Page(
                data = photos,
                prevKey = if (position == 1) null else position,
                nextKey = if (photos.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    private suspend fun refreshPhotos(tags: String, page: Int): List<Photo> {
        Timber.d("refreshPhotos: tags=$tags page=$page")
        val searchResponse = FlickrApi.retrofitService.searchPhotosAsync(tags, page)

        // Return early if Flickr photo search call failed
        if (searchResponse.stat != "ok") {
            Timber.e("Photo search failed with status: ${searchResponse.stat}")
            return listOf()
        }

        val photosList = mutableListOf<Photo>()

        val coroutineScope = CoroutineScope(Job() + Dispatchers.IO)
        val jobs = mutableListOf<Job>()

        searchResponse.photos.photo.forEach { photo ->
            jobs.add(coroutineScope.launch {
                // Launch a new job for each sizes fetch
                val getSizesResponse = FlickrApi.retrofitService.getSizesAsync(photo.id)

                // Return early if Flickr sizes fetch call failed
                if (getSizesResponse.stat != "ok") {
                    Timber.e("Photo search failed with status: ${searchResponse.stat}")
                    return@launch
                }

                var thumbnailUrl = ""
                var fullscreenUrl = ""

                getSizesResponse.sizes.size.forEach { size ->
                    if (size.label === SizeLabel.LARGE_SQUARE) thumbnailUrl = size.source
                    else if (size.label === SizeLabel.LARGE) fullscreenUrl = size.source
                }

                if (thumbnailUrl.isNotEmpty() && fullscreenUrl.isNotEmpty()) {
                    photosList.add(
                        Photo(
                            photo.id,
                            photo.title,
                            photo.ownerName,
                            thumbnailUrl,
                            fullscreenUrl
                        )
                    )
                }
            })
        }

        jobs.forEach { it.join() }

        return photosList
    }

    interface IOnLoadListener {
        fun onLoadStarted()
        fun onLoadFinished()
    }
}