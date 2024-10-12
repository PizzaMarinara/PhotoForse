package dev.efantini.photoforsetest.data.shared

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.efantini.photoforsetest.BuildConfig
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val INTERCEPTOR_LOGGING_NAME = "INTERCEPTOR_LOGGING"

    @Provides
    @Named(INTERCEPTOR_LOGGING_NAME)
    fun provideHttpLoggingInterceptor(): Interceptor =
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        } else {
            noOpInterceptor()
        }

    @Provides
    fun provideOkHttpClientBuilder(
        @Named(INTERCEPTOR_LOGGING_NAME) loggingInterceptor: Interceptor,
    ): OkHttpClient.Builder =
        OkHttpClient
            .Builder()
            .apply {
                addNetworkInterceptor(loggingInterceptor)
            }

    @Provides
    @Singleton
    fun provideRetrofitBuilder(): Retrofit.Builder {
        val json =
            Json {
                ignoreUnknownKeys = true
            }
        val contentType = "application/json".toMediaType()

        return Retrofit
            .Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(json.asConverterFactory(contentType))
    }

    private fun noOpInterceptor(): Interceptor =
        Interceptor { chain ->
            chain.proceed(chain.request())
        }
}
