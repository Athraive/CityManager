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
import androidx.compose.material.icons.filled.Place
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.ui.text.style.TextAlign
import de.geier.citymanager.ui.components.ImageViewerDialog
import kotlinx.coroutines.delay

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PoiDetailScreen(
    poi: PointOfInterest,
    assignedFactions: List<Faction>,
    assignedPersons: List<Person>,
    accessContext: AccessContext,
    onBack: () -> Unit,
    onSave: (PointOfInterest) -> Unit,
    onDelete: (PointOfInterest) -> Unit,
    onAssignFactions: (PointOfInterest) -> Unit,
    onPersonClick: (String) -> Unit,
    onFactionClick: (String) -> Unit,
    onShowOnMap: (String) -> Unit
) {

    var name by remember(poi.id) { mutableStateOf(poi.name) }
    var shortDescription by remember {
        mutableStateOf(poi.shortDescription)
    }
    var description by remember(poi.id) { mutableStateOf(poi.description ?: "") }
    var visible by remember(poi.id) { mutableStateOf(poi.visible) }
    var playerNotes by remember(poi.id) { mutableStateOf(poi.playerNotes) }
    var gameMasterNotes by remember(poi.id) { mutableStateOf(poi.gameMasterNotes) }

    // 🔥 Wichtig: lokaler State + Fallback auf POI
    var imageUri by remember(poi.id) {
        mutableStateOf(poi.imageUri)
    }

    var openedImage by remember {
        mutableStateOf<String?>(null)
    }
    var playerNotesExpanded by remember {
        mutableStateOf(false)
    }

    var gameMasterNotesExpanded by remember {
        mutableStateOf(false)
    }

    val displayImage = imageUri

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
    var visibilityMessage by remember {
        mutableStateOf<String?>(null)
    }
    LaunchedEffect(visibilityMessage) {

        if (visibilityMessage != null) {

            delay(2000)

            visibilityMessage = null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(name.ifBlank { "Ort" }) },
                navigationIcon = {
                    IconButton(
                        onClick = {

                            if (canEdit) {

                                onSave(
                                    poi.copy(
                                        name = name,
                                        shortDescription = shortDescription,
                                        description = description.takeIf { it.isNotBlank() },
                                        visible = visible,
                                        playerNotes = playerNotes,
                                        gameMasterNotes = gameMasterNotes,
                                        imageUri = imageUri
                                    )
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

        if (openedImage != null) {

            ImageViewerDialog(
                imageUri = openedImage!!,
                onDismiss = {
                    openedImage = null
                }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {



            /* ---------- Header ---------- */

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                val hasMapPosition =
                    poi.mapX != null &&
                            poi.mapY != null

                Box(
                    modifier = Modifier
                        .width(280.dp)
                        .height(180.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable {
                            if (canEdit) {
                                imagePickerLauncher.launch(arrayOf("image/*"))
                            } else if (imageUri != null) {
                                openedImage = imageUri
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {

                    if (displayImage != null) {

                        AsyncImage(
                            model = displayImage,
                            contentDescription = "POI Bild",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                    } else {

                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(72.dp)
                        )
                    }

                    if (canEdit && imageUri != null) {

                        Surface(
                            onClick = {
                                imageUri = null
                            },
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(8.dp),
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                            tonalElevation = 2.dp
                        ) {

                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Bild entfernen",
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }

                    if (canEdit) {

                        Surface(
                            onClick = {

                                visible = !visible

                                visibilityMessage =
                                    if (visible)
                                        "\"$name\" ist für Spieler sichtbar"
                                    else
                                        "\"$name\" ist für Spieler unsichtbar"
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp),
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                            tonalElevation = 2.dp
                        ) {

                            Icon(
                                imageVector =
                                    if (visible)
                                        Icons.Default.Visibility
                                    else
                                        Icons.Default.VisibilityOff,
                                contentDescription = null,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }

                    if (hasMapPosition) {

                        Surface(
                            onClick = {
                                onShowOnMap(poi.id)
                            },
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(8.dp),
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                            tonalElevation = 2.dp
                        ) {

                            Icon(
                                imageVector = Icons.Default.Place,
                                contentDescription = "Auf Karte anzeigen",
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }

                    if (visibilityMessage != null) {

                        Surface(
                            modifier = Modifier.align(Alignment.Center),
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                            tonalElevation = 4.dp
                        ) {

                            Text(
                                text = visibilityMessage!!,
                                modifier = Modifier.padding(
                                    horizontal = 16.dp,
                                    vertical = 10.dp
                                ),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                if (!canEdit) {

                    Spacer(modifier = Modifier.height(16.dp))

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
            }

            if (canEdit) {

                Text(
                    "Name",
                    style = MaterialTheme.typography.titleMedium
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    "Kurzbeschreibung",
                    style = MaterialTheme.typography.titleMedium
                )

                OutlinedTextField(
                    value = shortDescription,
                    onValueChange = { shortDescription = it },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            /* ---------- Beschreibung ---------- */

            Text(
                "Beschreibung",
                style = MaterialTheme.typography.titleLarge
            )

            if (canEdit) {

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

            } else {

                if (description.isNotBlank()) {

                    Text(description)

                } else {

                    Text(
                        text = "Keine Beschreibung vorhanden",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }


            /* ---------- Fraktionen ---------- */

            HorizontalDivider()

            Text(
                "Fraktionen",
                style = MaterialTheme.typography.titleLarge
            )

            if (assignedFactions.isEmpty()) {

                Text(
                    "–",
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

                OutlinedButton(
                    onClick = {
                        onAssignFactions(
                            poi.copy(
                                name = name,
                                shortDescription = shortDescription,
                                description = description.takeIf { it.isNotBlank() },
                                visible = visible,
                                playerNotes = playerNotes,
                                gameMasterNotes = gameMasterNotes,
                                imageUri = imageUri
                            )
                        )
                    }
                ) {

                    Text("Fraktionen zuweisen")
                }
            }

            /* ---------- Personen ---------- */

            HorizontalDivider()

            Text(
                "Personen",
                style = MaterialTheme.typography.titleLarge
            )

            if (assignedPersons.isEmpty()) {

                Text(
                    "–",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

            } else {

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    assignedPersons.forEach { person ->

                        AssistChip(
                            onClick = {
                                onPersonClick(person.id)
                            },
                            label = {
                                Text(person.name)
                            }
                        )
                    }
                }
            }

            /* ---------- Notizen ---------- */

            HorizontalDivider()

            Text(
                "Notizen",
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                "Spieler-Notizen",
                style = MaterialTheme.typography.titleMedium
            )


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

            HorizontalDivider()

            if (canEdit) {

                Text(
                    "SL-Notizen",
                    style = MaterialTheme.typography.titleMedium
                )


                OutlinedTextField(
                    value = gameMasterNotes,
                    onValueChange = { gameMasterNotes = it },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4,
                    maxLines = if (gameMasterNotesExpanded) 12 else 4
                )

                TextButton(
                    onClick = {
                        gameMasterNotesExpanded = !gameMasterNotesExpanded
                    }
                ) {
                    Text(
                        if (gameMasterNotesExpanded)
                            "Weniger anzeigen"
                        else
                            "Mehr anzeigen"
                    )
                }
            }

            Button(
                enabled = name.isNotBlank(),
                onClick = {
                    onSave(
                        poi.copy(
                            name = name,
                            shortDescription = shortDescription,
                            description = description.takeIf { it.isNotBlank() },
                            visible = visible,
                            playerNotes = playerNotes,
                            gameMasterNotes = gameMasterNotes,
                            imageUri = imageUri
                        )
                    )
                    onBack()
                }
            ) {
                Text("Speichern",
                    style = MaterialTheme.typography.bodyMedium)
            }
        }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Ort löschen?") },
            text = { Text("Möchtest du den Ort wirklich löschen?") },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    onClick = {
                        onDelete(poi)
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