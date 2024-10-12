package dev.efantini.photoforsetest.presentation.shared.ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class PhotoUiElement(
    val path: String,
) : Parcelable
