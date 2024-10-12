package dev.efantini.photoforsetest.presentation.photopicker.ui

import androidx.compose.runtime.Immutable
import dev.efantini.photoforsetest.presentation.shared.ui.PhotoUiElement
import dev.efantini.photoforsetest.utils.Event

@Immutable
data class PhotoPickerUiState(
    val countryTitle: String? = null,
    val selectedPhotosList: List<PhotoUiElement> = emptyList(),
    val cachedPhotoElement: PhotoUiElement? = null,
    val loadUploadScreenEvent: Event<String>? = null,
)
