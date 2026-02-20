package de.geier.citymanager.ui

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.*
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

    val lore by cityViewModel.cityLore.collectAsState()
    val persons by cityViewModel.allPersons.collectAsState()
    val pois by cityViewModel.allPois.collectAsState()

    var mode by remember { mutableStateOf(MapMode.VIEW) }

    var scale by remember { mutableStateOf(1f) }
    var panOffset by remember { mutableStateOf(Offset.Zero) }

    var containerSize by remember { mutableStateOf(IntSize.Zero) }

    var sidePanelOpen by remember { mutableStateOf(false) }
    var addPosition by remember { mutableStateOf<Offset?>(null) }

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
            .pointerInput(mode, scale, sidePanelOpen) {

                // Drag → Pan (nur wenn Panel geschlossen)
                if (!sidePanelOpen) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        if (scale > 1f) {
                            panOffset += dragAmount
                        }
                    }
                }
            }
            .pointerInput(mode, scale, panOffset, sidePanelOpen) {

                // Tap auf leere Karte im EDIT-Modus
                if (mode == MapMode.EDIT && !sidePanelOpen) {

                    detectTapGestures { tapOffset ->

                        val mapX = (tapOffset.x - panOffset.x) / scale
                        val mapY = (tapOffset.y - panOffset.y) / scale

                        addPosition = Offset(
                            mapX / containerSize.width,
                            mapY / containerSize.height
                        )

                        sidePanelOpen = true
                    }
                }
            }
    ) {

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

        // Bottom UI
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.Gray.copy(alpha = 0.4f))
                .padding(8.dp)
        ) {

            Box(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(bottom = 8.dp)
                    .background(
                        Color.Gray.copy(alpha = 0.6f),
                        RoundedCornerShape(12.dp)
                    )
                    .border(1.dp, Color.Black, RoundedCornerShape(12.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickable {
                        mode =
                            if (mode == MapMode.VIEW)
                                MapMode.EDIT
                            else
                                MapMode.VIEW
                    }
            ) {
                Text(
                    text = if (mode == MapMode.VIEW) "Edit" else "View",
                    color = Color.Black
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {

                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = Color.Black
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

        // Top menu
        var menuExpanded by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {

            Box(
                modifier = Modifier
                    .background(
                        Color.Gray.copy(alpha = 0.6f),
                        RoundedCornerShape(12.dp)
                    )
                    .border(1.dp, Color.Black, RoundedCornerShape(12.dp))
            ) {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(Icons.Default.MoreVert, null, tint = Color.Black)
                }
            }

            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false }
            ) {

                DropdownMenuItem(
                    text = { Text("Karte ändern") },
                    onClick = {
                        menuExpanded = false
                        imagePicker.launch(arrayOf("image/*"))
                    }
                )
            }
        }

        // Side Panel
        EditSidePanel(
            visible = sidePanelOpen,
            tapPosition = addPosition,
            onClose = {
                sidePanelOpen = false
                addPosition = null
            }
        )
    }
}