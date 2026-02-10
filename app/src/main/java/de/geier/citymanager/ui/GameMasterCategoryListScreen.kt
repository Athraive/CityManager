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

@Composable
fun GameMasterCategoryListScreen(
    categoryViewModel: PoiCategoryViewModel,
    cityViewModel: CityViewModel,
    poiViewModel: PoiViewModel,
    allPois: List<PointOfInterest>,
    factions: List<Faction>,
    accessContext: AccessContext
) {
    val categories by categoryViewModel.categories.collectAsState()

    var selectedCategory by remember { mutableStateOf<PoiCategory?>(null) }
    var selectedPoi by remember { mutableStateOf<PointOfInterest?>(null) }
    var editingCategory by remember { mutableStateOf<PoiCategory?>(null) }

    /* ---------------- POI DETAIL ---------------- */

    if (selectedPoi != null) {
        PoiDetailScreen(
            poi = selectedPoi!!,
            persons = emptyList(),
            factions = factions,
            poiViewModel = poiViewModel,
            accessContext = accessContext,
            onBack = {
                selectedPoi = null
                poiViewModel.clearSelection()
            },
            onSave = { cityViewModel.savePoi(it) },   // ✅ FIX
            onDelete = {
                cityViewModel.deletePoi(it)
                selectedPoi = null
                poiViewModel.clearSelection()
            }
        )
        return
    }

    /* ---------------- CATEGORY DETAIL ---------------- */

    if (editingCategory != null) {
        val poiCount = allPois.count { it.categoryId == editingCategory!!.id }

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

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(categories) { category ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "${category.icon} ${category.title}",
                            modifier = Modifier.weight(1f)
                                .clickable { selectedCategory = category }
                        )
                        Text("✏", modifier = Modifier.clickable {
                            editingCategory = category
                        })
                    }
                }
            }

        } else {

            val poisInCategory =
                allPois.filter { it.categoryId == selectedCategory!!.id }

            Column(
                modifier = Modifier.fillMaxSize().padding(padding)
            ) {
                Text(
                    "← ${selectedCategory!!.title}",
                    modifier = Modifier.padding(16.dp)
                        .clickable { selectedCategory = null }
                )

                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(poisInCategory) { poi ->
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .clickable {
                                    selectedPoi = poi
                                    poiViewModel.selectPoi(poi)
                                }
                                .padding(vertical = 8.dp)
                        ) {
                            Text("• ${poi.name}")
                        }
                    }
                }
            }
        }
    }
}
