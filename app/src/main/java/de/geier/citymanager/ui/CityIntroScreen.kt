@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class
)
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material.icons.filled.Edit
import androidx.compose.foundation.clickable

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
    val cards by cityViewModel.cityInfoCards.collectAsState()
    var showTemplateDialog by remember {
        mutableStateOf(false)
    }
    val canEdit = accessContext.canEdit()

    if (city == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val c = city!!

    /* ---------- STATE ---------- */

    var name by remember(c.id) {
        mutableStateOf(c.name)
    }

    var subtitle by remember(c.id) {
        mutableStateOf(c.subtitle)
    }

    var coatOfArmsUri by remember(c.id) {
        mutableStateOf(c.coatOfArmsUri)
    }

    var editingCardId by remember {
        mutableStateOf<String?>(null)
    }

    var editingSubtitle by remember {
        mutableStateOf(false)
    }

    val recommendedTemplates = listOf(
        "Beschreibung",
        "Einwohner",
        "Land",
        "Regierung",
        "Region",
        "Sprache"
    )

    val additionalTemplates = listOf(
        "Bildungsniveau",
        "Handel",
        "Magie",
        "Religion",
        "Verkehr",
        "Verteidigung",
        "Wirtschaft"
    )
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
    if (showTemplateDialog) {

        AlertDialog(
            onDismissRequest = {
                showTemplateDialog = false
            },

            title = {
                Text("Neue Karte")
            },

            text = {

                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Text(
                        "Empfohlen",
                        style = MaterialTheme.typography.titleLarge
                    )

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        recommendedTemplates.forEach { template ->

                            val alreadyExists =
                                cards.any {
                                    it.title.trim().equals(
                                        template.trim(),
                                        ignoreCase = true
                                    )
                                }

                            ElevatedCard(
                                onClick = {
                                    if (!alreadyExists) {
                                        cityViewModel.createCard(template)
                                        showTemplateDialog = false
                                    }
                                }
                            ) {

                                Row(
                                    modifier = Modifier.padding(
                                        horizontal = 16.dp,
                                        vertical = 12.dp
                                    ),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {

                                    if (alreadyExists) {
                                        Text("✓")
                                    }

                                    Text(
                                        text = template,
                                        color =
                                            if (alreadyExists)
                                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                            else
                                                MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }

                    HorizontalDivider()

                    Text(
                        "Weitere Vorlagen",
                        style = MaterialTheme.typography.titleLarge
                    )

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        additionalTemplates.forEach { template ->

                            val alreadyExists =
                                cards.any {
                                    it.title.trim().equals(
                                        template.trim(),
                                        ignoreCase = true
                                    )
                                }

                            ElevatedCard(
                                onClick = {
                                    if (!alreadyExists) {
                                        cityViewModel.createCard(template)
                                        showTemplateDialog = false
                                    }
                                }
                            ) {

                                Row(
                                    modifier = Modifier.padding(
                                        horizontal = 16.dp,
                                        vertical = 12.dp
                                    ),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {

                                    if (alreadyExists) {
                                        Text("✓")
                                    }

                                    Text(
                                        text = template,
                                        color =
                                            if (alreadyExists)
                                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                            else
                                                MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }

                    HorizontalDivider()

                    ElevatedCard(
                        onClick = {
                            cityViewModel.createCard("Neue Karte")
                            showTemplateDialog = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "+ Eigene Karte",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            },

            confirmButton = {}
        )
    }

    /* ---------- UI ---------- */

    Scaffold(
        floatingActionButton = {

            if (canEdit) {
                FloatingActionButton(
                    onClick = {
                        showTemplateDialog = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Karte hinzufügen"
                    )
                }
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            /* ---------- HEADER ---------- */

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {

                /* ---------- Name + Beiname ---------- */

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = c.name,
                        style = MaterialTheme.typography.headlineLarge
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    if (editingSubtitle) {

                        OutlinedTextField(
                            value = subtitle,
                            onValueChange = {
                                subtitle = it
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            label = {
                                Text(
                                    "Beiname",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Button(
                            onClick = {
                                cityViewModel.saveCity(
                                    c.copy(
                                        subtitle = subtitle,
                                        coatOfArmsUri = coatOfArmsUri
                                    )
                                )

                                editingSubtitle = false
                            }
                        ) {
                            Text(
                                text = "Übernehmen",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }

                    } else {

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = if (subtitle.isBlank())
                                    "Beiname hinzufügen"
                                else
                                    subtitle,
                                style = MaterialTheme.typography.titleMedium,
                                color =
                                    if (subtitle.isBlank())
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                    else
                                        MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f)
                            )

                            if (canEdit) {

                                IconButton(
                                    onClick = {
                                        editingSubtitle = true
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Beiname bearbeiten"
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.width(16.dp)
                )

                /* ---------- Wappen ---------- */

                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable(enabled = canEdit) {
                            imagePicker.launch(arrayOf("image/*"))
                        },
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
                            imageVector = Icons.Default.LocationCity,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
            }



            /* ---------- INFO CARDS ---------- */

            HorizontalDivider()


            cards.forEach { card ->

                var title by remember(card.id) {
                    mutableStateOf(card.title)
                }

                var content by remember(card.id) {
                    mutableStateOf(card.content)
                }

                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        if (canEdit && editingCardId == null) {
                            editingCardId = card.id
                        }
                    }
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        if (editingCardId == card.id) {

                            if (card.title == "Neue Karte") {

                                OutlinedTextField(
                                    value = title,
                                    onValueChange = {
                                        title = it
                                    },
                                    label = {
                                        Text("Titel")
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )

                            } else {

                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }

                            OutlinedTextField(
                                value = content,
                                onValueChange = {
                                    content = it
                                },
                                label = {
                                    Text("Inhalt")
                                },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 4
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                Button(
                                    onClick = {

                                        cityViewModel.saveCard(
                                            card.copy(
                                                title = title,
                                                content = content
                                            )
                                        )

                                        editingCardId = null
                                    }
                                ) {
                                    Text("Speichern")
                                }

                                TextButton(
                                    onClick = {
                                        cityViewModel.deleteCard(card)
                                        editingCardId = null
                                    }
                                ) {
                                    Text("Löschen")
                                }
                            }

                        } else {

                            Text(
                                text = card.title,
                                style = MaterialTheme.typography.titleLarge
                            )

                            if (card.content.isNotBlank()) {

                                Text(
                                    text = card.content,
                                    style = MaterialTheme.typography.bodyMedium
                                )

                            } else {

                                Text(
                                    text = "Zum Bearbeiten antippen",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            /* ---------- SAVE ---------- */

            if (canEdit) {
                Button(
                    onClick = {
                        cityViewModel.saveCity(
                            c.copy(
                                subtitle = subtitle,
                                name = name,
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
}

