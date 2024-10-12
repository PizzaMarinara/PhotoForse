package dev.efantini.photoforsetest.data.countries.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.efantini.photoforsetest.BuildConfig
import dev.efantini.photoforsetest.data.countries.remote.mapper.CountryAPIResultDTOMapper
import dev.efantini.photoforsetest.data.countries.remote.repository.CountryAPI
import dev.efantini.photoforsetest.data.countries.remote.repository.CountryRemoteRepositoryImpl
import dev.efantini.photoforsetest.domain.countries.repository.CountryRepository
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CountriesDataModule {
    @Provides
    @Singleton
    internal fun provideCountryAPI(
        retrofitBuilder: Retrofit.Builder,
        okHttpClientBuilder: OkHttpClient.Builder,
    ): CountryAPI =
        retrofitBuilder
            .baseUrl(BuildConfig.COUNTRY_API_BASE_URL)
            .client(
                okHttpClientBuilder
                    .addInterceptor(
                        getInterceptorWithHeaders(
                            mapOf(
                                "x-api-key" to BuildConfig.COUNTRY_API_KEY,
                            ),
                        ),
                    ).build(),
            ).build()
            .create(CountryAPI::class.java)

    @Provides
    fun provideCountryListRepository(
        countryAPI: CountryAPI,
        countryAPIResultDTOMapper: CountryAPIResultDTOMapper,
    ): CountryRepository = CountryRemoteRepositoryImpl(countryAPI, countryAPIResultDTOMapper)

    private fun getInterceptorWithHeaders(headers: Map<String, String>) =
        Interceptor { chain ->
            val request: Request =
                chain
                    .request()
                    .newBuilder()
                    .apply {
                        headers.forEach {
                            addHeader(it.key, it.value)
                        }
                    }.build()
            chain.proceed(request)
        }
}
