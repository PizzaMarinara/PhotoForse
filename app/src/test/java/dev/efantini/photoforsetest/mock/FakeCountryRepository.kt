package dev.efantini.photoforsetest.mock

import dev.efantini.photoforsetest.domain.countries.model.Country
import dev.efantini.photoforsetest.domain.countries.repository.CountryRepository
import dev.efantini.photoforsetest.utils.AsyncState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeCountryRepository : CountryRepository {
    override fun getAllCountries(): Flow<AsyncState<List<Country>>> =
        flow {
            emit(AsyncState.Loading())
            emit(AsyncState.Success(mockCountries))
        }
}
