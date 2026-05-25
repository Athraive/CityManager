@file:OptIn(ExperimentalMaterial3Api::class)

package de.geier.citymanager.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.geier.citymanager.ui.viewmodel.CityViewModel
import de.geier.citymanager.ui.viewmodel.PoiViewModel
import java.util.UUID
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GameMasterCategoryListScreen(
    categoryViewModel: PoiCategoryViewModel,
    cityViewModel: CityViewModel,
    poiViewModel: PoiViewModel,
    allPois: List<PointOfInterest>,
    factions: List<Faction>,
    persons: List<Person>,
    accessContext: AccessContext,
    onShowOnMap: (String) -> Unit
) {

    val categories by categoryViewModel.categories.collectAsState()

    var selectedCategory by remember { mutableStateOf<PoiCategory?>(null) }
    var selectedPoi by remember { mutableStateOf<PointOfInterest?>(null) }
    var editingCategory by remember { mutableStateOf<PoiCategory?>(null) }
    var assigningFactions by remember { mutableStateOf(false) }

    /* ---------------- ASSIGN FACTIONS ---------------- */

    if (assigningFactions && selectedPoi != null) {
        AssignFactionsToPoiScreen(
            poi = selectedPoi!!,
            factions = factions,
            poiViewModel = poiViewModel,
            onBack = { assigningFactions = false }
        )
        return
    }

    /* ---------------- POI DETAIL ---------------- */

    if (selectedPoi != null) {

        val assignedFactionIds by poiViewModel
            .factionIdsForSelectedPoi
            .collectAsState()

        val assignedPersonIds by poiViewModel
            .personIdsForSelectedPoi
            .collectAsState()

        val assignedFactions =
            factions.filter { assignedFactionIds.contains(it.id) }

        val assignedPersons =
            persons.filter { assignedPersonIds.contains(it.id) }

        PoiDetailScreen(
            poi = selectedPoi!!,
            assignedFactions = assignedFactions,
            assignedPersons = assignedPersons,
            accessContext = accessContext,
            onBack = {
                selectedPoi = null
                poiViewModel.clearSelection()
            },
            onSave = { cityViewModel.savePoi(it) },
            onDelete = {
                cityViewModel.deletePoi(it)
                selectedPoi = null
                poiViewModel.clearSelection()
            },
            onAssignFactions = {
                assigningFactions = true
            },
            onPersonClick = { },
            onFactionClick = { },
            onShowOnMap = onShowOnMap   // ✅ EINZIGE ÄNDERUNG
        )
        return
    }

    /* ---------------- CATEGORY DETAIL ---------------- */

    if (editingCategory != null) {

        val poiCount =
            allPois.count { it.categoryId == editingCategory!!.id }

        CategoryDetailScreen(
            category = editingCategory!!,
            poiCountInCategory = poiCount,
            onBack = { editingCategory = null },
            accessContext = accessContext,
            onSave = {
                categoryViewModel.save(it)
                editingCategory = null
            },
            onDelete = {
                if (poiCount == 0) {
                    categoryViewModel.delete(it)
                    editingCategory = null
                }
            }
        )
        return
    }

    /* ---------------- LIST ---------------- */

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (selectedCategory == null) {
                        editingCategory = PoiCategory(
                            id = UUID.randomUUID().toString(),
                            title = "",
                            icon = "📁",
                            visible = true
                        )
                    } else {
                        val newPoi = PointOfInterest(
                            id = UUID.randomUUID().toString(),
                            name = "",
                            description = "",
                            categoryId = selectedCategory!!.id,
                            visible = true,
                            factionId = null,
                            playerNotes = "",
                            gameMasterNotes = ""
                        )
                        selectedPoi = newPoi
                        poiViewModel.selectPoi(newPoi)
                    }
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Hinzufügen")
            }
        }
    ) { padding ->

        if (selectedCategory == null) {

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(
                    categories.sortedBy { it.title.lowercase() }
                ) { category ->

                    val poiCount =
                        allPois.count { it.categoryId == category.id }

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .combinedClickable(
                                onClick = {
                                    selectedCategory = category
                                },
                                onLongClick = {
                                    editingCategory = category
                                }
                            ),
                        shape = RoundedCornerShape(20.dp),
                        tonalElevation = 2.dp,
                        shadowElevation = 2.dp,
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ) {

                        Column(
                            modifier = Modifier
                                .padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {

                            Text(
                                text = category.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 2
                            )

                            Text(
                                text = "$poiCount Orte",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

        } else {

            val poisInCategory =
                allPois.filter { it.categoryId == selectedCategory!!.id }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                    ) {

                        Text(
                            text = selectedCategory!!.title,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .clickable { selectedCategory = null }
                                .padding(
                                    horizontal = 12.dp,
                                    vertical = 4.dp
                                )
                        )
                    }

                    selectedCategory!!
                        .description
                        ?.takeIf { it.isNotBlank() }
                        ?.let { description ->

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme
                                    .onSurfaceVariant
                                    .copy(alpha = 0.85f)
                            )
                        }
                }


                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        poisInCategory.sortedBy { it.name.lowercase() }
                    ) { poi ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedPoi = poi
                                    poiViewModel.selectPoi(poi)
                                }
                                .padding(
                                    vertical = 10.dp,
                                    horizontal = 4.dp
                                )
                        ) {
                            Text(
                                text = poi.name,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(
                                    vertical = 6.dp,
                                    horizontal = 4.dp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}