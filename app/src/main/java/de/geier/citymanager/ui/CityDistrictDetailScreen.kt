package de.geier.citymanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.geier.citymanager.data.entity.CityDistrictEntity
import de.geier.citymanager.ui.viewmodel.CityViewModel

@Composable
fun CityDistrictDetailScreen(
    districtId: String,
    cityViewModel: CityViewModel
) {
    val district by cityViewModel
        .districtById(districtId)
        .collectAsState()

    district ?: return

    var editMode by remember { mutableStateOf(false) }

    if (editMode) {
        EditDistrictScreen(
            district = district!!,
            onSave = { updated ->
                cityViewModel.saveDistrict(updated)
                editMode = false
            },
            onCancel = { editMode = false }
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = district!!.name,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = district!!.description,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { editMode = true }
        ) {
            Text("Bearbeiten")
        }
    }
}

@Composable
private fun EditDistrictScreen(
    district: CityDistrictEntity,
    onSave: (CityDistrictEntity) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember { mutableStateOf(district.name) }
    var description by remember { mutableStateOf(district.description) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Stadtviertel bearbeiten",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Beschreibung") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    onSave(
                        district.copy(
                            name = name,
                            description = description
                        )
                    )
                },
                enabled = name.isNotBlank()
            ) {
                Text("Speichern")
            }

            OutlinedButton(onClick = onCancel) {
                Text("Abbrechen")
            }
        }
    }
}