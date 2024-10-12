package dev.efantini.photoforsetest.domain.countries.usecase

import dev.efantini.photoforsetest.domain.countries.repository.CountryRepository
import javax.inject.Inject

class GetAllCountriesUseCase
    @Inject
    constructor(
        private val countryRepository: CountryRepository,
    ) {
        operator fun invoke() = countryRepository.getAllCountries()
    }
