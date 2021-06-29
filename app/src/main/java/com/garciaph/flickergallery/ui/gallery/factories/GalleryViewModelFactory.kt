package com.garciaph.flickergallery.ui.gallery.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.garciaph.flickergallery.domain.IPhotoRepository
import com.garciaph.flickergallery.ui.gallery.GalleryViewModel

class GalleryViewModelFactory(private val repository: IPhotoRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GalleryViewModel::class.java)) {
            return GalleryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}