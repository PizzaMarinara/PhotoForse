package dev.efantini.photoforsetest.presentation.shared.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun HorizontalListItem(
    item: PhotoUiElement,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier =
            modifier
                .size(200.dp, 200.dp),
    ) {
        AsyncImage(
            model =
                ImageRequest
                    .Builder(LocalContext.current)
                    .data(item.path)
                    .build(),
            contentScale = ContentScale.FillBounds,
            contentDescription = null,
            modifier =
                Modifier
                    .fillMaxWidth(),
        )
    }
}
