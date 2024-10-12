package dev.efantini.photoforsetest.data.upload.remote.repository

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UploadAPI {
    @Multipart
    @POST("user/api.php")
    suspend fun upload(
        @Part("reqtype") reqtype: RequestBody = "fileupload".toRequestBody("text/plain".toMediaTypeOrNull()),
        @Part fileToUpload: MultipartBody.Part,
    ): Response<String>
}
