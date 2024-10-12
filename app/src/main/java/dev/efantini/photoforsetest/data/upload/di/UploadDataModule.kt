package dev.efantini.photoforsetest.data.upload.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.efantini.photoforsetest.data.upload.remote.repository.UploadAPI
import dev.efantini.photoforsetest.data.upload.remote.repository.UploadRemoteRepositoryImpl
import dev.efantini.photoforsetest.domain.upload.repository.UploadRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UploadDataModule {
    @Provides
    @Singleton
    internal fun provideImageUploadAPI(
        retrofitBuilder: Retrofit.Builder,
        okHttpClientBuilder: OkHttpClient.Builder,
    ): UploadAPI =
        retrofitBuilder
            .baseUrl("https://catbox.moe")
            .client(
                okHttpClientBuilder.build(),
            ).build()
            .create(UploadAPI::class.java)

    @Provides
    fun provideUploadRepository(uploadAPI: UploadAPI): UploadRepository = UploadRemoteRepositoryImpl(uploadAPI)
}
