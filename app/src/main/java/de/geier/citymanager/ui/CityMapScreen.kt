package de.geier.citymanager.ui

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
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
import de.geier.citymanager.ui.navigation.Route
import de.geier.citymanager.ui.viewmodel.CityViewModel
import kotlin.math.max

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
    val poiCategories by cityViewModel.poiCategories.collectAsState()

    var toolboxOpen by remember { mutableStateOf(false) }
    var placementMode by remember { mutableStateOf(false) }
    var moveMode by remember { mutableStateOf(false) }

    var scale by remember { mutableStateOf(1f) }
    var panOffset by remember { mutableStateOf(Offset.Zero) }

    var containerSize by remember { mutableStateOf(IntSize.Zero) }
    var renderedWidth by remember { mutableStateOf(0f) }
    var renderedHeight by remember { mutableStateOf(0f) }

    // 🆕 Filter-State
    var showPersons by remember { mutableStateOf(true) }
    var visiblePoiCategoryIds by remember {
        mutableStateOf<Set<String>>(emptySet())
    }

    // Initial: alle Kategorien sichtbar
    LaunchedEffect(poiCategories) {
        visiblePoiCategoryIds = poiCategories.map { it.id }.toSet()
    }

    val filteredPersons =
        if (showPersons) persons else emptyList()

    val filteredPois =
        pois.filter { visiblePoiCategoryIds.contains(it.categoryId) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clipToBounds()
            .onSizeChanged { containerSize = it }
    ) {

        if (lore?.mapImageUri != null) {
            MapContent(
                mapImageUri = lore!!.mapImageUri!!,
                persons = filteredPersons,
                pois = filteredPois,
                containerSize = containerSize,
                scale = scale,
                panOffset = panOffset,
                placementMode = placementMode,
                moveMode = moveMode,
                onTapNormalized = { _, _ -> },
                onMovePin = { _, _, _, _ -> },
                onMoveFinished = {},
                onRenderedSizeCalculated = { w, h ->
                    renderedWidth = w
                    renderedHeight = h
                }
            )
        }

        // 🔹 Filter-UI (minimalistisch)
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .zIndex(3f)
        ) {

            Surface(
                shape = RoundedCornerShape(12.dp),
                tonalElevation = 6.dp
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {

                    Text("Filter")

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.toggleable(
                            value = showPersons,
                            onValueChange = { showPersons = it }
                        )
                    ) {
                        Checkbox(
                            checked = showPersons,
                            onCheckedChange = null
                        )
                        Text("Personen")
                    }

                    poiCategories.forEach { category ->
                        val checked =
                            visiblePoiCategoryIds.contains(category.id)

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.toggleable(
                                value = checked,
                                onValueChange = { isChecked ->
                                    visiblePoiCategoryIds =
                                        if (isChecked)
                                            visiblePoiCategoryIds + category.id
                                        else
                                            visiblePoiCategoryIds - category.id
                                }
                            )
                        ) {
                            Checkbox(
                                checked = checked,
                                onCheckedChange = null
                            )
                            Text(category.title)
                        }
                    }
                }
            }
        }
    }
}