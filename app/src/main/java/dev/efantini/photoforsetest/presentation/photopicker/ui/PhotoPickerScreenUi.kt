package dev.efantini.photoforsetest.presentation.photopicker.ui

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dev.efantini.photoforsetest.R
import dev.efantini.photoforsetest.presentation.navigation.UploadScreen
import dev.efantini.photoforsetest.presentation.photopicker.PhotoPickerViewModel
import dev.efantini.photoforsetest.presentation.shared.createNewFileUri
import dev.efantini.photoforsetest.presentation.shared.ui.HorizontalListItem

@Composable
fun PhotoPickerScreenUi(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: PhotoPickerViewModel = hiltViewModel<PhotoPickerViewModel>(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentContext = LocalContext.current
    val shouldShowPermsDeniedDialog = rememberSaveable { mutableStateOf(false) }
    val pickMultipleMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uris ->
            if (uris.isNotEmpty()) {
                viewModel.onPhotosSelected(uris.map { it.toString() })
            }
        }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isImageSaved ->
            viewModel.onImageSaved(isImageSaved)
        }

    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { permissionGranted ->
            if (permissionGranted) {
                val uri = createNewFileUri(currentContext)
                viewModel.onCameraLaunching(uri.toString())
                cameraLauncher.launch(uri)
            } else {
                shouldShowPermsDeniedDialog.value = true
            }
        }

    if (shouldShowPermsDeniedDialog.value) {
        PermsDeniedDialog(onDismiss = { shouldShowPermsDeniedDialog.value = false })
    }

    uiState.loadUploadScreenEvent?.consume()?.run {
        navController.navigate(UploadScreen(this))
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        PhotoPickerTitle(uiState)
        Button(onClick = {
            pickMultipleMedia
                .launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }) {
            Text(currentContext.getString(R.string.pick_image))
        }
        Button(onClick = {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }) {
            Text(currentContext.getString(R.string.take_from_camera))
        }
        Button(
            enabled = uiState.selectedPhotosList.isNotEmpty(),
            onClick = {
                viewModel.onNextButtonClicked()
            },
        ) {
            Text(currentContext.getString(R.string.upload))
        }
        LazyColumn {
            items(uiState.selectedPhotosList) { photo ->
                Row {
                    HorizontalListItem(item = photo)
                    Image(
                        modifier =
                            Modifier.size(40.dp, 40.dp).clickable {
                                viewModel.onPhotoRemoved(photo)
                            },
                        painter = painterResource(id = android.R.drawable.ic_menu_close_clear_cancel),
                        contentDescription = "",
                    )
                }
            }
        }
    }
}

@Composable
fun PhotoPickerTitle(
    uiState: PhotoPickerUiState,
    modifier: Modifier = Modifier,
) {
    Box {
        Row(modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)) {
            Text(text = uiState.countryTitle ?: LocalContext.current.getString(R.string.no_country_selected))
        }
    }
}

@Composable
fun PermsDeniedDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = LocalContext.current.getString(R.string.permission_denied)) },
        text = { Text(text = LocalContext.current.getString(R.string.permission_denied_text)) },
        confirmButton = {
            Button(
                onClick = onDismiss,
            ) {
                Text(
                    text = LocalContext.current.getString(R.string.ok),
                    color = Color.White,
                )
            }
        },
    )
}
