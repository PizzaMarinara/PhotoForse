package dev.efantini.photoforsetest.data.countries.remote.mapper

import dev.efantini.photoforsetest.data.countries.remote.model.CountryRemoteDTO
import dev.efantini.photoforsetest.domain.countries.model.Country
import javax.inject.Inject

class CountryAPIResultDTOMapper
    @Inject
    constructor() {
        fun mapToCountry(countryRemoteDTO: CountryRemoteDTO): Country? {
            return Country(
                countryRemoteDTO.iso ?: return null,
                countryRemoteDTO.isoAlpha2 ?: return null,
                countryRemoteDTO.isoAlpha3 ?: return null,
                countryRemoteDTO.name ?: return null,
                countryRemoteDTO.phonePrefix ?: return null,
            )
        }
    }
