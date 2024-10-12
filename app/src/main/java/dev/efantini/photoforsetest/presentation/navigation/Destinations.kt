package dev.efantini.photoforsetest.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
object CountriesScreen

@Serializable
data class PhotoPickerScreen(
    val country: String,
)

@Serializable
data class UploadScreen(
    val imagePathsJson: String,
)
