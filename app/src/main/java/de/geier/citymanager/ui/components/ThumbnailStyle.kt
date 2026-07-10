package de.geier.citymanager.ui.components

import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class ThumbnailStyle(
    val width: Dp,
    val height: Dp,
    val contentScale: ContentScale
) {

    Square(
        width = 72.dp,
        height = 72.dp,
        contentScale = ContentScale.Fit
    ),

    Landscape(
        width = 110.dp,
        height = 80.dp,
        contentScale = ContentScale.Crop
    )
}