package de.geier.citymanager.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import de.geier.citymanager.data.DatabaseProvider
import de.geier.citymanager.data.repository.PersonPoiRepository
import de.geier.citymanager.data.repository.PoiFactionRepository
import de.geier.citymanager.ui.viewmodel.CityViewModel
import kotlinx.coroutines.flow.first
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment

@Composable
fun PlayerCategoryListScreen(
    cityViewModel: CityViewModel,
    categoryViewModel: PoiCategoryViewModel,
    factions: List<Faction>,
    accessContext: AccessContext,
    onPersonLinkClicked: (String) -> Unit,
    onFactionLinkClicked: (String) -> Unit,
    onShowOnMap: (String) -> Unit
) {

    val context = LocalContext.current

    val categories by categoryViewModel
        .categories
        .collectAsState()

    val allPois by cityViewModel
        .allPois
        .collectAsState(initial = emptyList())

    var selectedCategory by remember {
        mutableStateOf<PoiCategory?>(null)
    }

    var selectedPoi by remember {
        mutableStateOf<PointOfInterest?>(null)
    }

    var searchPanelVisible by remember {
        mutableStateOf(false)
    }

    var searchQuery by remember {
        mutableStateOf("")
    }

    var selectedSearchCategories by remember(categories) {
        mutableStateOf(
            categories
                .filter { it.visible }
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



    val hiddenCategoryIds =
        categories
            .filter { !it.visible }
            .map { it.id }
            .toSet()

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
                    if (
                        selectedSearchCategories.contains("__OTHER__") &&
                        poi.categoryId in hiddenCategoryIds
                    ) {

                        true

                    } else {

                        selectedSearchCategories.contains(
                            poi.categoryId
                        )
                    }

                matchesName && matchesCategory
            }

    val searchPanelWidth =
        220.dp

    /* ================= KATEGORIEN ================= */

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (selectedCategory == null) {

            if (searchActive) {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            start =
                                if (searchPanelVisible)
                                    searchPanelWidth
                                else
                                    0.dp
                        ),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    if (filteredCategories.isNotEmpty()) {

                        item {

                            Text(
                                text = "Kategorien",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        items(filteredCategories) { category ->

                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {

                                        searchPanelVisible = false
                                        searchQuery = ""

                                        selectedCategory = category
                                    },
                                shape = RoundedCornerShape(16.dp),
                                tonalElevation = 2.dp
                            ) {

                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {

                                    Text(
                                        text = category.title,
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    category.description
                                        ?.takeIf { it.isNotBlank() }
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
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
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

                                        searchPanelVisible = false
                                        searchQuery = ""
                                    },
                                shape = RoundedCornerShape(16.dp),
                                tonalElevation = 2.dp
                            ) {

                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {

                                    Text(
                                        text = poi.name,
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = categoryName,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme
                                            .colorScheme
                                            .onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }

            } else {

                val playerCategories =
                    buildList {

                        addAll(
                            categories
                                .filter { it.visible }
                                .sortedBy { it.title.lowercase() }
                        )

                        add(
                            PoiCategory(
                                id = "__OTHER__",
                                title = "Sonstige Orte",
                                icon = "folder",
                                description = null,
                                visible = true
                            )
                        )
                    }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            start =
                                if (searchPanelVisible)
                                    searchPanelWidth
                                else
                                    0.dp
                        ),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    items(playerCategories) { category ->

                        val poiCount =
                            if (category.id == "__OTHER__") {

                                val hiddenCategoryIds =
                                    categories
                                        .filter { !it.visible }
                                        .map { it.id }
                                        .toSet()

                                allPois.count {
                                    it.categoryId in hiddenCategoryIds
                                }

                            } else {

                                allPois.count {
                                    it.categoryId == category.id
                                }
                            }

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedCategory = category
                                },
                            shape = RoundedCornerShape(20.dp),
                            tonalElevation = 2.dp,
                            shadowElevation = 2.dp,
                            color = MaterialTheme.colorScheme
                                .surfaceVariant
                                .copy(alpha = 0.5f)
                        ) {

                            Column(
                                modifier = Modifier.padding(18.dp),
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
            }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            IconButton(
                onClick = {
                    searchPanelVisible = true
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .size(56.dp)
            ) {

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 4.dp,
                    shadowElevation = 4.dp,
                    color = MaterialTheme.colorScheme
                        .surface
                        .copy(alpha = 0.92f)
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Suche"
                        )
                    }
                }
            }
        }

            val searchCategories =
                buildList {

                    addAll(
                        categories.filter { it.visible }
                    )

                    add(
                        PoiCategory(
                            id = "__OTHER__",
                            title = "Sonstige Orte",
                            icon = "folder",
                            description = null,
                            visible = true
                        )
                    )
                }

            SearchToolboxPanel(
                visible = searchPanelVisible,
                searchQuery = searchQuery,
                onSearchQueryChange = {
                    searchQuery = it
                },
                categories = searchCategories,
                selectedCategoryIds = selectedSearchCategories,
                onToggleCategory = { categoryId ->

                    selectedSearchCategories =
                        if (selectedSearchCategories.contains(categoryId)) {
                            selectedSearchCategories - categoryId
                        } else {
                            selectedSearchCategories + categoryId
                        }
                },
                onClose = {

                    searchPanelVisible = false
                    searchQuery = ""
                }
            )

            /* ================= POIs ================= */

        } else if (selectedPoi == null) {

            val poisInCategory =
                if (selectedCategory!!.id == "__OTHER__") {

                    val hiddenCategoryIds =
                        categories
                            .filter { !it.visible }
                            .map { it.id }
                            .toSet()

                    allPois.filter {
                        it.categoryId in hiddenCategoryIds
                    }

                } else {

                    cityViewModel
                        .poisByCategory(selectedCategory!!.id)
                        .collectAsState(initial = emptyList())
                        .value
                }

            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme
                            .surfaceVariant
                            .copy(alpha = 0.6f)
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(
                                horizontal = 12.dp,
                                vertical = 4.dp
                            )
                        ) {

                            Text(
                                text = selectedCategory!!.title,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = "✕",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier
                                    .clickable {
                                        selectedCategory = null
                                    }
                            )
                        }
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
                        poisInCategory.sortedBy {
                            it.name.lowercase()
                        }
                    ) { poi ->

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedPoi = poi
                                }
                                .padding(
                                    vertical = 10.dp,
                                    horizontal = 4.dp
                                )
                        ) {

                            Text(
                                text = poi.name,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }


            /* ================= POI DETAIL ================= */

        } else {

            val assignedFactionIds by produceState<Set<String>>(
                initialValue = emptySet(),
                key1 = selectedPoi!!.id
            ) {

                val db = DatabaseProvider.getDatabase(context)

                val repo =
                    PoiFactionRepository(db.poiFactionDao())

                value = repo
                    .getFactionIdsForPoi(selectedPoi!!.id)
                    .first()
                    .toSet()
            }

            val assignedPersons by produceState<List<Person>>(
                initialValue = emptyList(),
                key1 = selectedPoi!!.id
            ) {

                val db = DatabaseProvider.getDatabase(context)

                val personPoiRepo =
                    PersonPoiRepository(db.personPoiDao())

                val personIds =
                    personPoiRepo
                        .getPersonIdsForPoi(selectedPoi!!.id)
                        .first()

                val allPersons =
                    cityViewModel.allPersons.first()

                value =
                    allPersons
                        .filter { it.id in personIds }
                        .filter { it.visible }
            }
            val assignedFactions =
                factions.filter {
                    it.id in assignedFactionIds
                }

            PoiDetailScreen(
                poi = selectedPoi!!,
                assignedFactions = assignedFactions,
                assignedPersons = assignedPersons,
                accessContext = accessContext,

                onBack = {
                    selectedPoi = null
                },

                onSave = {},

                onDelete = {},

                onAssignFactions = {},

                onPersonClick = { id ->
                    onPersonLinkClicked(id)
                },

                onFactionClick = { id ->
                    onFactionLinkClicked(id)
                },

                onShowOnMap = onShowOnMap
            )
        }
    }
    }