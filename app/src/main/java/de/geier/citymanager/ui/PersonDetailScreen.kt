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
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.delay

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

                val hasMapPosition =
                    person.mapX != null &&
                            person.mapY != null

                Box(
                    modifier = Modifier
                        .width(180.dp)
                        .heightIn(max = 260.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable(enabled = canEdit) {
                            imagePickerLauncher.launch(arrayOf("image/*"))
                        },
                    contentAlignment = Alignment.Center
                ) {

                    if (imageUri != null) {

                        AsyncImage(
                            model = imageUri,
                            contentDescription = "Person Bild",
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
                                onShowOnMap(person.id)
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

            /* ---------- Beschreibung ---------- */

            if (canEdit) {

                Text(
                    "Beschreibung",
                    style = MaterialTheme.typography.titleLarge
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

            } else if (description.isNotBlank()) {

                Text(description)
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

                    Text("Fraktionen zuweisen")
                }
            }

            /* ---------- Orte ---------- */

            HorizontalDivider()

            Text(
                "Orte",
                style = MaterialTheme.typography.titleLarge
            )

            if (assignedPois.isEmpty()) {

                Text(
                    "–",
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

                OutlinedButton(
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

                    Text("Orte zuweisen")
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
                    "Speichern"
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