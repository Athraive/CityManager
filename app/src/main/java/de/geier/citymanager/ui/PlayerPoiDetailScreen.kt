package de.geier.citymanager.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.geier.citymanager.data.DatabaseProvider
import de.geier.citymanager.data.repository.PersonPoiRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerPoiDetailScreen(
    poi: PointOfInterest,
    persons: List<Person>,
    factions: List<Faction>,            // ✅ NEU
    onPersonClick: (Person) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    val poiViewModel: PlayerPoiViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                val db = DatabaseProvider.getDatabase(context)
                return PlayerPoiViewModel(
                    poiId = poi.id,
                    personPoiRepository = PersonPoiRepository(db.personPoiDao())
                ) as T
            }
        }
    )

    val personIds by poiViewModel.personIdsForPoi.collectAsState()

    /* ---------------- Sichtbarkeit (gehärtet) ---------------- */

    val visiblePersons = remember(persons, personIds) {
        persons.filter { person ->
            person.visible && personIds.contains(person.id)
        }
    }

    val visibleFaction = remember(poi.factionId, factions) {
        factions.firstOrNull { faction ->
            faction.id == poi.factionId && faction.visible
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(poi.name) },
                navigationIcon = {
                    Text(
                        text = "←",
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clickable { onBack() }
                    )
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = poi.name,
                fontSize = 24.sp
            )

            if (poi.description.isNotBlank()) {
                Text(text = poi.description)
            }

            /* ---------------- Fraktion (Spieler) ---------------- */

            if (visibleFaction != null) {
                HorizontalDivider()

                Text(
                    text = "Fraktion",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = visibleFaction.name,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            /* ---------------- Personen ---------------- */

            if (visiblePersons.isNotEmpty()) {

                HorizontalDivider()

                Text(
                    text = "Personen",
                    style = MaterialTheme.typography.titleMedium
                )

                visiblePersons.forEach { person ->
                    Text(
                        text = "• ${person.name}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onPersonClick(person) }
                            .padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}
