package de.geier.citymanager.ui

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import de.geier.citymanager.ui.viewmodel.CityViewModel

private enum class MapMode { VIEW, EDIT }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityMapScreen(
    cityId: String,
    cityViewModel: CityViewModel,
    accessContext: AccessContext
) {

    val context = LocalContext.current
    val density = LocalDensity.current

    val lore by cityViewModel.cityLore.collectAsState()
    val persons by cityViewModel.allPersons.collectAsState()
    val pois by cityViewModel.allPois.collectAsState()

    var mapMode by remember { mutableStateOf(MapMode.VIEW) }

    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var imageSize by remember { mutableStateOf(IntSize.Zero) }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            cityViewModel.saveCityMapUri(cityId, it.toString())
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        if (lore?.mapImageUri == null) {
            Text("Keine Stadtkarte hinterlegt", Modifier.align(Alignment.Center))
        } else {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->

                            val newScale = (scale * zoom).coerceIn(1f, 5f)

                            if (newScale == 1f) {
                                scale = 1f
                                offset = Offset.Zero
                            } else {
                                scale = newScale
                                offset += pan
                            }
                        }
                    }
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        translationX = offset.x
                        translationY = offset.y
                    }
            ) {

                AsyncImage(
                    model = lore!!.mapImageUri,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .onSizeChanged { imageSize = it },
                    contentScale = ContentScale.Fit
                )

                val width = imageSize.width.toFloat()
                val height = imageSize.height.toFloat()

                if (width > 0 && height > 0) {

                    val basePinSizePx = width * 0.007f
                    var pinSizeDp = with(density) { basePinSizePx.toDp() }
                    pinSizeDp = pinSizeDp.coerceIn(4.dp, 16.dp)

                    persons.filter { it.mapX != null }.forEach { person ->

                        MapPin(
                            id = person.id,
                            initialX = person.mapX!!,
                            initialY = person.mapY!!,
                            width = width,
                            height = height,
                            scale = scale,
                            sizeDp = pinSizeDp,
                            color = Color.Blue,
                            isEditable = mapMode == MapMode.EDIT,
                            onPositionPersist = { x, y ->
                                cityViewModel.updatePersonCoordinates(person.id, x, y)
                            },
                            onTap = {
                                println("Tap Person ${person.name}")
                            },
                            onLongPress = {
                                println("LongPress Person ${person.name}")
                            }
                        )
                    }

                    pois.filter { it.mapX != null }.forEach { poi ->

                        MapPin(
                            id = poi.id,
                            initialX = poi.mapX!!,
                            initialY = poi.mapY!!,
                            width = width,
                            height = height,
                            scale = scale,
                            sizeDp = pinSizeDp,
                            color = Color.Red,
                            isEditable = mapMode == MapMode.EDIT,
                            onPositionPersist = { x, y ->
                                cityViewModel.updatePoiCoordinates(poi.id, x, y)
                            },
                            onTap = {
                                println("Tap POI ${poi.name}")
                            },
                            onLongPress = {
                                println("LongPress POI ${poi.name}")
                            }
                        )
                    }
                }
            }
        }

        var menuExpanded by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {

            IconButton(onClick = { menuExpanded = true }) {
                Icon(Icons.Default.MoreVert, null)
            }

            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false }
            ) {

                DropdownMenuItem(
                    text = {
                        Text(if (mapMode == MapMode.VIEW) "Editieren" else "Ansicht")
                    },
                    onClick = {
                        mapMode =
                            if (mapMode == MapMode.VIEW)
                                MapMode.EDIT
                            else
                                MapMode.VIEW
                        menuExpanded = false
                    }
                )

                DropdownMenuItem(
                    text = { Text("Karte ändern") },
                    onClick = {
                        menuExpanded = false
                        imagePicker.launch(arrayOf("image/*"))
                    }
                )
            }
        }
    }
}

@Composable
private fun MapPin(
    id: String,
    initialX: Float,
    initialY: Float,
    width: Float,
    height: Float,
    scale: Float,
    sizeDp: androidx.compose.ui.unit.Dp,
    color: Color,
    isEditable: Boolean,
    onPositionPersist: (Float, Float) -> Unit,
    onTap: () -> Unit,
    onLongPress: () -> Unit
) {

    var localOffset by remember(id, width, height) {
        mutableStateOf(
            Offset(
                initialX * width,
                initialY * height
            )
        )
    }

    Box(
        modifier = Modifier
            .graphicsLayer {
                translationX = localOffset.x
                translationY = localOffset.y
            }
            .size(sizeDp)
            .border(0.5.dp, Color.White, CircleShape)
            .background(color, CircleShape)
            .pointerInput(id, isEditable) {

                detectDragGestures(
                    onDragEnd = {
                        if (isEditable) {
                            val normalizedX =
                                (localOffset.x / width).coerceIn(0f, 1f)
                            val normalizedY =
                                (localOffset.y / height).coerceIn(0f, 1f)

                            onPositionPersist(normalizedX, normalizedY)
                        }
                    },
                    onDrag = { change, dragAmount ->
                        if (isEditable) {
                            change.consume()
                            localOffset += Offset(
                                dragAmount.x / scale,
                                dragAmount.y / scale
                            )
                        }
                    }
                )
            }
            .pointerInput(id) {
                detectTapGestures(
                    onTap = { onTap() },
                    onLongPress = { onLongPress() }
                )
            }
    )
}
