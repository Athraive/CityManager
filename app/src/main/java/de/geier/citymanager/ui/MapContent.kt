package de.geier.citymanager.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.*
import coil.compose.AsyncImage
import kotlin.math.min
import kotlin.math.roundToInt

@Composable
fun MapContent(
    mapImageUri: String,
    persons: List<Person>,
    pois: List<PointOfInterest>,
    containerSize: IntSize,
    scale: Float,
    panOffset: Offset,
    placementMode: Boolean,
    onTapNormalized: (Float, Float) -> Unit
) {

    var imageWidthPx by remember { mutableStateOf<Float?>(null) }
    var imageHeightPx by remember { mutableStateOf<Float?>(null) }

    val containerWidth = containerSize.width.toFloat()
    val containerHeight = containerSize.height.toFloat()

    // ===== FIT-BERECHNUNG =====

    val renderedWidth: Float
    val renderedHeight: Float
    val offsetX: Float
    val offsetY: Float

    if (
        imageWidthPx != null &&
        imageHeightPx != null &&
        containerWidth > 0f &&
        containerHeight > 0f
    ) {

        val fitScale = min(
            containerWidth / imageWidthPx!!,
            containerHeight / imageHeightPx!!
        )

        renderedWidth = imageWidthPx!! * fitScale
        renderedHeight = imageHeightPx!! * fitScale

        offsetX = (containerWidth - renderedWidth) / 2f
        offsetY = (containerHeight - renderedHeight) / 2f

    } else {
        renderedWidth = 0f
        renderedHeight = 0f
        offsetX = 0f
        offsetY = 0f
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                translationX = panOffset.x
                translationY = panOffset.y
            }
            .pointerInput(
                placementMode,
                renderedWidth,
                renderedHeight,
                offsetX,
                offsetY
            ) {
                if (placementMode) {
                    detectTapGestures { tapOffset ->

                        if (renderedWidth == 0f || renderedHeight == 0f)
                            return@detectTapGestures

                        val relativeX = tapOffset.x - offsetX
                        val relativeY = tapOffset.y - offsetY

                        val normalizedX =
                            (relativeX / renderedWidth)
                                .coerceIn(0f, 1f)

                        val normalizedY =
                            (relativeY / renderedHeight)
                                .coerceIn(0f, 1f)

                        onTapNormalized(normalizedX, normalizedY)
                    }
                }
            }
    ) {

        AsyncImage(
            model = mapImageUri,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit,
            onSuccess = { result ->
                val drawable = result.result.drawable
                imageWidthPx = drawable.intrinsicWidth.toFloat()
                imageHeightPx = drawable.intrinsicHeight.toFloat()
            }
        )

        val pinSize = (14.dp / scale)

        // ===== PERSONEN =====
        persons
            .filter { it.mapX != null && it.mapY != null }
            .forEach { person ->

                if (renderedWidth > 0f && renderedHeight > 0f) {

                    val x = offsetX +
                            (person.mapX!! * renderedWidth)

                    val y = offsetY +
                            (person.mapY!! * renderedHeight)

                    Box(
                        modifier = Modifier
                            .offset {
                                IntOffset(
                                    x.roundToInt(),
                                    y.roundToInt()
                                )
                            }
                            .size(pinSize)
                            .background(Color.Blue, CircleShape)
                            .border(1.dp, Color.White, CircleShape)
                    )
                }
            }

        // ===== POIs =====
        pois
            .filter { it.mapX != null && it.mapY != null }
            .forEach { poi ->

                if (renderedWidth > 0f && renderedHeight > 0f) {

                    val x = offsetX +
                            (poi.mapX!! * renderedWidth)

                    val y = offsetY +
                            (poi.mapY!! * renderedHeight)

                    Box(
                        modifier = Modifier
                            .offset {
                                IntOffset(
                                    x.roundToInt(),
                                    y.roundToInt()
                                )
                            }
                            .size(pinSize)
                            .background(Color.Red, CircleShape)
                            .border(1.dp, Color.White, CircleShape)
                    )
                }
            }
    }
}