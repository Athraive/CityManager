@file:OptIn(ExperimentalMaterial3Api::class)

package de.geier.citymanager.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import de.geier.citymanager.ui.viewmodel.CityViewModel
import de.geier.citymanager.ui.viewmodel.PoiViewModel
import java.util.UUID
import androidx.compose.material.icons.filled.Close

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
    searchPanelVisible: Boolean,
    onSearchPanelVisibleChange: (Boolean) -> Unit,
    onPersonLinkClicked: (String) -> Unit,
    onFactionLinkClicked: (String) -> Unit,
    onShowOnMap: (String) -> Unit
) {

    val categories by categoryViewModel.categories.collectAsState()

    var selectedCategory by remember {
        mutableStateOf<PoiCategory?>(null)
    }

    var selectedPoi by remember {
        mutableStateOf<PointOfInterest?>(null)
    }

    var editingCategory by remember {
        mutableStateOf<PoiCategory?>(null)
    }

    var assigningFactions by remember {
        mutableStateOf(false)
    }

    var searchQuery by remember {
        mutableStateOf("")
    }

    var selectedSearchCategories by remember(categories) {
        mutableStateOf(
            categories
                .map { it.id }
                .toSet()
        )
    }

    val searchActive = searchPanelVisible

    val hasSearchQuery =
        searchQuery.isNotBlank()

    val filteredCategories =
        categories
            .filter { it.visible }
            .filter {

                if (!hasSearchQuery) {
                    return@filter false
                }

                val matchesCategory =
                    it.title.contains(
                        searchQuery,
                        ignoreCase = true
                    )

                val matchesFilter =
                    selectedSearchCategories.contains(it.id)

                matchesCategory && matchesFilter
            }

    val filteredPois =
        allPois
            .filter { poi ->

                if (!hasSearchQuery) {
                    return@filter false
                }

                val matchesName =
                    poi.name.contains(
                        searchQuery,
                        ignoreCase = true
                    )

                val matchesCategory =
                    selectedSearchCategories.contains(
                        poi.categoryId
                    )

                matchesName && matchesCategory
            }

    val searchPanelWidth =
        220.dp

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
            onAssignFactions = { updatedPoi ->

                cityViewModel.savePoi(updatedPoi)

                selectedPoi = updatedPoi
                poiViewModel.selectPoi(updatedPoi)

                assigningFactions = true
            },
            onPersonClick = { id ->
                onPersonLinkClicked(id)
            },

            onFactionClick = { id ->
                onFactionLinkClicked(id)
            },
            onShowOnMap = onShowOnMap
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

    /* ---------------- MAIN ---------------- */

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

                Icon(
                    Icons.Default.Add,
                    contentDescription = "Hinzufügen"
                )
            }
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            if (selectedCategory == null) {

                if (searchActive) {

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = searchPanelWidth),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement =
                            Arrangement.spacedBy(16.dp)
                    ) {

                        if (filteredCategories.isNotEmpty()) {

                            item {

                                Text(
                                    text = "Kategorien",
                                    style =
                                        MaterialTheme
                                            .typography
                                            .bodyMedium,
                                    fontWeight =
                                        FontWeight.SemiBold
                                )
                            }

                            items(filteredCategories) { category ->

                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {

                                            selectedCategory = category

                                            onSearchPanelVisibleChange(false)
                                            searchQuery = ""
                                        },
                                    shape = RoundedCornerShape(16.dp),
                                    tonalElevation = 2.dp
                                ) {

                                    Column(
                                        modifier =
                                            Modifier.padding(16.dp)
                                    ) {

                                        Text(
                                            text = category.title,
                                            style =
                                                MaterialTheme
                                                    .typography
                                                    .titleMedium
                                        )

                                        category.description
                                            ?.takeIf {
                                                it.isNotBlank()
                                            }
                                            ?.let { description ->

                                                Spacer(
                                                    modifier =
                                                        Modifier.height(4.dp)
                                                )

                                                Text(
                                                    text = description,
                                                    style =
                                                        MaterialTheme
                                                            .typography
                                                            .bodySmall,
                                                    color =
                                                        MaterialTheme
                                                            .colorScheme
                                                            .onSurfaceVariant
                                                )
                                            }
                                    }
                                }
                            }
                        }

                        if (filteredPois.isNotEmpty()) {

                            item {

                                Text(
                                    text = "Orte",
                                    style =
                                        MaterialTheme
                                            .typography
                                            .bodyMedium,
                                    fontWeight =
                                        FontWeight.SemiBold
                                )
                            }

                            items(filteredPois.sortedBy { it.name }) { poi ->

                                val categoryName =
                                    categories.firstOrNull {
                                        it.id == poi.categoryId
                                    }?.title ?: ""

                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {

                                            val category =
                                                categories.firstOrNull {
                                                    it.id == poi.categoryId
                                                }

                                            selectedCategory = category
                                            selectedPoi = poi

                                            poiViewModel.selectPoi(poi)

                                            onSearchPanelVisibleChange(false)
                                            searchQuery = ""
                                        },
                                    shape = RoundedCornerShape(16.dp),
                                    tonalElevation = 2.dp
                                ) {

                                    Column(
                                        modifier =
                                            Modifier.padding(16.dp)
                                    ) {

                                        Text(
                                            text = poi.name,
                                            style =
                                                MaterialTheme
                                                    .typography
                                                    .titleMedium
                                        )

                                        Spacer(
                                            modifier =
                                                Modifier.height(4.dp)
                                        )

                                        Text(
                                            text = categoryName,
                                            style =
                                                MaterialTheme
                                                    .typography
                                                    .bodySmall,
                                            color =
                                                MaterialTheme
                                                    .colorScheme
                                                    .onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }

                } else {

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement =
                            Arrangement.spacedBy(12.dp),
                        verticalArrangement =
                            Arrangement.spacedBy(12.dp)
                    ) {

                        items(
                            categories.sortedBy {
                                it.title.lowercase()
                            }
                        ) { category ->

                            val poiCount =
                                allPois.count {
                                    it.categoryId == category.id
                                }

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
                                color =
                                    MaterialTheme
                                        .colorScheme
                                        .surfaceVariant
                                        .copy(alpha = 0.5f)
                            ) {

                                Column(
                                    modifier =
                                        Modifier.padding(18.dp),
                                    verticalArrangement =
                                        Arrangement.spacedBy(10.dp)
                                ) {

                                    Text(
                                        text = category.title,
                                        style =
                                            MaterialTheme
                                                .typography
                                                .titleMedium,
                                        fontWeight =
                                            FontWeight.SemiBold,
                                        maxLines = 2
                                    )

                                    Text(
                                        text = "$poiCount Orte",
                                        style =
                                            MaterialTheme
                                                .typography
                                                .bodySmall,
                                        color =
                                            MaterialTheme
                                                .colorScheme
                                                .onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }

                SearchToolboxPanel(
                    visible = searchPanelVisible,
                    searchQuery = searchQuery,
                    onSearchQueryChange = {
                        searchQuery = it
                    },
                    categories = categories,
                    selectedCategoryIds =
                        selectedSearchCategories,
                    onToggleCategory = { categoryId ->

                        selectedSearchCategories =
                            if (
                                selectedSearchCategories.contains(categoryId)
                            ) {
                                selectedSearchCategories - categoryId
                            } else {
                                selectedSearchCategories + categoryId
                            }
                    },
                    onClose = {

                        onSearchPanelVisibleChange(false)
                        searchQuery = ""
                    }
                )

            } else {

                val poisInCategory =
                    allPois.filter {
                        it.categoryId == selectedCategory!!.id
                    }

                Column(
                    modifier = Modifier.fillMaxSize()
                ) {

                    TopAppBar(
                        title = { },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    selectedCategory = null
                                }
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Schließen"
                                )
                            }
                        }
                    )

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = selectedCategory!!.title,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        selectedCategory!!
                            .description
                            ?.takeIf { it.isNotBlank() }
                            ?.let { description ->

                                Spacer(
                                    modifier = Modifier.height(12.dp)
                                )

                                Text(
                                    text = description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme
                                        .colorScheme
                                        .onSurfaceVariant
                                        .copy(alpha = 0.85f)
                                )
                            }
                    }


                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement =
                            Arrangement.spacedBy(8.dp)
                    ) {

                        items(
                            poisInCategory.sortedBy {
                                it.name.lowercase()
                            }
                        ) { poi ->

                            ElevatedCard(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    selectedPoi = poi
                                    poiViewModel.selectPoi(poi)
                                }
                            ) {

                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {

                                    Text(
                                        text = poi.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )

                                    poi.shortDescription
                                        .takeIf { it.isNotBlank() }
                                        ?.let { shortDescription ->

                                            Spacer(
                                                modifier = Modifier.height(4.dp)
                                            )

                                            Text(
                                                text = shortDescription,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme
                                                    .colorScheme
                                                    .onSurfaceVariant,
                                                maxLines = 1
                                            )
                                        }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
