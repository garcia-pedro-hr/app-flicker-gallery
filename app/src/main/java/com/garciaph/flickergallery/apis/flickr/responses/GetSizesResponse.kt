package com.garciaph.flickergallery.apis.flickr.responses

import com.squareup.moshi.FromJson
import com.squareup.moshi.Json
import com.squareup.moshi.ToJson

data class GetSizesResponse (
    val stat: String,
    val sizes: GetSizesData
)

data class GetSizesData (
    @Json(name="canblog") val canBlog: Int,
    @Json(name="canprint") val canPrint: Int,
    @Json(name="candownload") val canDownload: Int,
    val size: List<Size>
)

data class Size (
    val label: SizeLabel,
    val width: Int,
    val height: Int,
    val source: String,
    val url: String,
    val media: String
)

enum class SizeLabel(val jsonName: String) {
    SQUARE("Square"),
    LARGE_SQUARE("Large Square"),
    THUMBNAIL("Thumbnail"),
    SMALL("Small"),
    SMALL_320("Small 320"),
    SMALL_400("Small 400"),
    MEDIUM("Medium"),
    MEDIUM_640("Medium 640"),
    MEDIUM_800("Medium 800"),
    LARGE("Large"),
    LARGE_1600("Large 1600"),
    LARGE_2048("Large 2048"),
    XLARGE_3K("X-Large 3K"),
    XLARGE_4K("X-Large 4K"),
    XLARGE_5K("X-Large 5K"),
    XLARGE_6K("X-Large 6K"),
    XLARGE_8K("X-Large 8K"),
    ORIGINAL("Original"),
    OTHER("Other");

    class Adapter() {
        @ToJson
        fun toJson(label: SizeLabel) = label.jsonName

        @FromJson
        fun fromJson(label: String) = when(label) {
            SQUARE.jsonName -> SQUARE
            LARGE_SQUARE.jsonName -> LARGE_SQUARE
            THUMBNAIL.jsonName -> THUMBNAIL
            SMALL.jsonName -> SMALL
            SMALL_320.jsonName -> SMALL_320
            SMALL_400.jsonName -> SMALL_400
            MEDIUM.jsonName -> MEDIUM
            MEDIUM_640.jsonName -> MEDIUM_640
            MEDIUM_800.jsonName -> MEDIUM_800
            LARGE.jsonName -> LARGE
            LARGE_1600.jsonName -> LARGE_1600
            LARGE_2048.jsonName -> LARGE_2048
            XLARGE_3K.jsonName -> XLARGE_3K
            XLARGE_4K.jsonName -> XLARGE_4K
            XLARGE_5K.jsonName -> XLARGE_5K
            XLARGE_6K.jsonName -> XLARGE_6K
            XLARGE_8K.jsonName -> XLARGE_8K
            ORIGINAL.jsonName -> ORIGINAL
            else -> OTHER
        }
    }

}

