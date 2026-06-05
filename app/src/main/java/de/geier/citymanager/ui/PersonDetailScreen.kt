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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.material3.AssistChip
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.runtime.saveable.rememberSaveable

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PersonDetailScreen(
    person: Person,
    assignedPois: List<PointOfInterest>,
    assignedFactions: List<Faction>,
    accessContext: AccessContext,
    onBack: () -> Unit,
    onSave: (Person) -> Unit,
    onSavePlayerNotes: (String, String) -> Unit,
    onDelete: (Person) -> Unit,
    onAssignPois: () -> Unit,
    onAssignFactions: () -> Unit,
    onPersonClick: (String) -> Unit,
    onPoiClick: (String) -> Unit,
    onFactionClick: (String) -> Unit,
    onShowOnMap: (String) -> Unit
) {

    var name by rememberSaveable(person.id) {
        mutableStateOf(person.name)
    }
    var description by remember(person.id) { mutableStateOf(person.description) }
    var shortDescription by remember(person.id) {
        mutableStateOf(person.shortDescription)
    }
    var playerNotes by remember(person.id) { mutableStateOf(person.playerNotes) }
    var gameMasterNotes by remember(person.id) { mutableStateOf(person.gameMasterNotes) }
    var playerNotesExpanded by remember(person.id) {
        mutableStateOf(false)
    }

    var gameMasterNotesExpanded by remember(person.id) {
        mutableStateOf(false)
    }
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
                title = { },
                navigationIcon = {
                    IconButton(
                        onClick = {

                            if (canEdit) {

                                onSave(
                                    person.copy(
                                        name = name,
                                        shortDescription = shortDescription,
                                        description = description,
                                        playerNotes = playerNotes,
                                        gameMasterNotes = gameMasterNotes,
                                        visible = visible,
                                        portraitImageUri = imageUri
                                    )
                                )

                            } else {

                                onSavePlayerNotes(
                                    person.id,
                                    playerNotes
                                )
                            }

                            onBack()
                        }
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Schließen"
                        )
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

            /* ---------- Character Header ---------- */

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .width(180.dp)
                        .heightIn(max = 260.dp)
                        .clip(RoundedCornerShape(16.dp))
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
                            modifier = Modifier.size(72.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (canEdit) {

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(0.8f),
                        label = {
                            Text(
                                "Name",
                                fontFamily = FontFamily.SansSerif
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = shortDescription,
                        onValueChange = { shortDescription = it },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(0.8f),
                        label = {
                            Text(
                                "Kurzbeschreibung",
                                fontFamily = FontFamily.SansSerif
                            )
                        }
                    )

                } else {

                    Text(
                        text = name,
                        style = MaterialTheme.typography.headlineSmall
                    )

                    if (shortDescription.isNotBlank()) {

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = shortDescription,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (canEdit) {

                    OutlinedButton(
                        onClick = {
                            imagePickerLauncher.launch(arrayOf("image/*"))
                        }
                    ) {

                        Text(
                            "Bild auswählen",
                            style = MaterialTheme.typography.bodyMedium,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                }
            }

            /* ---------- Karte ---------- */

            OutlinedButton(
                onClick = { onShowOnMap(person.id) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Auf Karte anzeigen",
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = FontFamily.SansSerif
                )
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
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    assignedFactions.forEach { faction ->

                        AssistChip(
                            onClick = {
                                onFactionClick(faction.id)
                            },
                            label = {
                                Text(faction.name)
                            }
                        )
                    }
                }
            }

            if (canEdit) {
                Button(
                    onClick = {

                        onSave(
                            person.copy(
                                name = name,
                                shortDescription = shortDescription,
                                description = description,
                                playerNotes = playerNotes,
                                gameMasterNotes = gameMasterNotes,
                                visible = visible,
                                portraitImageUri = imageUri
                            )
                        )

                        onAssignFactions()
                    }
                ) {
                    Text(
                        "Fraktionen zuweisen",
                        fontFamily = FontFamily.SansSerif
                    )
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
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    assignedPois.forEach { poi ->

                        AssistChip(
                            onClick = {
                                onPoiClick(poi.id)
                            },
                            label = {
                                Text(poi.name)
                            }
                        )
                    }
                }
            }

            if (canEdit) {
                Button(
                    onClick = {

                        onSave(
                            person.copy(
                                name = name,
                                shortDescription = shortDescription,
                                description = description,
                                playerNotes = playerNotes,
                                gameMasterNotes = gameMasterNotes,
                                visible = visible,
                                portraitImageUri = imageUri
                            )
                        )

                        onAssignPois()
                    }
                ) {
                    Text(
                        "Orte zuweisen",
                        fontFamily = FontFamily.SansSerif
                    )
                }
            }

            /* ---------- Notizen ---------- */

            HorizontalDivider()
            Text("Notizen", style = MaterialTheme.typography.titleMedium)

            Text(
                "Spieler-Notizen",
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = playerNotes,
                onValueChange = { playerNotes = it },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4,
                maxLines = if (playerNotesExpanded) 12 else 4
            )
            TextButton(
                onClick = {
                    playerNotesExpanded = !playerNotesExpanded
                }
            ) {
                Text(
                    if (playerNotesExpanded)
                        "Weniger anzeigen"
                    else
                        "Mehr anzeigen"
                )
            }
            if (canEdit) {
                Text(
                    "SL-Notizen",
                    style = MaterialTheme.typography.titleSmall
                )

                Spacer(modifier = Modifier.height(8.dp))

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

                    if (canEdit) {

                        onSave(
                            person.copy(
                                name = name,
                                shortDescription = shortDescription,
                                description = description,
                                playerNotes = playerNotes,
                                gameMasterNotes = gameMasterNotes,
                                visible = visible,
                                portraitImageUri = imageUri
                            )
                        )

                    } else {

                        onSavePlayerNotes(
                            person.id,
                            playerNotes
                        )
                    }

                    onBack()
                }
            ) {
                Text(
                    "Speichern",
                    fontFamily = FontFamily.SansSerif
                )
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