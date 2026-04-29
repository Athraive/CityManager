@file:OptIn(ExperimentalMaterial3Api::class)

package de.geier.citymanager.ui

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import de.geier.citymanager.ui.viewmodel.CityViewModel

@Composable
fun CityIntroScreen(
    cityViewModel: CityViewModel? = null,
    accessContext: AccessContext? = null
) {

    if (cityViewModel == null || accessContext == null) {
        Text("Keine Daten verfügbar")
        return
    }

    val city by cityViewModel.city.collectAsState()
    val canEdit = accessContext.canEdit()

    if (city == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val c = city!!

    /* ---------- STATE ---------- */

    var name by remember(c.id) { mutableStateOf(c.name) }
    var country by remember(c.id) { mutableStateOf(c.country) }
    var region by remember(c.id) { mutableStateOf(c.region) }
    var language by remember(c.id) { mutableStateOf(c.language) }
    var government by remember(c.id) { mutableStateOf(c.government) }
    var elevation by remember(c.id) { mutableStateOf(c.elevation) }
    var area by remember(c.id) { mutableStateOf(c.area) }
    var population by remember(c.id) { mutableStateOf(c.population) }
    var description by remember(c.id) { mutableStateOf(c.description) }

    var coatOfArmsUri by remember(c.id) {
        mutableStateOf(c.coatOfArmsUri)
    }

    val context = LocalContext.current

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            coatOfArmsUri = it.toString()
        }
    }

    /* ---------- UI ---------- */

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        /* ---------- HEADER ---------- */

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            if (canEdit) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Stadtname") },
                    modifier = Modifier.weight(1f)
                )
            } else {
                Text(
                    text = name,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(horizontalAlignment = Alignment.End) {

                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    if (coatOfArmsUri != null) {
                        AsyncImage(
                            model = coatOfArmsUri,
                            contentDescription = "Wappen",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    } else {
                        Icon(
                            Icons.Default.LocationCity,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }

                if (canEdit) {
                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            imagePicker.launch(arrayOf("image/*"))
                        }
                    ) {
                        Text("Wappen wählen")
                    }
                }
            }
        }

        /* ---------- FIELDS ---------- */

        EditableField("Land", country, canEdit) { country = it }
        EditableField("Region", region, canEdit) { region = it }
        EditableField("Sprache", language, canEdit) { language = it }
        EditableField("Regierung", government, canEdit) { government = it }
        EditableField("Höhe", elevation, canEdit) { elevation = it }
        EditableField("Fläche", area, canEdit) { area = it }
        EditableField("Einwohner", population, canEdit) { population = it }

        /* ---------- DESCRIPTION ---------- */

        HorizontalDivider()

        Text("Beschreibung", style = MaterialTheme.typography.titleMedium)

        if (canEdit) {
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4
            )
        } else if (description.isNotBlank()) {
            Text(description, style = MaterialTheme.typography.bodyMedium)
        }

        /* ---------- SAVE ---------- */

        if (canEdit) {
            Button(
                onClick = {
                    cityViewModel.saveCity(
                        c.copy(
                            name = name,
                            country = country,
                            region = region,
                            language = language,
                            government = government,
                            elevation = elevation,
                            area = area,
                            population = population,
                            description = description,
                            coatOfArmsUri = coatOfArmsUri
                        )
                    )
                }
            ) {
                Text("Speichern")
            }
        }
    }
}

/* ---------- Helper ---------- */

@Composable
private fun EditableField(
    label: String,
    value: String,
    canEdit: Boolean,
    onChange: (String) -> Unit
) {
    if (canEdit) {
        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth()
        )
    } else if (value.isNotBlank()) {

        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}