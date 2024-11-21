package com.zeltech.picsumcompose.presentation.details

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.zeltech.picsumcompose.R
import com.zeltech.picsumcompose.domain.model.Photo

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SharedTransitionScope.DetailScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    photo: Photo
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
        ) {
            AsyncImage(
                model = photo.downloadUrl,
                contentDescription = stringResource(R.string.detail_photo_by) + photo.author,
                modifier = Modifier
                    .fillMaxWidth()
                    .sharedElement(
                        state = rememberSharedContentState(key = photo.id),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = { _, _ ->
                            tween(durationMillis = 1000)
                        }
                    )
            )
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.detail_photo_details),
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = stringResource(R.string.detail_photo_id) + ": ${photo.id}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = stringResource(R.string.detail_photo_author) + ": ${photo.author}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = stringResource(R.string.detail_photo_dimensions) + ": ${photo.width}x${photo.height}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = stringResource(R.string.detail_photo_download_url) + ": ${photo.downloadUrl}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
