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
import java.util.UUID

@Composable
fun GameMasterCategoryListScreen(
    categoryViewModel: PoiCategoryViewModel,
    cityViewModel: CityViewModel,
    allPois: List<PointOfInterest>,
    accessContext: AccessContext
) {
    val categories by categoryViewModel.categories.collectAsState()
    val factions by cityViewModel.factions.collectAsState()

    var selectedCategory by remember { mutableStateOf<PoiCategory?>(null) }
    var selectedPoi by remember { mutableStateOf<PointOfInterest?>(null) }
    var editingCategory by remember { mutableStateOf<PoiCategory?>(null) }

    /* ------------------------------------------------------------------ */
    /* POI DETAIL (SL)                                                     */
    /* ------------------------------------------------------------------ */

    if (selectedPoi != null) {
        PlayerPoiDetailScreen(
            poi = selectedPoi!!,
            factions = factions,
            accessContext = accessContext,
            categoryTitle = selectedCategory?.title,
            onSave = { cityViewModel.savePoi(it) },
            onDelete = { cityViewModel.deletePoi(it) },
            onBack = { selectedPoi = null }
        )
        return
    }

    /* ------------------------------------------------------------------ */
    /* CATEGORY DETAIL (SL)                                                */
    /* ------------------------------------------------------------------ */

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
            onDelete = { category ->
                if (poiCount == 0) {
                    categoryViewModel.delete(category)
                    editingCategory = null
                }
            }
        )
        return
    }

    /* ------------------------------------------------------------------ */
    /* LIST / KATEGORIEN                                                   */
    /* ------------------------------------------------------------------ */

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
                        selectedPoi = PointOfInterest(
                            id = UUID.randomUUID().toString(),
                            name = "",
                            description = "",
                            categoryId = selectedCategory!!.id,
                            visible = true,
                            factionId = null,
                            playerNotes = "",
                            gameMasterNotes = "",
                            type = PoiType.LOCATION
                        )
                    }
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Hinzufügen")
            }
        }
    ) { padding ->

        if (selectedCategory == null) {

            /* ---------------- Kategorien ---------------- */

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(categories) { category ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${category.icon} ${category.title}",
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedCategory = category }
                        )
                        Text(
                            text = "✏",
                            modifier = Modifier.clickable {
                                editingCategory = category
                            }
                        )
                    }
                }
            }

        } else {

            /* ---------------- POIs ---------------- */

            val poisInCategory =
                allPois.filter { it.categoryId == selectedCategory!!.id }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {

                Text(
                    text = "← ${selectedCategory!!.title}",
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable { selectedCategory = null }
                )

                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(poisInCategory) { poi ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedPoi = poi }
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
