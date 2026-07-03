package de.geier.citymanager.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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

@Composable
fun EditableCard(
    title: String,
    content: String,
    canEdit: Boolean,
    onSave: (String, String) -> Unit,
    onDelete: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    leadingContent: (@Composable (() -> Unit))? = null,
    moveButtons: (@Composable () -> Unit)? = null
) {

    var editing by remember {
        mutableStateOf(false)
    }

    var currentTitle by remember(title) {
        mutableStateOf(title)
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

            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.Top
            ) {

                if (leadingContent != null) {
                    Column(
                        modifier = Modifier.width(110.dp)
                    ) {
                        leadingContent()
                    }
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    if (editing) {

                        if (title == "Neue Karte") {

                            OutlinedTextField(
                                value = currentTitle,
                                onValueChange = {
                                    currentTitle = it
                                },
                                label = {
                                    Text("Titel")
                                },
                                modifier = Modifier.fillMaxWidth()
                            )

                        } else {

                            Text(
                                text = currentTitle,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }

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

                        Text(
                            text = currentTitle,
                            style = MaterialTheme.typography.titleLarge
                        )

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
                                        if (expanded)
                                            "Weniger anzeigen"
                                        else
                                            "Mehr anzeigen",
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
        }

        moveButtons?.invoke()
    }
}