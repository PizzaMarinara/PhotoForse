package dev.efantini.photoforsetest.mock

import dev.efantini.photoforsetest.domain.countries.model.Country

val mockCountries =
    listOf(
        Country(1, "US", "en-us", "United States", "+1"),
        Country(2, "GB", "en-gb", "United Kingdom", "+44"),
    )
