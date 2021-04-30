package com.garciaph.flickergallery.apis.flickr

import com.garciaph.flickergallery.apis.flickr.responses.GetSizesResponse
import com.garciaph.flickergallery.apis.flickr.responses.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrApiService {

    companion object {
        private const val KEY = "9a95c68a9c6ec61104cd3967dcbb8bd3"

        private const val METHOD_PHOTOS_SEARCH = "flickr.photos.search"
        private const val METHOD_PHOTOS_GET_SIZES = "flickr.photos.getSizes"

        const val PHOTOS_PER_PAGE = 30

        private const val QUERY_SEARCH_PHOTOS = "?method=$METHOD_PHOTOS_SEARCH&api_key=$KEY" +
                "&per_page=$PHOTOS_PER_PAGE&extras=owner_name&format=json&nojsoncallback=1"
        private const val QUERY_GET_SIZES = "?method=$METHOD_PHOTOS_GET_SIZES&api_key=$KEY" +
                "&format=json&nojsoncallback=1"
    }

    /**
     * Return a list of photos matching some criteria.
     * Only photos visible to the calling user will be returned.
     * To return private or semi-private photos, the caller must be authenticated with 'read'
     * permissions, and have permission to view the photos. Unauthenticated calls will only
     * return public photos.
     * @see <a href="https://www.flickr.com/services/api/flickr.photos.search.html">flickr.photos.search</a>
     */
    @GET(QUERY_SEARCH_PHOTOS)
    suspend fun searchPhotosAsync(
        @Query("tags") tags: String,
        @Query("page") page: Int
    ): SearchResponse

    /**
     * Returns the available sizes for a photo.
     * The calling user must have permission to view the photo.
     * @see <a href="https://www.flickr.com/services/api/flickr.photos.getSizes.html">flickr.photos.getSizes</a>
     */
    @GET(QUERY_GET_SIZES)
    suspend fun getSizesAsync(@Query("photo_id") photoId: Long): GetSizesResponse
}