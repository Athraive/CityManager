@file:OptIn(ExperimentalMaterial3Api::class)

package de.geier.citymanager.ui

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.ui.text.font.FontFamily

@Composable
fun FactionDetailScreen(
    faction: Faction,
    assignedPersons: List<Person>,
    assignedPois: List<PointOfInterest>,
    accessContext: AccessContext,
    onBack: () -> Unit,
    onSave: (Faction) -> Unit,
    onDelete: (Faction) -> Unit,
    onPersonClick: (String) -> Unit,
    onPoiClick: (String) -> Unit
) {

    var name by remember(faction.id) { mutableStateOf(faction.name) }
    var description by remember(faction.id) { mutableStateOf(faction.description ?: "") }
    var playerNotes by remember(faction.id) { mutableStateOf(faction.playerNotes) }
    var gameMasterNotes by remember(faction.id) { mutableStateOf(faction.gameMasterNotes) }
    var visible by remember(faction.id) { mutableStateOf(faction.visible) }

    var imageUri by remember(faction.id) {
        mutableStateOf(faction.imageUri)
    }

    val displayImage = imageUri ?: faction.imageUri

    var showDeleteConfirm by remember { mutableStateOf(false) }

    val canEdit = accessContext.canEdit()

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
                title = { Text(name.ifBlank { "Fraktion" }) },
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

                Column(horizontalAlignment = Alignment.End) {

                    Box(
                        modifier = Modifier
                            .width(140.dp)
                            .heightIn(max = 220.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        if (displayImage != null) {
                            AsyncImage(
                                model = displayImage,
                                contentDescription = "Fraktionsbild",
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
                            Text("Bild auswählen",
                                fontFamily = FontFamily.SansSerif)
                        }
                    }
                }
            }

            /* ---------- Beschreibung ---------- */

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

            /* ---------- Personen ---------- */

            HorizontalDivider()
            Text("Personen", style = MaterialTheme.typography.titleMedium)

            if (assignedPersons.isEmpty()) {
                Text("Keine Personen zugeordnet", color = MaterialTheme.colorScheme.onSurfaceVariant)
            } else {
                assignedPersons.forEach {
                    Text(
                        text = "• ${it.name}",
                        modifier = Modifier.clickable { onPersonClick(it.id) }
                    )
                }
            }

            /* ---------- POIs ---------- */

            HorizontalDivider()
            Text("Orte", style = MaterialTheme.typography.titleMedium)

            if (assignedPois.isEmpty()) {
                Text("Keine Orte zugeordnet", color = MaterialTheme.colorScheme.onSurfaceVariant)
            } else {
                assignedPois.forEach {
                    Text(
                        text = "• ${it.name}",
                        modifier = Modifier.clickable { onPoiClick(it.id) }
                    )
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
                        faction.copy(
                            name = name,
                            description = description.takeIf { it.isNotBlank() },
                            playerNotes = playerNotes,
                            gameMasterNotes = gameMasterNotes,
                            visible = visible,
                            imageUri = imageUri
                        )
                    )
                    onBack()
                }
            ) {
                Text("Speichern",
                    fontFamily = FontFamily.SansSerif)
            }
        }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Fraktion löschen?") },
            text = { Text("Möchtest du diese Fraktion wirklich löschen?") },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    onClick = {
                        onDelete(faction)
                        showDeleteConfirm = false
                        onBack()
                    }
                ) {
                    Text("Löschen",
                        fontFamily = FontFamily.SansSerif)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeleteConfirm = false }) {
                    Text("Abbrechen",
                        fontFamily = FontFamily.SansSerif)
                }
            }
        )
    }
}