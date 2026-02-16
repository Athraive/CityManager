package de.geier.citymanager.ui.map

import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

data class MapItem(
    val id: String,
    val x: Float,
    val y: Float,
    val isPerson: Boolean
)

@Composable
fun CityMapComposable(
    imageUri: String,
    items: List<MapItem>
) {
    val zoomState = remember { ZoomState() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    zoomState.scale =
                        (zoomState.scale * zoom).coerceIn(1f, 5f)

                    zoomState.offset += pan
                }
            }
            .graphicsLayer {
                scaleX = zoomState.scale
                scaleY = zoomState.scale
                translationX = zoomState.offset.x
                translationY = zoomState.offset.y
            }
    ) {
        AsyncImage(
            model = imageUri,
            contentDescription = "Stadtkarte",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )

        items.forEach { item ->
            MapPin(
                color = if (item.isPerson)
                    androidx.compose.ui.graphics.Color.Blue
                else
                    androidx.compose.ui.graphics.Color.Red,
                modifier = Modifier.graphicsLayer {
                    translationX = item.x
                    translationY = item.y
                }
            )
        }
    }
}
