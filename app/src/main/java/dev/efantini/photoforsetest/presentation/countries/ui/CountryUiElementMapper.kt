package dev.efantini.photoforsetest.presentation.countries.ui

import dev.efantini.photoforsetest.domain.countries.model.Country
import javax.inject.Inject

class CountryUiElementMapper
    @Inject
    constructor() {
        fun mapFromCountry(country: Country): CountryUiElement =
            CountryUiElement(
                country.iso,
                country.name,
                country.phonePrefix,
            )
    }
