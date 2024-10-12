package dev.efantini.photoforsetest.presentation.upload.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.efantini.photoforsetest.R
import dev.efantini.photoforsetest.presentation.shared.ui.HorizontalListItem
import dev.efantini.photoforsetest.presentation.upload.UploadViewModel

@Composable
fun UploadScreenUi(
    modifier: Modifier = Modifier,
    viewModel: UploadViewModel = hiltViewModel<UploadViewModel>(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    Column(
        modifier = Modifier.fillMaxSize().padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Photos uploaded: ${
                uiState.uploadPhotoList
                    .filter { it.uploadStatus == UploadStatus.COMPLETED }
                    .size
            }",
            modifier = Modifier.padding(bottom = 10.dp),
        )
        Button(onClick = {
            viewModel.stopUpload()
        }) {
            Text(LocalContext.current.getString(R.string.stop_upload))
        }
        LazyColumn {
            items(uiState.uploadPhotoList) { photo ->
                Column {
                    Box {
                        HorizontalListItem(
                            modifier =
                                if (photo.uploadStatus == UploadStatus.STARTED
                                ) {
                                    Modifier.alpha(0.2f)
                                } else {
                                    Modifier
                                },
                            item = photo.element,
                        )
                        if (photo.uploadStatus == UploadStatus.STARTED) {
                            CircularProgressIndicator(
                                modifier = Modifier.width(100.dp).align(Alignment.Center),
                            )
                        }
                        if (photo.uploadStatus == UploadStatus.ERROR) {
                            Box(modifier = Modifier.matchParentSize().alpha(0.4f).background(Color.Black))
                        }
                    }
                    if (photo.uploadStatus == UploadStatus.COMPLETED && photo.remoteUrl != null) {
                        Row {
                            TextField(
                                value = photo.remoteUrl,
                                enabled = false,
                                onValueChange = {},
                            )
                            Image(
                                modifier =
                                    Modifier.size(40.dp, 40.dp).clickable {
                                        clipboardManager.setText(AnnotatedString((photo.remoteUrl)))
                                    },
                                painter = painterResource(id = android.R.drawable.ic_menu_share),
                                contentDescription = "",
                            )
                        }
                    } else if (photo.uploadStatus == UploadStatus.ERROR) {
                        Text(text = LocalContext.current.getString(R.string.upload_failed))
                    }
                }
            }
        }
    }
}
