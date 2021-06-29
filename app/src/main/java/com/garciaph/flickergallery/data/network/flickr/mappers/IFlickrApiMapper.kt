package com.garciaph.flickergallery.data.network.flickr.mappers

import com.garciaph.flickergallery.data.network.flickr.responses.PhotoData
import com.garciaph.flickergallery.data.network.flickr.responses.SizeData
import com.garciaph.flickergallery.domain.entities.Photo

interface IFlickrApiMapper {
    fun mapApiPhotoDataToDomain(photoData: PhotoData, sizesData: List<SizeData>): Photo
}