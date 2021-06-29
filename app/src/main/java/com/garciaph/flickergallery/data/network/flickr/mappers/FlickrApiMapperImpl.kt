package com.garciaph.flickergallery.data.network.flickr.mappers

import com.garciaph.flickergallery.data.network.flickr.responses.PhotoData
import com.garciaph.flickergallery.data.network.flickr.responses.SizeData
import com.garciaph.flickergallery.data.network.flickr.responses.SizeLabel
import com.garciaph.flickergallery.domain.entities.Photo

object FlickrApiMapperImpl: IFlickrApiMapper {
    override fun mapApiPhotoDataToDomain(photoData: PhotoData, sizesData: List<SizeData>): Photo =
        with(sizesData) {
            /* sizesData must contain LARGE_SQUARE and SQUARE sizes */
            val thumbnailUrl = sizesData.find { it.label === SizeLabel.LARGE_SQUARE }!!.source
            val fullscreenUrl = sizesData.find { it.label === SizeLabel.SQUARE }!!.source
            Photo(photoData.id, photoData.title, photoData.ownerName, thumbnailUrl, fullscreenUrl)
        }
}