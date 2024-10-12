package dev.efantini.photoforsetest.presentation.upload.ui

import androidx.compose.runtime.Immutable
import dev.efantini.photoforsetest.presentation.shared.ui.PhotoUiElement

@Immutable
data class UploadUiState(
    val uploadPhotoList: List<UploadPhotoUiElement> = emptyList(),
)

data class UploadPhotoUiElement(
    val element: PhotoUiElement,
    val uploadStatus: UploadStatus = UploadStatus.STARTED,
    val remoteUrl: String? = null,
)

enum class UploadStatus {
    STARTED,
    COMPLETED,
    ERROR,
}
