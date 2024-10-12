package dev.efantini.photoforsetest.presentation.upload

import android.app.Application
import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.efantini.photoforsetest.domain.upload.usecase.UploadByteArrayUseCase
import dev.efantini.photoforsetest.presentation.navigation.UploadScreen
import dev.efantini.photoforsetest.presentation.shared.ui.PhotoUiElement
import dev.efantini.photoforsetest.presentation.upload.ui.UploadPhotoUiElement
import dev.efantini.photoforsetest.presentation.upload.ui.UploadStatus
import dev.efantini.photoforsetest.presentation.upload.ui.UploadUiState
import dev.efantini.photoforsetest.utils.AsyncState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class UploadViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        application: Application,
        private val uploadByteArrayUseCase: UploadByteArrayUseCase,
    ) : AndroidViewModel(application) {
        private val context: Context get() = getApplication<Application>().applicationContext
        private lateinit var job: Job
        private val _uiState =
            MutableStateFlow(UploadUiState())
        val uiState =
            _uiState.onStart { loadData(savedStateHandle) }.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(),
                UploadUiState(),
            )

        fun stopUpload() {
            job.cancel()
            _uiState.update {
                it.copy(
                    uploadPhotoList =
                        it.uploadPhotoList.map { element ->
                            if (element.uploadStatus != UploadStatus.COMPLETED) {
                                element.copy(
                                    uploadStatus = UploadStatus.ERROR,
                                )
                            } else {
                                element
                            }
                        },
                )
            }
        }

        private fun loadData(savedStateHandle: SavedStateHandle) {
            val listToUpload =
                Json
                    .decodeFromString<List<PhotoUiElement>>(
                        savedStateHandle.toRoute<UploadScreen>().imagePathsJson,
                    ).map {
                        UploadPhotoUiElement(it)
                    }
            _uiState.update {
                it.copy(
                    uploadPhotoList = listToUpload,
                )
            }
            job =
                startUploadJobs()
                    .map { pairElement ->
                        _uiState.value.copy(
                            uploadPhotoList =
                                _uiState.value.uploadPhotoList
                                    .toMutableList()
                                    .apply {
                                        set(
                                            pairElement.first,
                                            get(pairElement.first)
                                                .copy(
                                                    uploadStatus =
                                                        when (pairElement.second) {
                                                            is AsyncState.Loading -> UploadStatus.STARTED
                                                            is AsyncState.Success -> UploadStatus.COMPLETED
                                                            is AsyncState.Error -> UploadStatus.ERROR
                                                        },
                                                    remoteUrl =
                                                        if (pairElement.second is AsyncState.Success) {
                                                            (pairElement.second as AsyncState.Success<String>).data
                                                        } else {
                                                            null
                                                        },
                                                ),
                                        )
                                    },
                        )
                    }.onEach { newState ->
                        _uiState.update { newState }
                    }.launchIn(viewModelScope)
        }

        private fun mapUploadElementToFlow(
            index: Int,
            element: UploadPhotoUiElement,
        ): Flow<Pair<Int, AsyncState<String>>>? {
            val inputStream =
                context
                    .contentResolver
                    .openInputStream(element.element.path.toUri())
            return inputStream?.let {
                uploadByteArrayUseCase(inputStream.readBytes()).map {
                    Pair(index, it)
                }
            }
        }

        private fun startUploadJobs(): Flow<Pair<Int, AsyncState<String>>> {
            val listFlows =
                _uiState.value.uploadPhotoList.mapIndexedNotNull { index, element ->
                    mapUploadElementToFlow(index, element)
                }
            return listFlows.merge()
        }
    }
