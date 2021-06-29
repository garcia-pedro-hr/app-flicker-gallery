package com.garciaph.flickergallery.data.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.garciaph.flickergallery.data.network.flickr.FlickrApiService
import com.garciaph.flickergallery.data.network.flickr.mappers.FlickrApiMapperImpl
import com.garciaph.flickergallery.data.network.flickr.responses.SizeData
import com.garciaph.flickergallery.data.network.flickr.responses.SizeLabel
import com.garciaph.flickergallery.domain.IPhotoRepository
import com.garciaph.flickergallery.domain.entities.Photo
import kotlinx.coroutines.*
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

/**
 * FlickrPagingSource acts here as a repository and the load function gets the data from the API.
 * In PagingSource, we take two parameters: one of integer type that represents the page number
 * and other of the data type we are loading.
 */
class FlickrPagingSource(
    private val service: FlickrApiService,
    private val tags: String,
    private val listener: IPhotoRepository.IOnLoadListener
) : PagingSource<Int, Photo>() {

    companion object {
        const val PHOTOS_PER_PAGE = 30
    }

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? =
        state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> =
        try {
            listener.onLoadStarted()

            val currentLoadingPageKey = params.key ?: 1
            val pageData = loadPageOfPhotos(currentLoadingPageKey)

            listener.onLoadFinished()

            LoadResult.Page(
                data = pageData,
                prevKey = if (currentLoadingPageKey == 1) null else currentLoadingPageKey - 1,
                nextKey = if (pageData.isEmpty()) null else currentLoadingPageKey + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }

    private suspend fun loadPageOfPhotos(pageKey: Int): List<Photo> {
        Timber.d("loadPageOfPhotos: tags=$tags pageKey=$pageKey")

        val searchResponse = service.searchPhotosAsync(tags, pageKey)
        val photosList: MutableList<Photo> = mutableListOf()

        // Return early if search call failed
        if (searchResponse.stat != "ok") {
            Timber.e("Photo search failed with status: ${searchResponse.stat}")
            return listOf()
        }

        // Asynchronously load sizes for every photo
        coroutineScope {
            searchResponse.data.photos.map { photo ->
                async(Dispatchers.IO) {
                    val sizes = loadSizesForPhoto(photo.id)

                    if (sizes.any { it.label === SizeLabel.LARGE } &&
                        sizes.any { it.label === SizeLabel.LARGE_SQUARE }) {
                        photosList.add(FlickrApiMapperImpl.mapApiPhotoDataToDomain(photo, sizes))
                    }
                }
            }.awaitAll()
        }

        return photosList
    }

    private suspend fun loadSizesForPhoto(photoId: Long): List<SizeData> {
        Timber.d("loadSizesForPhoto: photoId=$photoId")

        val response = service.getSizesAsync(photoId)

        // Return early if Flickr sizes fetch call failed
        if (response.stat != "ok") {
            Timber.e("Photo search failed with status: ${response.stat}")
            return listOf()
        }

        return response.data.sizes
    }
}