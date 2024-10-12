package dev.efantini.photoforsetest.data.upload.remote.repository

import dev.efantini.photoforsetest.domain.upload.repository.UploadRepository
import dev.efantini.photoforsetest.utils.toResultFlow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class UploadRemoteRepositoryImpl(
    private val uploadAPI: UploadAPI,
) : UploadRepository {
    override fun uploadImage(byteArray: ByteArray) =
        toResultFlow {
            uploadAPI
                .upload(
                    fileToUpload =
                        MultipartBody.Part.createFormData(
                            "fileToUpload",
                            "fileToUpload.jpg",
                            byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull()),
                        ),
                )
        }
}
