package de.geier.citymanager.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import de.geier.citymanager.data.entity.CityDistrictEntity
import de.geier.citymanager.ui.viewmodel.CityViewModel
import java.util.UUID

@Composable
fun CityDistrictListScreen(
    cityId: String,
    cityViewModel: CityViewModel,
    accessContext: AccessContext,
    onDistrictSelected: (String) -> Unit
) {
    val districts by cityViewModel
        .districtsForCity(cityId)
        .collectAsState(initial = emptyList())

    val city by cityViewModel.city.collectAsState()

    var showCreate by remember { mutableStateOf(false) }
    var selectedDistrict by remember { mutableStateOf<CityDistrictEntity?>(null) }

    var showCityImageFullscreen by remember { mutableStateOf(false) }

    val cityImagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            city?.let { currentCity ->
                cityViewModel.saveCity(
                    currentCity.copy(backgroundImageUri = it.toString())
                )
            }
        }
    }

    when {

        showCreate -> {
            CreateDistrictScreen(
                onSave = { name: String, description: String ->
                    cityViewModel.saveDistrict(
                        CityDistrictEntity(
                            id = UUID.randomUUID().toString(),
                            cityId = cityId,
                            name = name,
                            description = description,
                            orderIndex = districts.size,
                            mapKey = null,
                            imageUri = null
                        )
                    )
                    showCreate = false
                },
                onCancel = { showCreate = false }
            )
        }

        selectedDistrict != null -> {
            DistrictDetailPanel(
                district = selectedDistrict!!,
                accessContext = accessContext,
                onBack = { selectedDistrict = null },
                onSave = {
                    cityViewModel.saveDistrict(it)
                    selectedDistrict = it
                }
            )
        }

        else -> {

            Scaffold(
                containerColor = Color.Transparent,
                floatingActionButton = {
                    if (accessContext.canEdit()) {
                        FloatingActionButton(
                            onClick = { showCreate = true }
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null)
                        }
                    }
                }
            ) { padding ->

                // 🔥 GLOBALER FIX
                CompositionLocalProvider(
                    LocalContentColor provides MaterialTheme.colorScheme.onBackground
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                    ) {

                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentAlignment = Alignment.Center
                            ) {

                                if (city?.backgroundImageUri != null) {
                                    AsyncImage(
                                        model = city!!.backgroundImageUri,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clickable {
                                                showCityImageFullscreen = true
                                            }
                                    )
                                } else {
                                    Text("Kein Stadtviertel-Bild gesetzt")
                                }

                                if (accessContext.canEdit()) {
                                    IconButton(
                                        onClick = { cityImagePicker.launch("image/*") },
                                        modifier = Modifier.align(Alignment.BottomEnd)
                                    ) {
                                        Icon(Icons.Default.Image, contentDescription = null)
                                    }
                                }
                            }

                            if (districts.isEmpty()) {

                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("Für diese Stadt sind noch keine Stadtviertel angelegt.")
                                }

                            } else {

                                LazyVerticalGrid(
                                    columns = GridCells.Adaptive(minSize = 140.dp),
                                    modifier = Modifier.fillMaxSize(),
                                    contentPadding = PaddingValues(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    items(districts, key = { it.id }) { district ->
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    selectedDistrict = district
                                                }
                                        ) {

                                            Column {

                                                if (district.imageUri != null) {
                                                    AsyncImage(
                                                        model = district.imageUri,
                                                        contentDescription = null,
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .height(100.dp)
                                                    )
                                                }

                                                Column(modifier = Modifier.padding(12.dp)) {
                                                    Text(
                                                        text = district.name,
                                                        style = MaterialTheme.typography.titleMedium
                                                    )

                                                    if (district.description.isNotBlank()) {
                                                        Spacer(modifier = Modifier.height(4.dp))
                                                        Text(
                                                            text = district.description,
                                                            maxLines = 2
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }


                            if (showCityImageFullscreen && city?.backgroundImageUri != null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black)
                                    .clickable { showCityImageFullscreen = false },
                                contentAlignment = Alignment.Center
                            ) {
                                AsyncImage(
                                    model = city!!.backgroundImageUri,
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DistrictDetailPanel(
    district: CityDistrictEntity,
    accessContext: AccessContext,
    onBack: () -> Unit,
    onSave: (CityDistrictEntity) -> Unit
) {
    var editMode by remember { mutableStateOf(false) }
    var showImageFullscreen by remember { mutableStateOf(false) }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            onSave(district.copy(imageUri = it.toString()))
        }
    }

    if (!accessContext.canEdit()) {
        editMode = false
    }

    if (editMode) {
        EditDistrictScreen(
            district = district,
            onSave = {
                onSave(it)
                editMode = false
            },
            onCancel = { editMode = false }
        )
        return
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            if (district.imageUri != null) {
                AsyncImage(
                    model = district.imageUri,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clickable {
                            showImageFullscreen = true
                        }
                )
            }

            if (accessContext.canEdit()) {
                IconButton(onClick = { imagePicker.launch("image/*") }) {
                    Icon(Icons.Default.Image, contentDescription = null)
                }
            }

            Text(
                text = district.name,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = district.description,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

                if (accessContext.canEdit()) {
                    Button(onClick = { editMode = true }) {
                        Text("Bearbeiten")
                    }
                }

                OutlinedButton(onClick = onBack) {
                    Text("Zurück")
                }
            }
        }

        if (showImageFullscreen && district.imageUri != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .clickable { showImageFullscreen = false },
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = district.imageUri,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
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
            "Stadtviertel bearbeiten",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Beschreibung") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = {
                    onSave(district.copy(name = name, description = description))
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

@Composable
private fun CreateDistrictScreen(
    onSave: (String, String) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            "Stadtviertel erstellen",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Beschreibung") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = { onSave(name, description) },
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