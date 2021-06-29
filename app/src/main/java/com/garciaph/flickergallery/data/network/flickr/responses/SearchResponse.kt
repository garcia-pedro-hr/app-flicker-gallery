package com.garciaph.flickergallery.data.network.flickr.responses

import com.squareup.moshi.Json

data class SearchResponse(
    val stat: String,
    @Json(name="photos") val data: SearchData
)

data class SearchData(
    val page: Int,
    val pages: Int,
    @Json(name="perpage") val perPage: Int,
    val total: Long,
    @Json(name="photo") val photos: List<PhotoData>
)

data class PhotoData (
    val id: Long,
    val owner: String,
    val secret: String,
    val server: Long,
    val farm: Int,
    val title: String,
    @Json(name="ispublic") val isPublic: Int,
    @Json(name="isfriend") val isFriend: Int,
    @Json(name="isfamily") val isFamily: Int,
    @Json(name="ownername") val ownerName: String
)