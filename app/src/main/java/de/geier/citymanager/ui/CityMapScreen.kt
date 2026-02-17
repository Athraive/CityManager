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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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

private sealed class SelectableEntity {
    data class Person(val id: String, val name: String) : SelectableEntity()
    data class Poi(val id: String, val name: String) : SelectableEntity()
}

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

    var selectedEntity by remember { mutableStateOf<SelectableEntity?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var showPersons by remember { mutableStateOf(true) }
    var showPois by remember { mutableStateOf(true) }

    val pinScale = 1f + (scale - 1f) * 0.25f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {

        /* ============================================================
           1️⃣  MAP LAYER (transformiert)
        ============================================================ */

        if (lore?.mapImageUri != null) {

            val uri = lore!!.mapImageUri!!

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->

                            val newScale = (scale * zoom).coerceIn(1f, 5f)

                            if (newScale > 1f) {
                                offset += pan
                            } else {
                                offset = Offset.Zero
                            }

                            scale = newScale
                        }
                    }
                    .pointerInput(mapMode, selectedEntity, imageSize, scale, offset) {

                        if (mapMode == MapMode.EDIT && selectedEntity != null) {

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

                                val entity =
                                    selectedEntity ?: return@detectTapGestures

                                when (entity) {

                                    is SelectableEntity.Person ->
                                        cityViewModel.updatePersonCoordinates(
                                            entity.id,
                                            normalizedX,
                                            normalizedY
                                        )

                                    is SelectableEntity.Poi ->
                                        cityViewModel.updatePoiCoordinates(
                                            entity.id,
                                            normalizedX,
                                            normalizedY
                                        )
                                }

                                selectedEntity = null
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
                    var pinSizeDp =
                        with(density) { basePinSizePx.toDp() }
                    pinSizeDp = pinSizeDp.coerceIn(4.dp, 18.dp)

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
                }
            }

        } else {
            Text(
                text = "Keine Stadtkarte hinterlegt",
                modifier = Modifier.align(Alignment.Center)
            )
        }

        /* ============================================================
           2️⃣  EDIT PANEL (NICHT transformiert!)
        ============================================================ */

        if (mapMode == MapMode.EDIT && accessContext.canEdit()) {

            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(8.dp)
            ) {

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Suchen...") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = showPersons,
                        onClick = { showPersons = !showPersons },
                        label = { Text("Personen") }
                    )
                    FilterChip(
                        selected = showPois,
                        onClick = { showPois = !showPois },
                        label = { Text("POIs") }
                    )
                }

                val freePersons =
                    persons.filter {
                        it.mapX == null &&
                                it.name.contains(searchQuery, true)
                    }

                val freePois =
                    pois.filter {
                        it.mapX == null &&
                                it.name.contains(searchQuery, true)
                    }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 200.dp)
                ) {

                    if (showPersons) {
                        items(freePersons) { person ->
                            TextButton(
                                onClick = {
                                    selectedEntity =
                                        SelectableEntity.Person(
                                            person.id,
                                            person.name
                                        )
                                }
                            ) {
                                Text("Person: ${person.name}")
                            }
                        }
                    }

                    if (showPois) {
                        items(freePois) { poi ->
                            TextButton(
                                onClick = {
                                    selectedEntity =
                                        SelectableEntity.Poi(
                                            poi.id,
                                            poi.name
                                        )
                                }
                            ) {
                                Text("POI: ${poi.name}")
                            }
                        }
                    }
                }
            }
        }

        /* ============================================================
           3️⃣  FAB
        ============================================================ */

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
                    Text("⋮")
                }
            }
        }
    }
}
