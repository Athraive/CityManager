package de.geier.citymanager.ui

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import de.geier.citymanager.ui.viewmodel.CityViewModel
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectTapGestures

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
    var panelOpen by remember { mutableStateOf(false) }
    var selectedEntity by remember { mutableStateOf<SelectableEntity?>(null) }

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

        /* ---------------- MAP ---------------- */

        if (lore?.mapImageUri == null) {

            Text(
                text = "Keine Stadtkarte hinterlegt",
                modifier = Modifier.align(Alignment.Center)
            )

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
                    contentDescription = "Stadtkarte",
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
                        val x = person.mapX!! * width
                        val y = person.mapY!! * height

                        Box(
                            modifier = Modifier
                                .graphicsLayer {
                                    translationX = x
                                    translationY = y
                                }
                                .size(pinSizeDp)
                                .border(0.5.dp, Color.White, CircleShape)
                                .background(Color.Blue, CircleShape)
                        )
                    }

                    pois.filter { it.mapX != null }.forEach { poi ->
                        val x = poi.mapX!! * width
                        val y = poi.mapY!! * height

                        Box(
                            modifier = Modifier
                                .graphicsLayer {
                                    translationX = x
                                    translationY = y
                                }
                                .size(pinSizeDp)
                                .border(0.5.dp, Color.White, CircleShape)
                                .background(Color.Red, CircleShape)
                        )
                    }
                }
            }
        }

        /* ---------------- OVERFLOW MENU ---------------- */

        var menuExpanded by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {

            IconButton(onClick = { menuExpanded = true }) {
                Icon(Icons.Default.MoreVert, contentDescription = null)
            }

            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false }
            ) {

                DropdownMenuItem(
                    text = {
                        Text(
                            if (mapMode == MapMode.VIEW) "Editieren"
                            else "Ansicht"
                        )
                    },
                    onClick = {
                        menuExpanded = false

                        if (mapMode == MapMode.VIEW) {
                            mapMode = MapMode.EDIT
                            panelOpen = true
                        } else {
                            mapMode = MapMode.VIEW
                            panelOpen = false
                        }
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

        /* ---------------- SIDE PANEL ---------------- */

        if (panelOpen && mapMode == MapMode.EDIT) {

            Surface(
                tonalElevation = 8.dp,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(260.dp)
                    .align(Alignment.CenterEnd)
            ) {

                LazyColumn(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    item {
                        Text(
                            "Personen",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    items(persons) { person ->

                        val placed = person.mapX != null

                        Text(
                            text = person.name + if (placed) " ✓" else "",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedEntity =
                                        SelectableEntity.Person(person.id)
                                    panelOpen = false
                                }
                                .padding(8.dp)
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "POIs",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    items(pois) { poi ->

                        val placed = poi.mapX != null

                        Text(
                            text = poi.name + if (placed) " ✓" else "",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedEntity =
                                        SelectableEntity.Poi(poi.id)
                                    panelOpen = false
                                }
                                .padding(8.dp)
                        )
                    }
                }
            }
        }

        /* ---------------- TAP TO PLACE ---------------- */

        if (mapMode == MapMode.EDIT && selectedEntity != null) {

            LaunchedEffect(selectedEntity) {
                // wartet auf nächsten Tap
            }

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .pointerInput(selectedEntity) {
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

                            when (val entity = selectedEntity) {

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

                                null -> {}
                            }

                            selectedEntity = null
                        }
                    }
            )
        }
    }
}

/* ---------------- ENTITY WRAPPER ---------------- */

private sealed class SelectableEntity {
    data class Person(val id: String) : SelectableEntity()
    data class Poi(val id: String) : SelectableEntity()
}
