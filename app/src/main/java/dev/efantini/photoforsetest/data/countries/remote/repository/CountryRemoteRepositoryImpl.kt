package dev.efantini.photoforsetest.data.countries.remote.repository

import dev.efantini.photoforsetest.data.countries.remote.mapper.CountryAPIResultDTOMapper
import dev.efantini.photoforsetest.domain.countries.repository.CountryRepository
import dev.efantini.photoforsetest.utils.AsyncState
import dev.efantini.photoforsetest.utils.toResultFlow
import kotlinx.coroutines.flow.map

class CountryRemoteRepositoryImpl(
    private val countryAPI: CountryAPI,
    private val countryAPIResultDTOMapper: CountryAPIResultDTOMapper,
) : CountryRepository {
    override fun getAllCountries() =
        toResultFlow { countryAPI.getAllCountries() }.map { asyncState ->
            when (asyncState) {
                is AsyncState.Loading -> asyncState
                is AsyncState.Success -> {
                    val countries = asyncState.data.mapNotNull { countryAPIResultDTOMapper.mapToCountry(it) }
                    AsyncState.Success(countries)
                }
                is AsyncState.Error -> asyncState
            }
        }
}
