package de.geier.citymanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.geier.citymanager.ui.background.CityBackgroundPresets
import de.geier.citymanager.ui.background.CityThemePresets
import de.geier.citymanager.ui.viewmodel.CitySelectViewModel
import de.geier.citymanager.ui.viewmodel.CitySelectViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCityScreen(
    onCityCreated: (String) -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current

    val viewModel: CitySelectViewModel = viewModel(
        factory = CitySelectViewModelFactory(context)
    )

    var cityName by remember { mutableStateOf("") }
    var cityCode by remember { mutableStateOf("") }

    var selectedTheme by remember { mutableStateOf("DEFAULT") }
    var selectedBackgroundPreset by remember { mutableStateOf("WHITE") }

    var themeExpanded by remember { mutableStateOf(false) }
    var bgExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Neue Stadt anlegen",
            style = MaterialTheme.typography.headlineMedium
        )

        /* ---------------- Name ---------------- */

        OutlinedTextField(
            value = cityName,
            onValueChange = { cityName = it },
            label = { Text("Stadtname") },
            modifier = Modifier.fillMaxWidth()
        )

        /* ---------------- SL Code ---------------- */

        OutlinedTextField(
            value = cityCode,
            onValueChange = {
                if (it.length <= 4) cityCode = it
            },
            label = { Text("SL-Code (4-stellig)") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        /* ---------------- Font Preset Dropdown ---------------- */

        ExposedDropdownMenuBox(
            expanded = themeExpanded,
            onExpandedChange = { themeExpanded = !themeExpanded }
        ) {

            OutlinedTextField(
                value = selectedTheme,
                onValueChange = {},
                readOnly = true,
                label = { Text("Schriftstil") },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = themeExpanded,
                onDismissRequest = { themeExpanded = false }
            ) {
                CityThemePresets.presets.forEach { theme ->
                    DropdownMenuItem(
                        text = { Text(theme) },
                        onClick = {
                            selectedTheme = theme
                            themeExpanded = false
                        }
                    )
                }
            }
        }

        /* ---------------- Background Preset Dropdown ---------------- */

        ExposedDropdownMenuBox(
            expanded = bgExpanded,
            onExpandedChange = { bgExpanded = !bgExpanded }
        ) {

            OutlinedTextField(
                value = selectedBackgroundPreset,
                onValueChange = {},
                readOnly = true,
                label = { Text("Hintergrund (Preset)") },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = bgExpanded,
                onDismissRequest = { bgExpanded = false }
            ) {

                CityBackgroundPresets.presets.forEach { preset ->
                    DropdownMenuItem(
                        text = { Text(preset) },
                        onClick = {
                            selectedBackgroundPreset = preset
                            bgExpanded = false
                        }
                    )
                }
            }
        }

        /* ---------------- Buttons ---------------- */

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

            Button(
                onClick = {
                    if (cityName.isNotBlank() && cityCode.length == 4) {

                        viewModel.createCity(
                            name = cityName.trim(),
                            gameMasterCode = cityCode,
                            backgroundPreset = selectedBackgroundPreset,
                            fontPreset = selectedTheme
                        ) { newId ->
                            onCityCreated(newId)
                        }
                    }
                }
            ) {
                Text("Erstellen")
            }

            OutlinedButton(onClick = onCancel) {
                Text("Abbrechen")
            }
        }
    }
}
