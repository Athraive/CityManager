package de.geier.citymanager.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.UUID

@Composable
fun PoiCreateScreen(
    poiType: PoiType,
    onSave: (PointOfInterest) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    // genau eine Kategorie auswählbar
    var selectedCategoryId by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = if (poiType == PoiType.LOCATION) "➕ Neuer Ort" else "➕ Neues Geschäft",
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

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Kategorie",
            style = MaterialTheme.typography.titleMedium
        )

        // Kategorien-Auswahl (Checkboxen, aber exklusiv)
        POI_CATEGORIES.forEach { category ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        selectedCategoryId = category.id
                    }
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Checkbox(
                    checked = selectedCategoryId == category.id,
                    onCheckedChange = {
                        selectedCategoryId = category.id
                    }
                )
                Text("${category.icon} ${category.title}")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                enabled = name.isNotBlank() && selectedCategoryId != null,
                onClick = {
                    onSave(
                        PointOfInterest(
                            id = UUID.randomUUID().toString(),
                            name = name,
                            description = description,
                            categoryId = selectedCategoryId!!,
                            type = poiType,
                            visible = true
                        )
                    )
                }
            ) {
                Text("Speichern")
            }

            OutlinedButton(onClick = onCancel) {
                Text("Abbrechen")
            }
        }
    }
}
