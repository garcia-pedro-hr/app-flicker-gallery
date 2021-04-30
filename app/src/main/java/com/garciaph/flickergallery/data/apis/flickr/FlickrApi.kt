package com.garciaph.flickergallery.data.apis.flickr

import com.garciaph.flickergallery.FlickerGalleryApplication
import com.garciaph.flickergallery.data.apis.flickr.responses.SizeLabel
import com.garciaph.flickergallery.utils.hasNetworkConnection
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object FlickrApi {

    private const val BASE_URL = "https://www.flickr.com/services/rest/"

    private const val CACHE_SIZE: Long = 5 * 1024 * 1024  // 5MB
    private const val CACHE_ONLINE_LIFESPAN: Int = 5  // 5 seconds
    private const val CACHE_OFFLINE_LIFESPAN: Int = 60 * 60 * 24 * 7  // 7 days

    val retrofitService: FlickrApiService by lazy {
        val application = FlickerGalleryApplication.INSTANCE

        val cache = Cache(application.cacheDir, CACHE_SIZE)

        val cachedOkHttpClient = OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor { chain ->
                val request = with(chain.request()) {
                    if (application.hasNetworkConnection()) {
                        /* If there is Internet connection, get the cache that was stored
                           5 seconds ago. If the cache is older than 5 seconds, discard it. */
                        newBuilder().header(
                            "Cache-Control",
                            "public, max-age=$CACHE_ONLINE_LIFESPAN"
                        ).build()
                    } else {
                        /* If there is no Internet connection, get the cache that was stored
                           7 days ago. If the cache is older than 7 days, discard it.
                           Maybe Room could be used here to store the cached data before
                           discarding so the offline data would be shown even after 7 days.*/
                        newBuilder().header(
                            "Cache-Control",
                            "public, only-if-cached, max-stale=$CACHE_OFFLINE_LIFESPAN"
                        ).build()
                    }
                }
                chain.proceed(request)
            }
            .build()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(SizeLabel.Adapter())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(cachedOkHttpClient)
            .build()

        retrofit.create(FlickrApiService::class.java)
    }
}