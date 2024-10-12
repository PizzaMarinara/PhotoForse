package dev.efantini.photoforsetest.domain.upload.usecase

import dev.efantini.photoforsetest.domain.upload.repository.UploadRepository
import javax.inject.Inject

class UploadByteArrayUseCase
    @Inject
    constructor(
        private val uploadRepository: UploadRepository,
    ) {
        operator fun invoke(byteArray: ByteArray) = uploadRepository.uploadImage(byteArray)
    }
