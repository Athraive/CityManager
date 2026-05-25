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

@Composable
fun PlayerCategoryListScreen(
    cityViewModel: CityViewModel,
    categoryViewModel: PoiCategoryViewModel,
    factions: List<Faction>,
    accessContext: AccessContext,
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

    /* ================= KATEGORIEN ================= */

    if (selectedCategory == null) {

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(
                categories
                    .filter { it.visible }
                    .sortedBy { it.title.lowercase() }
            ) { category ->

                val poiCount =
                    allPois.count { it.categoryId == category.id }

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

        /* ================= POIs ================= */

    } else if (selectedPoi == null) {

        val poisInCategory by cityViewModel
            .poisByCategory(selectedCategory!!.id)
            .collectAsState(initial = emptyList())

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

                    Text(
                        text = selectedCategory!!.title,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .clickable {
                                selectedCategory = null
                            }
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

        PlayerPoiDetailScreen(
            poi = selectedPoi!!,
            factions = factions,
            assignedFactionIds = assignedFactionIds,
            assignedPersons = assignedPersons,
            accessContext = accessContext,
            onSave = {},
            onBack = {
                selectedPoi = null
            },
            onShowOnMap = onShowOnMap
        )
    }
}