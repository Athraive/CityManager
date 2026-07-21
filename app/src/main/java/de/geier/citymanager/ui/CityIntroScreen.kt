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
import de.geier.citymanager.ui.components.CityHeaderCard
import de.geier.citymanager.ui.components.EditableCard
import androidx.compose.foundation.layout.Row
import de.geier.citymanager.ui.components.MoveButtons
import de.geier.citymanager.ui.components.CardThumbnail
import de.geier.citymanager.ui.components.ImageViewerDialog
import de.geier.citymanager.ui.components.ThumbnailStyle

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

    var openedImage by remember {
        mutableStateOf<String?>(null)
    }

    var showTemplateDialog by remember {
        mutableStateOf(false)
    }

    val canEdit = accessContext.canEdit()

    if (city == null) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
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

            val newUri = it.toString()
            coatOfArmsUri = newUri

            cityViewModel.saveCity(
                c.copy(
                    subtitle = subtitle,
                    coatOfArmsUri = newUri
                )
            )
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
                .padding(paddingValues)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {


            /* ---------- HEADER ---------- */

            CityHeaderCard(
                cityName = c.name,
                subtitle = subtitle,
                coatOfArmsUri = coatOfArmsUri,
                canEdit = canEdit,

                onSubtitleChange = {
                    subtitle = it
                },

                onSubtitleSave = {
                    cityViewModel.saveCity(
                        c.copy(
                            subtitle = it,
                            name = name,
                            coatOfArmsUri = coatOfArmsUri
                        )
                    )
                },

                onImageClick = {
                    imagePicker.launch(arrayOf("image/*"))
                }
            )

            /* ---------- INFO CARDS ---------- */

            HorizontalDivider()

            val visibleCards =
                if (canEdit) {
                    cards
                } else {
                    cards.filter { it.content.isNotBlank() }
                }


            visibleCards.forEachIndexed { index, card ->

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Top
                ) {

                    EditableCard(
                        modifier = Modifier.weight(1f),
                        title = card.title,
                        subtitle = card.subtitle,
                        content = card.content,
                        titleEditable = card.isCustom,
                        thumbnailWidth = ThumbnailStyle.Square.width + 4.dp,
                        canEdit = canEdit,

                        thumbnail =
                            if (!canEdit && card.imageUri == null) {

                                null

                            } else {

                                {

                                    CardThumbnail(
                                        imageUri = card.imageUri,
                                        canEdit = canEdit,
                                        style = ThumbnailStyle.Square,

                                        onReplace = { uri ->

                                            cityViewModel.saveCard(
                                                card.copy(
                                                    imageUri = uri.toString()
                                                )
                                            )

                                        },

                                        onRemove = {

                                            cityViewModel.saveCard(
                                                card.copy(
                                                    imageUri = null
                                                )
                                            )

                                        },

                                        onOpen = {

                                            if (card.imageUri != null) {
                                                openedImage = card.imageUri
                                            }

                                        }
                                    )

                                }

                            },

                        onSave = { title, subtitle, content ->

                            cityViewModel.saveCard(
                                card.copy(
                                    title = title,
                                    subtitle = subtitle,
                                    content = content
                                )
                            )

                        },

                        onDelete = {
                            cityViewModel.deleteCard(card)
                        },

                        moveButtons = {

                            if (canEdit) {

                                MoveButtons(
                                    canMoveUp = index > 0,
                                    canMoveDown = index < visibleCards.lastIndex,

                                    onMoveUp = {
                                        cityViewModel.moveCardUp(card)
                                    },

                                    onMoveDown = {
                                        cityViewModel.moveCardDown(card)
                                    }
                                )

                            }
                        }
                    )
                }
            }
        }
    }
}

