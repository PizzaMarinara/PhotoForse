package dev.efantini.photoforsetest

import app.cash.turbine.test
import dev.efantini.photoforsetest.domain.countries.usecase.GetAllCountriesUseCase
import dev.efantini.photoforsetest.mock.FakeCountryRepository
import dev.efantini.photoforsetest.mock.mockCountries
import dev.efantini.photoforsetest.presentation.countries.CountriesViewModel
import dev.efantini.photoforsetest.presentation.countries.ui.CountryUiElementMapper
import dev.efantini.photoforsetest.rules.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.Duration

class CountriesViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: CountriesViewModel
    private lateinit var getAllCountriesUseCase: GetAllCountriesUseCase
    private lateinit var fakeCountryRepository: FakeCountryRepository

    @Before
    fun setUp() {
        fakeCountryRepository = FakeCountryRepository()
        getAllCountriesUseCase = GetAllCountriesUseCase(fakeCountryRepository)
        viewModel = CountriesViewModel(getAllCountriesUseCase, CountryUiElementMapper())
    }

    @Test
    fun `viewmodel correctly takes all states provided by repository`() =
        runTest {
            viewModel.uiState.test(Duration.INFINITE) {
                awaitItem().also {
                    assert(it.isLoading)
                }
                awaitItem().also {
                    assert(!it.isLoading)
                    it.countries.forEachIndexed { index, country ->
                        assert(mockCountries[index].iso == country.iso)
                        assert(mockCountries[index].name == country.name)
                        assert(mockCountries[index].phonePrefix == country.prefix)
                    }
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }
}
