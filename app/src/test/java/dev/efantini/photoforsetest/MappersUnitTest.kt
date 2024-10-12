package dev.efantini.photoforsetest

import dev.efantini.photoforsetest.data.countries.remote.mapper.CountryAPIResultDTOMapper
import dev.efantini.photoforsetest.data.countries.remote.model.CountryRemoteDTO
import dev.efantini.photoforsetest.domain.countries.model.Country
import org.junit.Assert.assertEquals
import org.junit.Test

class MappersUnitTest {
    @Test
    fun `mapping of country remote dto is correct`() {
        val iso = 1
        val isoAlpha2 = "2"
        val isoAlpha3 = "3"
        val name = "4"
        val phonePrefix = "5"

        val test =
            CountryAPIResultDTOMapper()
                .mapToCountry(
                    CountryRemoteDTO(
                        iso = iso,
                        isoAlpha2 = isoAlpha2,
                        isoAlpha3 = isoAlpha3,
                        name = name,
                        phonePrefix = phonePrefix,
                    ),
                )
        assertEquals(test, Country(iso, isoAlpha2, isoAlpha3, name, phonePrefix))
    }

    @Test
    fun `if there is even only one null value in the DTO params, the mapper returns null`() {
        var testNull =
            CountryAPIResultDTOMapper()
                .mapToCountry(
                    CountryRemoteDTO(
                        iso = null,
                        isoAlpha2 = "2",
                        isoAlpha3 = "3",
                        name = "4",
                        phonePrefix = "5",
                    ),
                )
        assertEquals(testNull, null)
        testNull =
            CountryAPIResultDTOMapper()
                .mapToCountry(
                    CountryRemoteDTO(
                        iso = 1,
                        isoAlpha2 = null,
                        isoAlpha3 = "3",
                        name = "4",
                        phonePrefix = "5",
                    ),
                )
        assertEquals(testNull, null)
        testNull =
            CountryAPIResultDTOMapper()
                .mapToCountry(
                    CountryRemoteDTO(
                        iso = 1,
                        isoAlpha2 = "2",
                        isoAlpha3 = null,
                        name = "4",
                        phonePrefix = "5",
                    ),
                )
        assertEquals(testNull, null)
        testNull =
            CountryAPIResultDTOMapper()
                .mapToCountry(
                    CountryRemoteDTO(
                        iso = 1,
                        isoAlpha2 = "2",
                        isoAlpha3 = "3",
                        name = null,
                        phonePrefix = "5",
                    ),
                )
        assertEquals(testNull, null)
        testNull =
            CountryAPIResultDTOMapper()
                .mapToCountry(
                    CountryRemoteDTO(
                        iso = 1,
                        isoAlpha2 = "2",
                        isoAlpha3 = "3",
                        name = "4",
                        phonePrefix = null,
                    ),
                )
        assertEquals(testNull, null)
    }
}
