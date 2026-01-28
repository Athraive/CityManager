@file:OptIn(ExperimentalMaterial3Api::class)

package de.geier.citymanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.geier.citymanager.ui.viewmodel.PersonViewModel


@Composable
fun AssignPoisToPersonScreen(
    person: Person,
    pois: List<PointOfInterest>,
    viewModel: PersonViewModel,
    onClose: () -> Unit
) {
    val assignedPoiIds by viewModel.poiIdsForSelectedPerson.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Orte zuweisen") },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.Close, contentDescription = "Schließen")
                    }
                }
            )
        }
    ) { padding ->

        if (pois.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Keine Orte vorhanden",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(pois, key = { it.id }) { poi ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Checkbox(
                            checked = assignedPoiIds.contains(poi.id),
                            onCheckedChange = {
                                viewModel.togglePoiAssignment(poi.id)
                            }
                        )

                        Column {
                            Text(
                                text = poi.name,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            if (poi.description.isNotBlank()) {
                                Text(
                                    text = poi.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
