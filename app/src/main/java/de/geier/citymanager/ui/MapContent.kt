package de.geier.citymanager.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.*
import coil.compose.AsyncImage
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt
import androidx.compose.foundation.border
import androidx.compose.ui.Alignment

@Composable
fun MapContent(
    mapImageUri: String,
    persons: List<Person>,
    pois: List<PointOfInterest>,
    containerSize: IntSize,
    scale: Float,
    panOffset: Offset,
    placementMode: Boolean,
    moveMode: Boolean,
    onTapNormalized: (Float, Float) -> Unit,
    onMovePin: (String, Boolean, Float, Float) -> Unit,
    onMoveFinished: () -> Unit,
    onRenderedSizeCalculated: (Float, Float) -> Unit,

    selectedPersonId: String?,
    selectedPoiId: String?,
    onPersonClick: (String) -> Unit,
    onPoiClick: (String) -> Unit,
    onPersonBubbleClick: (String) -> Unit,
    onPoiBubbleClick: (String) -> Unit
) {

    var imageWidthPx by remember { mutableStateOf<Float?>(null) }
    var imageHeightPx by remember { mutableStateOf<Float?>(null) }

    val containerWidth = containerSize.width.toFloat()
    val containerHeight = containerSize.height.toFloat()

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

    LaunchedEffect(renderedWidth, renderedHeight) {
        if (renderedWidth > 0f && renderedHeight > 0f) {
            onRenderedSizeCalculated(renderedWidth, renderedHeight)
        }
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

            /* ---------------- TAPS ---------------- */

            .pointerInput(moveMode, placementMode) {

                detectTapGestures { tap ->

                    if (renderedWidth == 0f) {
                        return@detectTapGestures
                    }

                    val relX = tap.x - offsetX
                    val relY = tap.y - offsetY

                    val radius = 42f / scale

                    var hitId: String? = null
                    var hitIsPerson: Boolean? = null

                    persons.forEach {

                        if (it.mapX != null && it.mapY != null) {

                            val px = it.mapX!! * renderedWidth
                            val py = it.mapY!! * renderedHeight

                            if (
                                sqrt(
                                    (relX - px).pow(2) +
                                            (relY - py).pow(2)
                                ) <= radius
                            ) {
                                hitId = it.id
                                hitIsPerson = true
                            }
                        }
                    }

                    pois.forEach {

                        if (it.mapX != null && it.mapY != null) {

                            val px = it.mapX!! * renderedWidth
                            val py = it.mapY!! * renderedHeight

                            if (
                                sqrt(
                                    (relX - px).pow(2) +
                                            (relY - py).pow(2)
                                ) <= radius
                            ) {
                                hitId = it.id
                                hitIsPerson = false
                            }
                        }
                    }

                    if (hitId != null) {

                        if (hitIsPerson == true) {

                            if (selectedPersonId == hitId) {
                                onPersonBubbleClick(hitId)
                            } else {
                                onPersonClick(hitId)
                            }

                        } else {

                            if (selectedPoiId == hitId) {
                                onPoiBubbleClick(hitId)
                            } else {
                                onPoiClick(hitId)
                            }
                        }

                    } else if (placementMode) {

                        val normX =
                            (relX / renderedWidth)
                                .coerceIn(0f, 1f)

                        val normY =
                            (relY / renderedHeight)
                                .coerceIn(0f, 1f)

                        onTapNormalized(normX, normY)
                    }
                }
            }

            /* ---------------- DRAG ---------------- */

            .pointerInput(
                moveMode,
                selectedPersonId,
                selectedPoiId
            ) {

                val selectedId =
                    selectedPersonId ?: selectedPoiId

                val isPerson =
                    selectedPersonId != null

                if (moveMode && selectedId != null) {

                    detectDragGestures(

                        onDrag = { change, _ ->

                            val relX =
                                change.position.x - offsetX

                            val relY =
                                change.position.y - offsetY

                            val newX =
                                (relX / renderedWidth)
                                    .coerceIn(0f, 1f)

                            val newY =
                                (relY / renderedHeight)
                                    .coerceIn(0f, 1f)

                            onMovePin(
                                selectedId,
                                isPerson,
                                newX,
                                newY
                            )
                        },

                        onDragEnd = {
                            onMoveFinished()
                        }
                    )
                }
            }
    ) {

        AsyncImage(
            model = mapImageUri,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit,
            onSuccess = {
                val d = it.result.drawable
                imageWidthPx = d.intrinsicWidth.toFloat()
                imageHeightPx = d.intrinsicHeight.toFloat()
            }
        )

        val pinSize = 14.dp / scale

        if (renderedWidth > 0f && renderedHeight > 0f) {

            /* ---------------- PERSONS ---------------- */

            persons
                .filter {
                    it.mapX != null && it.mapY != null
                }
                .forEach {

                    val x =
                        offsetX + it.mapX!! * renderedWidth

                    val y =
                        offsetY + it.mapY!! * renderedHeight

                    Box(
                        modifier = Modifier
                            .offset {
                                IntOffset(
                                    x.roundToInt(),
                                    y.roundToInt()
                                )
                            }
                            .size(pinSize)
                            .background(
                                Color(0xFF1A1A1A),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                        Box(
                            modifier = Modifier
                                .size(pinSize * 0.45f)
                                .background(
                                    if (selectedPersonId == it.id)
                                        Color.White
                                    else
                                        Color(0xFF6FA8DC),
                                    CircleShape
                                )
                        )
                    }

                    if (selectedPersonId == it.id) {

                        PinLabel(
                            name = it.name,
                            x = x,
                            y = y,
                            scale = scale,
                            showLeft = x > renderedWidth * 0.75f,
                            onClick = {
                                onPersonBubbleClick(it.id)
                            }
                        )
                    }
                }

            /* ---------------- POIS ---------------- */

            pois
                .filter {
                    it.mapX != null && it.mapY != null
                }
                .forEach {

                    val x =
                        offsetX + it.mapX!! * renderedWidth

                    val y =
                        offsetY + it.mapY!! * renderedHeight

                    Box(
                        modifier = Modifier
                            .offset {
                                IntOffset(
                                    x.roundToInt(),
                                    y.roundToInt()
                                )
                            }
                            .size(pinSize)
                            .background(
                                Color(0xFF1A1A1A),
                                RoundedCornerShape(2.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                        Box(
                            modifier = Modifier
                                .size(pinSize * 0.45f)
                                .background(
                                    if (selectedPoiId == it.id)
                                        Color.White
                                    else
                                        Color(0xFFC04BFF),
                                    RoundedCornerShape(1.dp)
                                )
                        )
                    }

                    if (selectedPoiId == it.id) {

                        PinLabel(
                            name = it.name,
                            x = x,
                            y = y,
                            scale = scale,
                            showLeft = x > renderedWidth * 0.75f,
                            onClick = {
                                onPoiBubbleClick(it.id)
                            }
                        )
                    }
                }
        }

        LaunchedEffect(
            selectedPersonId,
            selectedPoiId,
            persons,
            pois
        ) {

            println("---- DEBUG MAP ----")
            println("selectedPersonId = $selectedPersonId")
            println("selectedPoiId = $selectedPoiId")

            println("Persons IDs:")
            persons.take(10).forEach {
                println("person.id=${it.id}")
            }

            println("Pois IDs:")
            pois.take(10).forEach {
                println("poi.id=${it.id}")
            }
        }
    }
}

@Composable
private fun PinLabel(
    name: String,
    x: Float,
    y: Float,
    scale: Float,
    showLeft: Boolean,
    onClick: () -> Unit
) {

    val offsetXPx = 35f / scale
    val offsetYPx = 65f / scale

    Box(
        modifier = Modifier
            .offset {
                val labelX =
                    if (showLeft)
                        x - (220f / scale)
                    else
                        x + offsetXPx

                IntOffset(
                    labelX.roundToInt(),
                    (y - offsetYPx).roundToInt()
                )
            }
            .graphicsLayer {
                scaleX = 1f / scale
                scaleY = 1f / scale
                transformOrigin = TransformOrigin(0f, 0f)
            }
    ) {

        Surface(
            onClick = onClick,
            tonalElevation = 4.dp,
            shadowElevation = 6.dp,
            shape = RoundedCornerShape(
                topStart = 8.dp,
                topEnd = 8.dp,
                bottomEnd = 8.dp,
                bottomStart = 0.dp
            )
        ) {

            Column(
                modifier = Modifier.padding(
                    horizontal = 10.dp,
                    vertical = 6.dp
                )
            ) {

                Text(
                    text = name,
                    style = MaterialTheme.typography.labelMedium
                )

                Spacer(
                    modifier = Modifier.height(2.dp)
                )

                Text(
                    text = "Tippen für Details",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}