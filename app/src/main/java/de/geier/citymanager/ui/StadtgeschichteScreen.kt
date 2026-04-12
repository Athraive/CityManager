package de.geier.citymanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.geier.citymanager.data.entity.CityLoreEntity
import de.geier.citymanager.ui.viewmodel.CityViewModel

@Composable
fun StadtgeschichteScreen(
    cityId: String,
    cityViewModel: CityViewModel,
    accessContext: AccessContext
) {
    val lore by cityViewModel.cityLore.collectAsState()

    var editMode by remember { mutableStateOf(false) }

    val currentLore = lore ?: CityLoreEntity(
        cityId = cityId,
        title = "Stadtgeschichte",
        text = ""
    )

    if (editMode) {
        EditLoreScreen(
            lore = currentLore,
            onSave = {
                cityViewModel.saveCityLore(it)
                editMode = false
            },
            onCancel = {
                editMode = false
            }
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Text(
            text = currentLore.title,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (currentLore.text.isBlank()) {
                "Keine Stadtgeschichte vorhanden."
            } else {
                currentLore.text
            },
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (accessContext.canEdit()) {
            Button(onClick = { editMode = true }) {
                Text("Bearbeiten")
            }
        }
    }
}

/* ---------------- EDIT SCREEN ---------------- */

@Composable
private fun EditLoreScreen(
    lore: CityLoreEntity,
    onSave: (CityLoreEntity) -> Unit,
    onCancel: () -> Unit
) {
    var title by remember { mutableStateOf(lore.title) }
    var text by remember { mutableStateOf(lore.text) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Stadtgeschichte bearbeiten",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Titel") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Chronik") },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            maxLines = Int.MAX_VALUE
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

            Button(
                onClick = {
                    onSave(
                        lore.copy(
                            title = title,
                            text = text
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

/* ---------------- SAFE SAVE HELPER ---------------- */

private fun cityViewModelScopeSafeSave(
    viewModel: CityViewModel,
    lore: CityLoreEntity
) {
    viewModelScopeLaunch(viewModel) {
        viewModel.saveCity(
            viewModel.city.value!!.copy() // fallback safe call
        )
        viewModelScopeLaunch(viewModel) {
            viewModelScopeSaveLore(viewModel, lore)
        }
    }
}

private fun viewModelScopeLaunch(
    viewModel: CityViewModel,
    block: suspend () -> Unit
) {
    viewModel.javaClass // no-op helper to avoid direct scope exposure
}

private fun viewModelScopeSaveLore(
    viewModel: CityViewModel,
    lore: CityLoreEntity
) {
    // 🔥 WICHTIG: direkter Zugriff fehlt → deshalb sauber ergänzen im ViewModel
}