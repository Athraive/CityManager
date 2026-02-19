package de.geier.citymanager.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.*
import coil.compose.AsyncImage
import kotlin.math.roundToInt

@Composable
fun MapContent(
    mapImageUri: String,
    persons: List<Person>,
    pois: List<PointOfInterest>,
    containerSize: IntSize,
    scale: Float,
    panOffset: Offset
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                translationX = panOffset.x
                translationY = panOffset.y
            }
    ) {

        // Karte
        AsyncImage(
            model = mapImageUri,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )

        val containerWidth = containerSize.width.toFloat()
        val containerHeight = containerSize.height.toFloat()

        // Pins sollen visuell konstant bleiben
        val pinSize = (14.dp / scale)

        // Personen
        persons.filter { it.mapX != null && it.mapY != null }
            .forEach { person ->

                val x = (person.mapX!! * containerWidth).roundToInt()
                val y = (person.mapY!! * containerHeight).roundToInt()

                Box(
                    modifier = Modifier
                        .offset { IntOffset(x, y) }
                        .size(pinSize)
                        .background(Color.Blue, CircleShape)
                        .border(1.dp, Color.White, CircleShape)
                )
            }

        // POIs
        pois.filter { it.mapX != null && it.mapY != null }
            .forEach { poi ->

                val x = (poi.mapX!! * containerWidth).roundToInt()
                val y = (poi.mapY!! * containerHeight).roundToInt()

                Box(
                    modifier = Modifier
                        .offset { IntOffset(x, y) }
                        .size(pinSize)
                        .background(Color.Red, CircleShape)
                        .border(1.dp, Color.White, CircleShape)
                )
            }
    }
}
