package de.geier.citymanager.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import de.geier.citymanager.data.DatabaseProvider
import de.geier.citymanager.data.repository.PoiFactionRepository
import de.geier.citymanager.data.repository.PersonPoiRepository
import de.geier.citymanager.ui.viewmodel.CityViewModel
import kotlinx.coroutines.flow.first

@Composable
fun PlayerCategoryListScreen(
    cityViewModel: CityViewModel,
    categoryViewModel: PoiCategoryViewModel,
    factions: List<Faction>,
    accessContext: AccessContext,
    onShowOnMap: (String) -> Unit   // ✅ NEU
) {
    val context = LocalContext.current
    val categories by categoryViewModel.categories.collectAsState()

    var selectedCategory by remember { mutableStateOf<PoiCategory?>(null) }
    var selectedPoi by remember { mutableStateOf<PointOfInterest?>(null) }

    /* ================= Kategorien ================= */

    if (selectedCategory == null) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories.filter { it.visible }) { category ->
                Text(
                    text = "${category.icon} ${category.title}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedCategory = category }
                        .padding(12.dp)
                )
            }
        }

        /* ================= POIs ================= */

    } else if (selectedPoi == null) {

        val poisInCategory by cityViewModel
            .poisByCategory(selectedCategory!!.id)
            .collectAsState(initial = emptyList())

        Column(modifier = Modifier.fillMaxSize()) {

            Text(
                text = "← ${selectedCategory!!.title}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { selectedCategory = null }
            )

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(poisInCategory) { poi ->
                    Text(
                        text = "• ${poi.name}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedPoi = poi }
                            .padding(8.dp)
                    )
                }
            }
        }

        /* ================= POI-Detail ================= */

    } else {

        val assignedFactionIds by produceState<Set<String>>(
            initialValue = emptySet(),
            key1 = selectedPoi!!.id
        ) {
            val db = DatabaseProvider.getDatabase(context)
            val repo = PoiFactionRepository(db.poiFactionDao())

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
            val personPoiRepo = PersonPoiRepository(db.personPoiDao())

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
            onBack = { selectedPoi = null },
            onShowOnMap = onShowOnMap   // ✅ ENTSCHEIDEND
        )
    }
}