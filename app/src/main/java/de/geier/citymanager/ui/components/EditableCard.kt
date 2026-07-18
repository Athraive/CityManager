package de.geier.citymanager.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight

private val ThumbnailWidth = ThumbnailStyle.Landscape.width

@Composable
fun EditableCard(
    title: String,
    subtitle: String,
    content: String,
    canEdit: Boolean,
    onSave: (String, String, String) -> Unit,
    onDelete: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    thumbnail: (@Composable (() -> Unit))? = null,
    moveButtons: (@Composable () -> Unit)? = null
) {
    var editing by remember { mutableStateOf(false) }

    var currentTitle by remember(title) {
        mutableStateOf(title)
    }

    var currentSubtitle by remember(subtitle) {
        mutableStateOf(subtitle)
    }

    var currentContent by remember(content) {
        mutableStateOf(content)
    }

    var expanded by remember(content) {
        mutableStateOf(false)
    }

    var hasOverflow by remember {
        mutableStateOf(false)
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top
    ) {

        ElevatedCard(
            modifier = Modifier.weight(1f),
            onClick = {
                if (canEdit && !editing) {
                    editing = true
                }
            }
        ) {

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    if (thumbnail != null) {
                        Column(
                            modifier = Modifier.width(ThumbnailWidth)
                        ) {
                            thumbnail()
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(ThumbnailStyle.Landscape.height),
                        contentAlignment = Alignment.CenterStart
                    ) {

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Center
                        ) {

                            Text(
                                text = currentTitle,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )

                            if (currentSubtitle.isNotBlank()) {
                                Text(
                                    text = currentSubtitle,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                if (editing) {

                    OutlinedTextField(
                        value = currentSubtitle,
                        onValueChange = { currentSubtitle = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text("Untertitel")
                        },
                        singleLine = true
                    )


                    OutlinedTextField(
                        value = currentContent,
                        onValueChange = {
                            currentContent = it
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
                                editing = false

                                onSave(
                                    currentTitle,
                                    currentSubtitle,
                                    currentContent
                                )
                            }
                        ) {
                            Text(
                                "Speichern",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        if (canEdit && onDelete != null) {
                            TextButton(
                                onClick = {
                                    editing = false
                                    onDelete()
                                }
                            ) {
                                Text(
                                    "Löschen",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }

                } else {

                    if (currentContent.isNotBlank()) {

                        Text(
                            text = currentContent,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = if (expanded) Int.MAX_VALUE else 4,
                            onTextLayout = { layoutResult ->
                                hasOverflow = layoutResult.hasVisualOverflow
                            }
                        )

                        if (hasOverflow || expanded) {
                            TextButton(
                                onClick = {
                                    expanded = !expanded
                                }
                            ) {
                                Text(
                                    text = if (expanded) {
                                        "Weniger anzeigen"
                                    } else {
                                        "Mehr anzeigen"
                                    },
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }

                    } else if (canEdit) {

                        Text(
                            text = "Zum Bearbeiten antippen",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        moveButtons?.invoke()
    }
}