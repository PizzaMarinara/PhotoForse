package dev.efantini.photoforsetest.domain.countries.repository

import dev.efantini.photoforsetest.domain.countries.model.Country
import dev.efantini.photoforsetest.utils.AsyncState
import kotlinx.coroutines.flow.Flow

interface CountryRepository {
    fun getAllCountries(): Flow<AsyncState<List<Country>>>
}
