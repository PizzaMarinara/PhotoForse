package dev.efantini.photoforsetest.data.countries.remote.repository

import dev.efantini.photoforsetest.data.countries.remote.model.CountryRemoteDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface CountryAPI {
    @GET("geographics/countries/")
    @Headers("Content-Type: application/json")
    suspend fun getAllCountries(): Response<List<CountryRemoteDTO>>
}
