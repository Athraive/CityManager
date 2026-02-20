package de.geier.citymanager.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.*
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import de.geier.citymanager.ui.navigation.Route
import de.geier.citymanager.ui.viewmodel.CityViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityMapScreen(
    cityId: String,
    cityViewModel: CityViewModel,
    accessContext: AccessContext,
    navController: NavController
) {

    val context = LocalContext.current
    val lore by cityViewModel.cityLore.collectAsState()
    val persons by cityViewModel.allPersons.collectAsState()
    val pois by cityViewModel.allPois.collectAsState()

    var toolboxOpen by remember { mutableStateOf(false) }
    var placementMode by remember { mutableStateOf(false) }

    var scale by remember { mutableStateOf(1f) }
    var panOffset by remember { mutableStateOf(Offset.Zero) }
    var containerSize by remember { mutableStateOf(IntSize.Zero) }

    var confirmReplaceMap by remember { mutableStateOf(false) }

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged { containerSize = it }
            .pointerInput(scale) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    if (scale > 1f) {
                        panOffset += dragAmount
                    }
                }
            }
            .pointerInput(placementMode, scale, panOffset) {
                if (placementMode) {
                    detectTapGestures { tapOffset ->

                        val adjustedX =
                            (tapOffset.x - panOffset.x) / scale
                        val adjustedY =
                            (tapOffset.y - panOffset.y) / scale

                        val normalizedX =
                            (adjustedX / containerSize.width)
                                .coerceIn(0f, 1f)

                        val normalizedY =
                            (adjustedY / containerSize.height)
                                .coerceIn(0f, 1f)

                        // Vorläufig nur Log-Ausgabe
                        println("Pin bei $normalizedX / $normalizedY")

                        placementMode = false
                    }
                }
            }
    ) {

        /* ---------------- Map Content ---------------- */

        if (lore?.mapImageUri != null) {
            MapContent(
                mapImageUri = lore!!.mapImageUri!!,
                persons = persons,
                pois = pois,
                containerSize = containerSize,
                scale = scale,
                panOffset = panOffset
            )
        }

        /* ---------------- Placement Hinweis ---------------- */

        if (placementMode) {
            Surface(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp),
                tonalElevation = 4.dp,
                shadowElevation = 6.dp
            ) {
                Text(
                    text = "Tippe auf die Karte, um einen Pin zu setzen",
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        /* ---------------- Zoom-Leiste ---------------- */

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .zIndex(1f)
        ) {

            Surface(
                tonalElevation = 6.dp,
                shadowElevation = 8.dp,
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {

                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )

                    Slider(
                        value = scale,
                        onValueChange = {
                            scale = it
                            if (scale == 1f) {
                                panOffset = Offset.Zero
                            }
                        },
                        valueRange = 1f..5f,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        /* ---------------- Werkzeug-Button ---------------- */

        FloatingActionButton(
            onClick = {
                toolboxOpen = !toolboxOpen
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 96.dp) // ← höher gesetzt
                .zIndex(2f),
            containerColor =
                if (toolboxOpen)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.surfaceVariant
        ) {
            Icon(Icons.Default.Build, contentDescription = null)
        }

        /* ---------------- Toolbox ---------------- */

        if (toolboxOpen) {
            EditToolboxPanel(
                onPinAdd = {
                    toolboxOpen = false
                    placementMode = true
                },
                onPinDelete = {
                    toolboxOpen = false
                    navController.navigate(Route.MANAGE_PINS)
                },
                onChangeMap = {
                    toolboxOpen = false
                    confirmReplaceMap = true
                }
            )
        }

        /* ---------------- Dialog Karte ersetzen ---------------- */

        if (confirmReplaceMap) {
            AlertDialog(
                onDismissRequest = { confirmReplaceMap = false },
                title = { Text("Karte wirklich ersetzen?") },
                text = {
                    Text("Die bestehende Karte wird überschrieben.")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            confirmReplaceMap = false
                            imagePicker.launch(arrayOf("image/*"))
                        }
                    ) {
                        Text("Ersetzen")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { confirmReplaceMap = false }
                    ) {
                        Text("Abbrechen")
                    }
                }
            )
        }
    }
}