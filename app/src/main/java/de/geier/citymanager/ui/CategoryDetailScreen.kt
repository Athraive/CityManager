@file:OptIn(ExperimentalMaterial3Api::class)

package de.geier.citymanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontFamily

@Composable
fun CategoryDetailScreen(
    category: PoiCategory,
    poiCountInCategory: Int,
    onBack: () -> Unit,
    accessContext: AccessContext,
    onSave: (PoiCategory) -> Unit,
    onDelete: (PoiCategory) -> Unit
) {

    var title by remember(category.id) {
        mutableStateOf(category.title)
    }

    var description by remember(category.id) {
        mutableStateOf(category.description ?: "")
    }

    var visible by remember(category.id) {
        mutableStateOf(category.visible)
    }

    var showDeleteConfirm by remember {
        mutableStateOf(false)
    }

    var showDeleteBlocked by remember {
        mutableStateOf(false)
    }

    val canEdit = accessContext.canEdit()

    Scaffold(
        containerColor = Color.Transparent,

        topBar = {

            TopAppBar(

                title = {
                    Text(title.ifBlank { "Neue Kategorie" })
                },

                navigationIcon = {

                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Schließen"
                        )
                    }
                },

                actions = {

                    if (canEdit) {

                        IconButton(
                            onClick = {

                                if (poiCountInCategory == 0) {
                                    showDeleteConfirm = true
                                } else {
                                    showDeleteBlocked = true
                                }
                            }
                        ) {

                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Löschen"
                            )
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

            /* ---------- Titel ---------- */

            if (canEdit) {

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },

                    label = {
                        Text("Name")
                    },

                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

            } else {

                Text(
                    title,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            /* ---------- Beschreibung ---------- */

            Text(
                "Beschreibung",
                style = MaterialTheme.typography.titleMedium
            )

            if (canEdit) {

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )

            } else if (description.isNotBlank()) {

                Text(description)
            }

            /* ---------- Sichtbarkeit ---------- */

            if (canEdit) {

                Row(
                    horizontalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {

                    Checkbox(
                        checked = visible,
                        onCheckedChange = { visible = it }
                    )

                    Text(
                        "Für Spieler sichtbar",
                        )
                }
            }

            /* ---------- Hinweis ---------- */

            if (poiCountInCategory > 0) {

                Text(
                    text =
                        "Diese Kategorie enthält " +
                                "$poiCountInCategory POIs " +
                                "und kann nicht gelöscht werden.",

                    style = MaterialTheme.typography.bodySmall,

                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            /* ---------- Speichern ---------- */

            if (canEdit) {

                Button(
                    enabled = title.isNotBlank(),

                    onClick = {

                        onSave(
                            category.copy(
                                title = title,
                                description =
                                    description.takeIf {
                                        it.isNotBlank()
                                    },
                                visible = visible
                            )
                        )

                        onBack()
                    }
                ) {

                    Text(
                        "Speichern",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }

    /* ---------- Delete Confirm ---------- */

    if (showDeleteConfirm) {

        AlertDialog(

            onDismissRequest = {
                showDeleteConfirm = false
            },

            title = {

                Text(
                    "Kategorie löschen?"
                    )
            },

            text = {

                Text(
                    "Möchtest du die Kategorie " +
                            "„${title.ifBlank { "ohne Namen" }}“ " +
                            "wirklich löschen?"
                )
            },

            confirmButton = {

                Button(

                    colors = ButtonDefaults.buttonColors(
                        containerColor =
                            MaterialTheme.colorScheme.error
                    ),

                    onClick = {

                        onDelete(category)

                        showDeleteConfirm = false

                        onBack()
                    }
                ) {

                    Text(
                        "Löschen"
                    )
                }
            },

            dismissButton = {

                OutlinedButton(
                    onClick = {
                        showDeleteConfirm = false
                    }
                ) {

                    Text(
                        "Abbrechen"
                    )
                }
            }
        )
    }

    /* ---------- Delete Blocked ---------- */

    if (showDeleteBlocked) {

        AlertDialog(

            onDismissRequest = {
                showDeleteBlocked = false
            },

            title = {

                Text(
                    "Kategorie nicht leer",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = FontFamily.SansSerif
                    )
                )
            },

            text = {

                Text(
                    "Diese Kategorie enthält noch POIs " +
                            "und kann erst gelöscht werden, " +
                            "wenn alle zugehörigen Orte entfernt " +
                            "oder verschoben wurden.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },

            confirmButton = {

                TextButton(
                    onClick = {
                        showDeleteBlocked = false
                    }
                ) {

                    Text(
                        "OK",
                        fontFamily = FontFamily.SansSerif
                    )
                }
            }
        )
    }
}