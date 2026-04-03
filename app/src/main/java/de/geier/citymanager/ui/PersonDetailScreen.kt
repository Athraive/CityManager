@file:OptIn(ExperimentalMaterial3Api::class)

package de.geier.citymanager.ui

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.filled.Person

@Composable
fun PersonDetailScreen(
    person: Person,
    assignedPois: List<PointOfInterest>,
    assignedFactions: List<Faction>,
    accessContext: AccessContext,
    onBack: () -> Unit,
    onSave: (Person) -> Unit,
    onDelete: (Person) -> Unit,
    onAssignPois: () -> Unit,
    onAssignFactions: () -> Unit,
    onPersonClick: (String) -> Unit,
    onPoiClick: (String) -> Unit,
    onFactionClick: (String) -> Unit,
    onShowOnMap: (String) -> Unit
) {

    var name by remember(person.id) { mutableStateOf(person.name) }
    var description by remember(person.id) { mutableStateOf(person.description) }
    var playerNotes by remember(person.id) { mutableStateOf(person.playerNotes) }
    var gameMasterNotes by remember(person.id) { mutableStateOf(person.gameMasterNotes) }
    var visible by remember(person.id) { mutableStateOf(person.visible) }

    // ✅ NEU: Bild-State
    var imageUri by remember(person.id) {
        mutableStateOf(person.portraitImageUri)
    }

    var showDeleteConfirm by remember { mutableStateOf(false) }

    val canEdit = accessContext.canEdit()

    // ✅ NEU: ImagePicker
    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            imageUri = it.toString()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(name.ifBlank { "Person" }) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.Close, contentDescription = "Schließen")
                    }
                },
                actions = {
                    if (canEdit) {
                        IconButton(onClick = { showDeleteConfirm = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Löschen")
                        }
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            if (canEdit) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            } else {
                Text(name, style = MaterialTheme.typography.titleLarge)
            }

            /* ---------- Bild ---------- */

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Spacer(modifier = Modifier.weight(1f))

                Column(
                    horizontalAlignment = Alignment.End
                ) {

                    Box(
                        modifier = Modifier
                            .width(140.dp)
                            .heightIn(max = 220.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        if (imageUri != null) {
                            AsyncImage(
                                model = imageUri,
                                contentDescription = "Person Bild",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(64.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (canEdit) {
                        Button(
                            onClick = { imagePickerLauncher.launch(arrayOf("image/*")) }
                        ) {
                            Text("Bild auswählen")
                        }
                    }
                }
            }

            /* ---------- Karte ---------- */

            Button(
                onClick = { onShowOnMap(person.id) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Auf Karte anzeigen")
            }

            Text("Beschreibung", style = MaterialTheme.typography.titleMedium)

            if (canEdit) {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            } else if (description.isNotBlank()) {
                Text(description)
            }

            if (canEdit) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Checkbox(
                        checked = visible,
                        onCheckedChange = { visible = it }
                    )
                    Text("Für Spieler sichtbar")
                }
            }

            /* ---------- Fraktionen ---------- */

            HorizontalDivider()
            Text("Fraktionen", style = MaterialTheme.typography.titleMedium)

            if (assignedFactions.isEmpty()) {
                Text(
                    "Keine Fraktionen zugewiesen",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                assignedFactions.forEach { faction ->
                    Text(
                        text = "• ${faction.name}",
                        modifier = Modifier.clickable {
                            onFactionClick(faction.id)
                        }
                    )
                }
            }

            if (canEdit) {
                Button(onClick = onAssignFactions) {
                    Text("Fraktionen zuweisen")
                }
            }

            /* ---------- Orte ---------- */

            HorizontalDivider()
            Text("Orte", style = MaterialTheme.typography.titleMedium)

            if (assignedPois.isEmpty()) {
                Text(
                    "Keine Orte zugewiesen",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                assignedPois.forEach { poi ->
                    Text(
                        text = "• ${poi.name}",
                        modifier = Modifier.clickable {
                            onPoiClick(poi.id)
                        }
                    )
                }
            }

            if (canEdit) {
                Button(onClick = onAssignPois) {
                    Text("Orte zuweisen")
                }
            }

            /* ---------- Notizen ---------- */

            HorizontalDivider()
            Text("Notizen", style = MaterialTheme.typography.titleMedium)

            Text("Spieler-Notizen")
            OutlinedTextField(
                value = playerNotes,
                onValueChange = { playerNotes = it },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4
            )

            if (canEdit) {
                Text("SL-Notizen")
                OutlinedTextField(
                    value = gameMasterNotes,
                    onValueChange = { gameMasterNotes = it },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4
                )
            }

            Button(
                enabled = name.isNotBlank(),
                onClick = {
                    onSave(
                        person.copy(
                            name = name,
                            description = description,
                            playerNotes = playerNotes,
                            gameMasterNotes = gameMasterNotes,
                            visible = visible,
                            portraitImageUri = imageUri   // ✅ ENTSCHEIDEND
                        )
                    )
                    onBack()
                }
            ) {
                Text("Speichern")
            }
        }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Person löschen?") },
            text = { Text("Möchtest du diese Person wirklich löschen?") },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    onClick = {
                        onDelete(person)
                        showDeleteConfirm = false
                        onBack()
                    }
                ) {
                    Text("Löschen")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeleteConfirm = false }) {
                    Text("Abbrechen")
                }
            }
        )
    }
}