package de.geier.citymanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
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
    var selectedStylePreset by remember { mutableStateOf("SCIFI") }

    var themeExpanded by remember { mutableStateOf(false) }
    var styleExpanded by remember { mutableStateOf(false) }

    val isNameValid = cityName.isNotBlank()
    val isCodeValid = cityCode.length == 4

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
            isError = !isNameValid,
            modifier = Modifier.fillMaxWidth()
        )

        if (!isNameValid) {
            Text(
                "Bitte Stadtname eingeben",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        /* ---------------- SL Code ---------------- */

        OutlinedTextField(
            value = cityCode,
            onValueChange = {
                if (it.length <= 4) cityCode = it
            },
            label = { Text("SL-Code (4-stellig)") },
            visualTransformation = PasswordVisualTransformation(),
            isError = !isCodeValid,
            modifier = Modifier.fillMaxWidth()
        )

        if (!isCodeValid) {
            Text(
                "Code muss genau 4 Zeichen haben",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        /* ---------------- Font Preset ---------------- */

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

        /* ---------------- Style Preset ---------------- */

        ExposedDropdownMenuBox(
            expanded = styleExpanded,
            onExpandedChange = { styleExpanded = !styleExpanded }
        ) {

            OutlinedTextField(
                value = selectedStylePreset,
                onValueChange = {},
                readOnly = true,
                label = { Text("Stil") },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = styleExpanded,
                onDismissRequest = { styleExpanded = false }
            ) {
                listOf("SCIFI", "FANTASY", "ASIA", "WESTERN").forEach { style ->
                    DropdownMenuItem(
                        text = { Text(style) },
                        onClick = {
                            selectedStylePreset = style
                            styleExpanded = false
                        }
                    )
                }
            }
        }

        /* ---------------- Buttons ---------------- */

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

            Button(
                onClick = {
                    viewModel.createCity(
                        name = cityName.trim(),
                        gameMasterCode = cityCode,
                        backgroundPreset = "WHITE", // 🔥 Fallback (temporär)
                        fontPreset = selectedTheme,
                        stylePreset = selectedStylePreset
                    ) { newId ->
                        onCityCreated(newId)
                    }
                },
                enabled = isNameValid && isCodeValid
            ) {
                Text("Erstellen")
            }

            OutlinedButton(onClick = onCancel) {
                Text("Abbrechen")
            }
        }
    }
}