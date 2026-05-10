package de.geier.citymanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.geier.citymanager.data.entity.CityEntity
import de.geier.citymanager.ui.background.CityThemePresets
import de.geier.citymanager.ui.theme.CityStylePreset
import de.geier.citymanager.ui.viewmodel.CitySelectViewModel
import de.geier.citymanager.ui.viewmodel.CitySelectViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCityScreen(
    onCityCreated: (String) -> Unit,
    onCancel: () -> Unit,

    existingCity: CityEntity? = null,

    onCityUpdated: (() -> Unit)? = null
) {

    val context = LocalContext.current

    val viewModel: CitySelectViewModel = viewModel(
        factory = CitySelectViewModelFactory(context)
    )

    var cityName by remember(existingCity) {
        mutableStateOf(existingCity?.name ?: "")
    }

    var cityCode by remember(existingCity) {
        mutableStateOf(existingCity?.gameMasterCode ?: "")
    }

    var selectedTheme by remember(existingCity) {
        mutableStateOf(existingCity?.fontPreset ?: "SCIFI")
    }

    var selectedStylePreset by remember(existingCity) {
        mutableStateOf(
            existingCity?.stylePreset
                ?: CityStylePreset.URBAN_GREY.name
        )
    }

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
            text =
                if (existingCity == null)
                    "Neue Stadt anlegen"
                else
                    "Stadt bearbeiten",

            style = MaterialTheme.typography.headlineMedium
        )

        /* ---------------- Name ---------------- */

        OutlinedTextField(
            value = cityName,

            onValueChange = {
                cityName = it
            },

            label = {
                Text("Stadtname")
            },

            isError = !isNameValid,

            modifier = Modifier.fillMaxWidth()
        )

        if (!isNameValid) {

            Text(
                text = "Bitte Stadtname eingeben",

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

            label = {
                Text("SL-Code (4-stellig)")
            },

            visualTransformation =
                PasswordVisualTransformation(),

            isError = !isCodeValid,

            modifier = Modifier.fillMaxWidth()
        )

        if (!isCodeValid) {

            Text(
                text = "Code muss genau 4 Zeichen haben",

                color = MaterialTheme.colorScheme.error,

                style = MaterialTheme.typography.bodySmall
            )
        }

        /* ---------------- Font Preset ---------------- */

        ExposedDropdownMenuBox(
            expanded = themeExpanded,

            onExpandedChange = {
                themeExpanded = !themeExpanded
            }
        ) {

            OutlinedTextField(

                value = when (selectedTheme) {

                    "SCIFI" -> "Cyber Tech"

                    "WESTERN" -> "Frontier"

                    "ASIA" -> "Sakura"

                    "MEDIEVAL" -> "Kingdom"

                    else -> selectedTheme
                },

                onValueChange = {},

                readOnly = true,

                label = {
                    Text("Schriftstil")
                },

                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = themeExpanded,

                onDismissRequest = {
                    themeExpanded = false
                }
            ) {

                CityThemePresets.presets
                    .filter { it != "modern" }
                    .forEach { theme ->

                        val displayName = when (theme) {

                            "scifi" -> "Cyber Tech"

                            "western" -> "Frontier"

                            "asia" -> "Sakura"

                            "medieval" -> "Kingdom"

                            else -> theme
                        }

                        DropdownMenuItem(

                            text = {
                                Text(displayName)
                            },

                            onClick = {

                                selectedTheme = when (theme) {

                                    "scifi" -> "SCIFI"

                                    "western" -> "WESTERN"

                                    "asia" -> "ASIA"

                                    "medieval" -> "MEDIEVAL"

                                    else -> "SCIFI"
                                }

                                themeExpanded = false
                            }
                        )
                    }
            }
        }

        /* ---------------- Style Preset ---------------- */

        ExposedDropdownMenuBox(
            expanded = styleExpanded,

            onExpandedChange = {
                styleExpanded = !styleExpanded
            }
        ) {

            OutlinedTextField(

                value = when (selectedStylePreset) {

                    CityStylePreset.URBAN_GREY.name ->
                        "Urban Grey"

                    CityStylePreset.PARCHEMENT.name ->
                        "Old Parchement"

                    CityStylePreset.BLOSSOM.name ->
                        "Cherry Blossom"

                    CityStylePreset.DUST.name ->
                        "Dusty Road"

                    CityStylePreset.FILM_NOIR.name ->
                        "Film Noir"

                    CityStylePreset.NEON_MATRIX.name ->
                        "Neon Matrix"

                    else -> selectedStylePreset
                },

                onValueChange = {},

                readOnly = true,

                label = {
                    Text("Farbschema")
                },

                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = styleExpanded,

                onDismissRequest = {
                    styleExpanded = false
                }
            ) {

                CityStylePreset.entries.forEach { style ->

                    DropdownMenuItem(

                        text = {

                            Text(
                                when (style) {

                                    CityStylePreset.URBAN_GREY ->
                                        "Urban Grey"

                                    CityStylePreset.PARCHEMENT ->
                                        "Old Parchement"

                                    CityStylePreset.BLOSSOM ->
                                        "Cherry Blossom"

                                    CityStylePreset.DUST ->
                                        "Dusty Road"

                                    CityStylePreset.FILM_NOIR ->
                                        "Film Noir"

                                    CityStylePreset.NEON_MATRIX ->
                                        "Neon Matrix"
                                }
                            )
                        },

                        onClick = {

                            selectedStylePreset = style.name
                            styleExpanded = false
                        }
                    )
                }
            }
        }

        /* ---------------- Buttons ---------------- */

        Row(
            horizontalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            Button(

                onClick = {

                    if (existingCity == null) {

                        /* ---------------- CREATE ---------------- */

                        viewModel.createCity(
                            name = cityName.trim(),

                            gameMasterCode = cityCode,

                            backgroundPreset = "WHITE",

                            fontPreset = selectedTheme,

                            stylePreset = selectedStylePreset

                        ) { newId ->

                            onCityCreated(newId)
                        }

                    } else {

                        /* ---------------- UPDATE ---------------- */

                        viewModel.saveCity(
                            existingCity.copy(
                                name = cityName.trim(),

                                gameMasterCode = cityCode,

                                fontPreset = selectedTheme,

                                stylePreset = selectedStylePreset
                            )
                        )

                        onCityUpdated?.invoke()
                    }
                },

                enabled = isNameValid && isCodeValid
            ) {

                Text(
                    if (existingCity == null)
                        "Erstellen"
                    else
                        "Speichern"
                )
            }

            OutlinedButton(
                onClick = onCancel
            ) {

                Text("Abbrechen")
            }
        }
    }
}