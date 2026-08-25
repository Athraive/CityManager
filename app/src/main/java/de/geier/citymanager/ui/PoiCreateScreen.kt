package de.geier.citymanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.UUID

@Composable
fun PoiCreateScreen(
    categoryId: String,
    onSave: (PointOfInterest) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var visible by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "➕ Neuer POI",
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Beschreibung") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Checkbox(
                checked = visible,
                onCheckedChange = { visible = it }
            )
            Text("Für Spieler sichtbar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                enabled = name.isNotBlank(),
                onClick = {
                    onSave(
                        PointOfInterest(
                            id = UUID.randomUUID().toString(),
                            name = name,
                            description = description.takeIf { it.isNotBlank() },
                            categoryId = categoryId,
                            visible = visible,
                            factionId = null,
                            playerNotes = "",
                            gameMasterNotes = ""
                        )
                    )
                }
            ) {
                Text("Speichern",
                    style = MaterialTheme.typography.bodyMedium)
            }

            OutlinedButton(onClick = onCancel) {
                Text("Abbrechen")
            }
        }
    }
}
