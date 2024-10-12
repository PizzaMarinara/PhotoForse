package dev.efantini.photoforsetest.presentation.shared

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import dev.efantini.photoforsetest.BuildConfig
import dev.efantini.photoforsetest.R
import java.io.File

fun createNewFileUri(currentContext: Context): Uri? {
    val tempFile =
        File.createTempFile(
            currentContext.getString(R.string.temp_file_prefix),
            currentContext.getString(R.string.temp_file_extension),
            currentContext.cacheDir,
        )
    return FileProvider.getUriForFile(
        currentContext,
        "${BuildConfig.APPLICATION_ID}.provider",
        tempFile,
    )
}
