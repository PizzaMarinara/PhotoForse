package dev.efantini.photoforsetest.presentation.countries

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.efantini.photoforsetest.domain.countries.usecase.GetAllCountriesUseCase
import dev.efantini.photoforsetest.presentation.countries.ui.CountriesUiState
import dev.efantini.photoforsetest.presentation.countries.ui.CountryUiElement
import dev.efantini.photoforsetest.presentation.countries.ui.CountryUiElementMapper
import dev.efantini.photoforsetest.utils.AsyncState
import dev.efantini.photoforsetest.utils.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CountriesViewModel
    @Inject
    constructor(
        private val getAllCountriesUseCase: GetAllCountriesUseCase,
        private val countryUiElementMapper: CountryUiElementMapper,
    ) : ViewModel() {
        private val _uiState =
            MutableStateFlow(CountriesUiState())
        val uiState =
            _uiState
                .onStart {
                    loadData()
                }.stateIn(
                    viewModelScope,
                    SharingStarted.WhileSubscribed(),
                    CountriesUiState(),
                )

        fun onCountrySelected(country: CountryUiElement) {
            _uiState.update {
                it.copy(selectedElement = country)
            }
        }

        fun onNextButtonClicked() {
            _uiState.value.selectedElement?.let { selectedElement ->
                _uiState.update {
                    it.copy(
                        loadPhotoPickerScreenEvent = Event(selectedElement.name),
                    )
                }
            }
        }

        private fun loadData() {
            fetchAllCountries()
                .onEach { newState ->
                    _uiState.update {
                        newState
                    }
                }.launchIn(viewModelScope)
        }

        private fun fetchAllCountries() =
            getAllCountriesUseCase.invoke().map { state ->
                when (state) {
                    is AsyncState.Loading -> CountriesUiState(isLoading = true)
                    is AsyncState.Success ->
                        CountriesUiState(
                            isLoading = false,
                            countries = state.data.map { country -> countryUiElementMapper.mapFromCountry(country) },
                        )
                    is AsyncState.Error ->
                        CountriesUiState(
                            isLoading = false,
                            error = state.exception.message,
                        )
                }
            }
    }
