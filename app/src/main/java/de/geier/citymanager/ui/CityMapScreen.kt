package de.geier.citymanager.ui

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.*
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import de.geier.citymanager.ui.map.MapViewModel
import de.geier.citymanager.ui.navigation.Route
import de.geier.citymanager.ui.viewmodel.CityViewModel
import kotlin.math.max
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.DashboardCustomize
import androidx.compose.foundation.gestures.detectTransformGestures

@OptIn(ExperimentalMaterial3Api::class)

private enum class MapPanel {
    NONE,
    VISIBILITY,
    CATEGORIES,
    FACTIONS,
    TOOLBOX
}
@Composable
fun CityMapScreen(
    cityId: String,
    cityViewModel: CityViewModel,
    accessContext: AccessContext,
    navController: NavController,
    mapViewModel: MapViewModel,

    focusPersonId: String? = null,
    focusPoiId: String? = null,
    onPersonBubbleClick: (String) -> Unit,
    onPoiBubbleClick: (String) -> Unit
) {

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val lore by cityViewModel.cityLore.collectAsState()
    val poiCategories by cityViewModel.poiCategories.collectAsState()
    val allFactions by cityViewModel.factions.collectAsState()

    val factions =
        if (accessContext.canEdit())
            allFactions
        else
            allFactions.filter { it.visible }

    val personsWithFactions by mapViewModel.personsWithFactions.collectAsState()
    val poisWithFactions by mapViewModel.poisWithFactions.collectAsState()

    /* ---------------- VISIBILITY STATE ---------------- */

    var showPersons by remember { mutableStateOf(true) }
    var showPois by remember { mutableStateOf(true) }
    var showFactions by remember { mutableStateOf(true) }
    var showNoFaction by remember { mutableStateOf(true) }

    var activePanel by remember {
        mutableStateOf(MapPanel.NONE)
    }


    var visibleCategoryIds by remember { mutableStateOf<Set<String>>(emptySet()) }
    var visibleFactionIds by remember { mutableStateOf<Set<String>>(emptySet()) }


    LaunchedEffect(poiCategories) {
        visibleCategoryIds = poiCategories.map { it.id }.toSet()
    }

    LaunchedEffect(factions) {
        visibleFactionIds = factions.map { it.id }.toSet()
    }


    /* ---------------- SELECTION STATE (🔥 WICHTIG) ---------------- */

    var selectedPersonId by remember { mutableStateOf<String?>(null) }
    var selectedPoiId by remember { mutableStateOf<String?>(null) }


    /* ---------------- FILTER LOGIC ---------------- */

    val basePersons =
        if (!showPersons)
            emptyList()
        else
            personsWithFactions
                .filter {
                    if (!showFactions) {
                        true
                    } else if (it.factionIds.isEmpty()) {
                        showNoFaction
                    } else {
                        it.factionIds.any { id -> visibleFactionIds.contains(id) }
                    }
                }
                .map { it.person }

// 🔥 Fokus-Person IMMER anzeigen
    val persons = remember(basePersons, selectedPersonId, personsWithFactions) {

        val focusPerson =
            personsWithFactions
                .map { it.person }
                .firstOrNull { it.id == selectedPersonId }

        if (focusPerson != null && basePersons.none { it.id == focusPerson.id }) {
            basePersons + focusPerson
        } else {
            basePersons
        }
    }


    val basePois =
        if (!showPois)
            emptyList()
        else
            poisWithFactions
                .filter {
                    if (!showFactions) {
                        true
                    } else if (it.factionIds.isEmpty()) {
                        showNoFaction
                    } else {
                        it.factionIds.any { id -> visibleFactionIds.contains(id) }
                    }
                }
                .map { it.poi }
                .filter { visibleCategoryIds.contains(it.categoryId) }

// 🔥 Fokus-POI IMMER anzeigen
    val pois = remember(basePois, selectedPoiId, poisWithFactions) {

        val focusPoi =
            poisWithFactions
                .map { it.poi }
                .firstOrNull { it.id == selectedPoiId }

        if (focusPoi != null && basePois.none { it.id == focusPoi.id }) {
            basePois + focusPoi
        } else {
            basePois
        }
    }

    /* ---------------- MAP STATE ---------------- */


    var placementMode by remember { mutableStateOf(false) }
    var moveMode by remember { mutableStateOf(false) }

    var scale by remember { mutableStateOf(1f) }
    var panOffset by remember { mutableStateOf(Offset.Zero) }

    var containerSize by remember { mutableStateOf(IntSize.Zero) }
    var renderedWidth by remember { mutableStateOf(0f) }
    var renderedHeight by remember { mutableStateOf(0f) }

    var confirmReplaceMap by remember { mutableStateOf(false) }


    /* 🔥 NEU: Fokus von außen übernehmen */

    LaunchedEffect(focusPersonId, focusPoiId) {

        if (focusPersonId != null) {
            selectedPersonId = null
            selectedPersonId = focusPersonId
            selectedPoiId = null

        } else if (focusPoiId != null) {
            selectedPoiId = null
            selectedPoiId = focusPoiId
            selectedPersonId = null
        }
    }

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

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .clipToBounds()
                .onSizeChanged { containerSize = it }


                .pointerInput(
                    moveMode,
                    renderedWidth,
                    renderedHeight,
                    containerSize,
                    activePanel
                ) {

                    if (!moveMode && activePanel == MapPanel.NONE) {

                        detectTransformGestures { _, pan, zoom, _ ->

                            if (
                                renderedWidth > 0f &&
                                renderedHeight > 0f &&
                                containerSize.width > 0 &&
                                containerSize.height > 0
                            ) {

                                // ---------------- ZOOM ----------------

                                val newScale =
                                    (scale * zoom)
                                        .coerceIn(1f, 5f)

                                scale = newScale

                                // ---------------- PAN ----------------

                                val containerWidth =
                                    containerSize.width.toFloat()

                                val containerHeight =
                                    containerSize.height.toFloat()

                                val newOffset =
                                    panOffset + pan

                                val maxPanX =
                                    max(
                                        0f,
                                        (renderedWidth * scale - containerWidth) / 2f
                                    )

                                val maxPanY =
                                    max(
                                        0f,
                                        (renderedHeight * scale - containerHeight) / 2f
                                    )

                                panOffset = Offset(
                                    x = newOffset.x.coerceIn(-maxPanX, maxPanX),
                                    y = newOffset.y.coerceIn(-maxPanY, maxPanY)
                                )

                                // Reset wenn komplett rausgezoomt

                                if (scale <= 1f) {
                                    panOffset = Offset.Zero
                                }
                            }
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
                    panOffset = panOffset,
                    placementMode = placementMode,
                    moveMode = moveMode,
                    onTapNormalized = { x, y ->
                        placementMode = false
                        navController.navigate("add_pin/$x/$y")
                    },
                    onMovePin = { id, isPerson, x, y ->
                        if (isPerson)
                            cityViewModel.updatePersonCoordinates(id, x, y)
                        else
                            cityViewModel.updatePoiCoordinates(id, x, y)
                    },
                    onMoveFinished = { moveMode = false },
                    onRenderedSizeCalculated = { w, h ->
                        renderedWidth = w
                        renderedHeight = h
                    },

                    // 🔥 NEU (Pflicht!)
                    selectedPersonId = selectedPersonId,
                    selectedPoiId = selectedPoiId,

                    onPersonClick = {
                        selectedPersonId = it
                        selectedPoiId = null
                    },
                    onPoiClick = {
                        selectedPoiId = it
                        selectedPersonId = null
                    },

                    onPersonBubbleClick = onPersonBubbleClick,
                    onPoiBubbleClick = onPoiBubbleClick
                )
            }

            /* ---------------- ZOOM ---------------- */

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

                        Icon(Icons.Default.Search, contentDescription = null)

                        Slider(
                            value = scale,
                            onValueChange = {
                                scale = it
                                if (scale == 1f) panOffset = Offset.Zero
                            },
                            valueRange = 1f..5f,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }


            /* ---------------- MAIN FAB ---------------- */

            if (accessContext.canEdit()) {

                FloatingActionButton(
                    onClick = {
                        activePanel =
                            if (activePanel == MapPanel.TOOLBOX)
                                MapPanel.NONE
                            else
                                MapPanel.TOOLBOX
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 16.dp, bottom = 96.dp)
                        .zIndex(2f),
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ) {
                    Icon(
                        Icons.Default.DashboardCustomize,
                        contentDescription = null
                    )
                }
            }

            /* ---------------- LAYER CONTROL ---------------- */

            SmallFloatingActionButton(
                onClick = {
                    activePanel =
                        if (activePanel == MapPanel.VISIBILITY)
                            MapPanel.NONE
                        else
                            MapPanel.VISIBILITY
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 16.dp, end = 16.dp)
                    .zIndex(2f),
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ) {
                Icon(
                    Icons.Default.Layers,
                    contentDescription = null
                )
            }



            /* ---------------- VISIBILITY PANEL ---------------- */

            val visibleFactionCount =
                visibleFactionIds.size + if (showNoFaction) 1 else 0

            val totalFactionCount =
                factions.size + 1

            if (activePanel == MapPanel.VISIBILITY) {

                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 16.dp, bottom = 240.dp)
                        .zIndex(3f),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                    tonalElevation = 4.dp,
                    shadowElevation = 4.dp,
                    shape = RoundedCornerShape(24.dp)
                ) {

                    Column(modifier = Modifier.padding(16.dp)) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text("Sichtbarkeit")

                            TextButton(
                                onClick = {
                                    activePanel = MapPanel.NONE
                                },
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text("✕")
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.toggleable(
                                value = showPersons,
                                onValueChange = { showPersons = it }
                            )
                        ) {
                            Switch(showPersons, null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Personen anzeigen")
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.toggleable(
                                value = showPois,
                                onValueChange = { showPois = it }
                            )
                        ) {
                            Switch(showPois, null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("POI anzeigen")

                        }

                        TextButton(
                            onClick = {
                                activePanel = MapPanel.CATEGORIES
                            },
                            enabled = showPois,
                            modifier = Modifier.padding(start = 40.dp)
                        ) {
                            Text("Kategorien (${visibleCategoryIds.size}/${poiCategories.size})")

                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.toggleable(
                                value = showFactions,
                                onValueChange = { showFactions = it }
                            )
                        ) {
                            Switch(showFactions, null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Fraktionen anzeigen")

                        }

                        TextButton(
                            onClick = {
                                activePanel = MapPanel.FACTIONS
                            },
                            enabled = showFactions,
                            modifier = Modifier.padding(start = 40.dp)
                        ) {
                            Text("Fraktionen ($visibleFactionCount/$totalFactionCount)")

                        }
                    }
                }
            }


            /* ---------------- CATEGORY PANEL ---------------- */

            if (activePanel == MapPanel.CATEGORIES) {

                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 240.dp, end = 16.dp)
                        .widthIn(min = 380.dp, max = 500.dp)
                        .zIndex(4f),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                    tonalElevation = 4.dp,
                    shadowElevation = 6.dp,
                    shape = RoundedCornerShape(24.dp)
                ) {

                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .heightIn(max = 450.dp)
                            .verticalScroll(rememberScrollState())
                    ) {

                        Text(
                            text = "POI-Kategorien",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        poiCategories.forEach { category ->

                            val checked = visibleCategoryIds.contains(category.id)

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .toggleable(
                                        value = checked,
                                        onValueChange = { isChecked ->
                                            visibleCategoryIds =
                                                if (isChecked)
                                                    visibleCategoryIds + category.id
                                                else
                                                    visibleCategoryIds - category.id

                                            showPois =
                                                visibleCategoryIds.size == poiCategories.size
                                        }
                                    )
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                Text(
                                    text = category.title,
                                    modifier = Modifier.weight(1f),
                                    maxLines = 1
                                )

                                Spacer(modifier = Modifier.width(16.dp))

                                Switch(
                                    checked = checked,
                                    onCheckedChange = null
                                )
                            }
                        }
                    }
                }
            }


            /* ---------------- FACTION PANEL ---------------- */

            if (activePanel == MapPanel.FACTIONS) {

                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 240.dp, end = 16.dp)
                        .widthIn(min = 380.dp, max = 500.dp)
                        .zIndex(4f),
                    tonalElevation = 8.dp,
                    shadowElevation = 12.dp,
                    shape = RoundedCornerShape(16.dp)
                ) {

                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .heightIn(max = 450.dp)
                            .verticalScroll(rememberScrollState())
                    ) {

                        Text(
                            text = "Fraktionen",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .toggleable(
                                    value = showNoFaction,
                                    onValueChange = { showNoFaction = it }
                                )
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Text(
                                text = "Ohne Fraktion",
                                modifier = Modifier.weight(1f)
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Switch(
                                checked = showNoFaction,
                                onCheckedChange = null
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Divider()
                        Spacer(modifier = Modifier.height(12.dp))

                        factions.forEach { faction ->

                            val checked = visibleFactionIds.contains(faction.id)

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .toggleable(
                                        value = checked,
                                        onValueChange = { isChecked ->
                                            visibleFactionIds =
                                                if (isChecked)
                                                    visibleFactionIds + faction.id
                                                else
                                                    visibleFactionIds - faction.id
                                        }
                                    )
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                Text(
                                    text = faction.name,
                                    modifier = Modifier.weight(1f),
                                    maxLines = 1
                                )

                                Spacer(modifier = Modifier.width(16.dp))

                                Switch(
                                    checked = checked,
                                    onCheckedChange = null
                                )
                            }
                        }
                    }
                }
            }

            /* ---------------- TOOLBOX PANEL ---------------- */

            if (
                activePanel == MapPanel.TOOLBOX &&
                accessContext.canEdit()
            ) {

                EditToolboxPanel(

                    onClose = {
                        activePanel = MapPanel.NONE
                    },

                    onPinAdd = {
                        activePanel = MapPanel.NONE
                        placementMode = true
                        moveMode = false

                        scope.launch {
                            snackbarHostState.showSnackbar(
                                "Bitte Ort auf der Karte auswählen"
                            )
                        }
                    },

                    onPinDelete = {

                        navController.navigate(Route.MANAGE_PINS)
                    },

                    onChangeMap = {

                        confirmReplaceMap = true
                    },

                    onMoveStart = {
                        activePanel = MapPanel.NONE
                        placementMode = false
                        moveMode = true
                    }
                )
            }

            /* ---------------- CONFIRM DIALOG ---------------- */

            if (confirmReplaceMap) {

                AlertDialog(
                    onDismissRequest = { confirmReplaceMap = false },
                    title = { Text("Karte wirklich ersetzen?") },
                    text = { Text("Die bestehende Karte wird überschrieben.") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                confirmReplaceMap = false
                                imagePicker.launch(arrayOf("image/*"))
                            }
                        ) { Text("Ersetzen") }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { confirmReplaceMap = false }
                        ) { Text("Abbrechen") }
                    }
                )
            }
        }
    }
}

