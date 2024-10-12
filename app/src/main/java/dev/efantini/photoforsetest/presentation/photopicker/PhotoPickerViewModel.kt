package dev.efantini.photoforsetest.presentation.photopicker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.efantini.photoforsetest.presentation.navigation.PhotoPickerScreen
import dev.efantini.photoforsetest.presentation.photopicker.ui.PhotoPickerUiState
import dev.efantini.photoforsetest.presentation.shared.ui.PhotoUiElement
import dev.efantini.photoforsetest.utils.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class PhotoPickerViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        application: Application,
    ) : AndroidViewModel(application) {
        private val _uiState =
            MutableStateFlow(
                PhotoPickerUiState(
                    countryTitle =
                        savedStateHandle
                            .toRoute<PhotoPickerScreen>()
                            .country,
                ),
            )
        val uiState = _uiState

        fun onCameraLaunching(uri: String) {
            _uiState.update {
                it.copy(
                    cachedPhotoElement = PhotoUiElement(uri),
                )
            }
        }

        fun onImageSaved(isImageSaved: Boolean) {
            val list = _uiState.value.selectedPhotosList.toMutableList()
            _uiState.value.cachedPhotoElement?.let {
                list.add(it)
            }
            if (isImageSaved) {
                _uiState.update {
                    it.copy(
                        selectedPhotosList = list.toList(),
                    )
                }
            }
        }

        fun onPhotosSelected(uris: List<String>) {
            _uiState.update {
                it.copy(
                    selectedPhotosList =
                        _uiState.value.selectedPhotosList.toMutableList().apply {
                            uris.forEach { uri ->
                                add(PhotoUiElement(uri))
                            }
                        },
                )
            }
        }

        fun onPhotoRemoved(element: PhotoUiElement) {
            val list = _uiState.value.selectedPhotosList.toMutableList()
            list.remove(element)
            _uiState.update {
                it.copy(
                    selectedPhotosList = list.toList(),
                )
            }
        }

        fun onNextButtonClicked() {
            _uiState.update {
                it.copy(
                    loadUploadScreenEvent = Event(Json.encodeToString(it.selectedPhotosList)),
                )
            }
        }
    }
