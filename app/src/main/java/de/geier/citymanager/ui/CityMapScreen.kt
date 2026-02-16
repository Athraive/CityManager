package de.geier.citymanager.ui

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material3.ModalBottomSheet
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

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            cityViewModel.saveCityMapUri(
                accessContext.cityId,
                it.toString()
            )
        }
    }

    var mapMode by remember { mutableStateOf(MapMode.VIEW) }

    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var imageSize by remember { mutableStateOf(IntSize.Zero) }

    var tempPosition by remember { mutableStateOf<Offset?>(null) }
    var showSheet by remember { mutableStateOf(false) }

    // Gedämpfte Zoom-Skalierung
    val pinScale = 1f + (scale - 1f) * 0.25f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {

        when {
            lore?.mapImageUri == null -> {
                Text(
                    text = "Keine Stadtkarte hinterlegt",
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            else -> {

                val uri = lore!!.mapImageUri!!

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(mapMode) {
                            detectTransformGestures { _, pan, zoom, _ ->
                                scale = (scale * zoom).coerceIn(1f, 5f)
                                offset += pan
                            }
                        }
                        .pointerInput(mapMode, imageSize, scale, offset) {
                            if (mapMode == MapMode.EDIT) {
                                detectTapGestures { tapOffset ->
                                    if (imageSize.width == 0) return@detectTapGestures

                                    val correctedX =
                                        (tapOffset.x - offset.x) / scale
                                    val correctedY =
                                        (tapOffset.y - offset.y) / scale

                                    val normalizedX =
                                        correctedX / imageSize.width
                                    val normalizedY =
                                        correctedY / imageSize.height

                                    tempPosition =
                                        Offset(normalizedX, normalizedY)

                                    showSheet = true
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
                        model = uri,
                        contentDescription = "Stadtkarte",
                        modifier = Modifier
                            .fillMaxSize()
                            .onSizeChanged { imageSize = it },
                        contentScale = ContentScale.Fit
                    )

                    val width = imageSize.width.toFloat()
                    val height = imageSize.height.toFloat()

                    if (width > 0f && height > 0f) {

                        val basePinSizePx = width * 0.007f
                        val baseTempPinSizePx = width * 0.009f

                        var pinSizeDp =
                            with(density) { basePinSizePx.toDp() }

                        var tempPinSizeDp =
                            with(density) { baseTempPinSizePx.toDp() }

                        // Mindest- & Maximalgröße
                        pinSizeDp = pinSizeDp.coerceIn(4.dp, 18.dp)
                        tempPinSizeDp = tempPinSizeDp.coerceIn(5.dp, 22.dp)

                        persons.filter {
                            it.mapX != null && it.mapY != null
                        }.forEach { person ->

                            val x = person.mapX!! * width
                            val y = person.mapY!! * height

                            Box(
                                modifier = Modifier
                                    .graphicsLayer {
                                        translationX = x
                                        translationY = y
                                        scaleX = pinScale
                                        scaleY = pinScale
                                    }
                                    .size(pinSizeDp)
                                    .border(0.5.dp, Color.White, CircleShape)
                                    .background(Color.Blue, CircleShape)
                            )
                        }

                        pois.filter {
                            it.mapX != null && it.mapY != null
                        }.forEach { poi ->

                            val x = poi.mapX!! * width
                            val y = poi.mapY!! * height

                            Box(
                                modifier = Modifier
                                    .graphicsLayer {
                                        translationX = x
                                        translationY = y
                                        scaleX = pinScale
                                        scaleY = pinScale
                                    }
                                    .size(pinSizeDp)
                                    .border(0.5.dp, Color.White, CircleShape)
                                    .background(Color.Red, CircleShape)
                            )
                        }

                        tempPosition?.let { temp ->

                            val x = temp.x * width
                            val y = temp.y * height

                            Box(
                                modifier = Modifier
                                    .graphicsLayer {
                                        translationX = x
                                        translationY = y
                                        scaleX = pinScale
                                        scaleY = pinScale
                                    }
                                    .size(tempPinSizeDp)
                                    .border(0.5.dp, Color.White, CircleShape)
                                    .background(Color.Yellow, CircleShape)
                            )
                        }
                    }
                }
            }
        }

        if (accessContext.canEdit()) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                FloatingActionButton(
                    onClick = {
                        mapMode =
                            if (mapMode == MapMode.VIEW)
                                MapMode.EDIT
                            else
                                MapMode.VIEW
                    }
                ) {
                    Text(
                        if (mapMode == MapMode.VIEW)
                            "Edit"
                        else
                            "View"
                    )
                }

                FloatingActionButton(
                    onClick = { imagePicker.launch(arrayOf("image/*")) }
                ) {
                    Text("+")
                }
            }
        }
    }

    if (showSheet && tempPosition != null) {

        ModalBottomSheet(
            onDismissRequest = {
                showSheet = false
                tempPosition = null
            }
        ) {

            val freePersons =
                persons.filter { it.mapX == null }

            val freePois =
                pois.filter { it.mapX == null }

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Text("Position zuweisen")

                freePersons.forEach { person ->
                    Button(
                        onClick = {
                            val pos = tempPosition!!
                            cityViewModel.updatePersonCoordinates(
                                person.id,
                                pos.x,
                                pos.y
                            )
                            showSheet = false
                            tempPosition = null
                        }
                    ) {
                        Text("Person: ${person.name}")
                    }
                }

                freePois.forEach { poi ->
                    Button(
                        onClick = {
                            val pos = tempPosition!!
                            cityViewModel.updatePoiCoordinates(
                                poi.id,
                                pos.x,
                                pos.y
                            )
                            showSheet = false
                            tempPosition = null
                        }
                    ) {
                        Text("POI: ${poi.name}")
                    }
                }

                if (freePersons.isEmpty() && freePois.isEmpty()) {
                    Text("Keine freien Einträge verfügbar.")
                }
            }
        }
    }
}
