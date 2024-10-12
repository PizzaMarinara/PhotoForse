package dev.efantini.photoforsetest.presentation.countries.ui

import androidx.compose.runtime.Immutable
import dev.efantini.photoforsetest.utils.Event

@Immutable
data class CountriesUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val selectedElement: CountryUiElement? = null,
    val countries: List<CountryUiElement> = emptyList(),
    val loadPhotoPickerScreenEvent: Event<String>? = null,
)

data class CountryUiElement(
    val iso: Int,
    val name: String,
    val prefix: String,
)
