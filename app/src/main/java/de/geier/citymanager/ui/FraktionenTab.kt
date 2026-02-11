@file:OptIn(ExperimentalMaterial3Api::class)

package de.geier.citymanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import de.geier.citymanager.data.DatabaseProvider
import de.geier.citymanager.data.repository.PersonFactionRepository
import de.geier.citymanager.data.repository.PoiFactionRepository
import kotlinx.coroutines.flow.first
import java.util.UUID

@Composable
fun FraktionenTab(
    accessContext: AccessContext,
    factions: List<Faction>,
    persons: List<Person>,
    pois: List<PointOfInterest>,
    onSave: (Faction) -> Unit,
    onDelete: (Faction) -> Unit
) {

    val context = LocalContext.current
    var activeFaction by remember { mutableStateOf<Faction?>(null) }

    /* ================= Detail ================= */

    activeFaction?.let { faction ->

        val db = DatabaseProvider.getDatabase(context)
        val personFactionRepo = PersonFactionRepository(db.personFactionDao())
        val poiFactionRepo = PoiFactionRepository(db.poiFactionDao())

        /* ---------- Zugeordnete Personen ---------- */

        val assignedPersons by produceState<List<Person>>(
            initialValue = emptyList(),
            key1 = faction.id
        ) {
            val result = mutableListOf<Person>()

            persons.forEach { person ->
                val factionIds =
                    personFactionRepo
                        .getFactionIdsForPerson(person.id)
                        .first()

                if (faction.id in factionIds) {
                    result.add(person)
                }
            }

            value =
                if (accessContext.canEdit())
                    result
                else
                    result.filter { it.visible }
        }

        /* ---------- Zugeordnete POIs ---------- */

        val assignedPois by produceState<List<PointOfInterest>>(
            initialValue = emptyList(),
            key1 = faction.id
        ) {
            val result = mutableListOf<PointOfInterest>()

            pois.forEach { poi ->
                val factionIds =
                    poiFactionRepo
                        .getFactionIdsForPoi(poi.id)
                        .first()

                if (faction.id in factionIds) {
                    result.add(poi)
                }
            }

            value =
                if (accessContext.canEdit())
                    result
                else
                    result.filter { it.visible }
        }

        FactionDetailScreen(
            faction = faction,
            assignedPersons = assignedPersons,
            assignedPois = assignedPois,
            accessContext = accessContext,
            onBack = { activeFaction = null },
            onSave = onSave,
            onDelete = onDelete
        )
        return
    }

    /* ================= Liste ================= */

    Scaffold(
        floatingActionButton = {
            if (accessContext.canEdit()) {
                FloatingActionButton(
                    onClick = {
                        activeFaction = Faction(
                            id = UUID.randomUUID().toString(),
                            name = "",
                            description = null,
                            visible = true,
                            playerNotes = "",
                            gameMasterNotes = ""
                        )
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Fraktion anlegen")
                }
            }
        }
    ) { padding ->

        val visibleFactions =
            if (accessContext.canEdit()) factions
            else factions.filter { it.visible }

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = visibleFactions,
                key = { it.id }
            ) { faction ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { activeFaction = faction }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            faction.name,
                            style = MaterialTheme.typography.titleMedium
                        )
                        faction.description?.let { Text(it) }
                    }
                }
            }
        }
    }
}
