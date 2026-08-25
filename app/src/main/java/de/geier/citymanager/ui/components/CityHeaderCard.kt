package de.geier.citymanager.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.Delete
import coil.compose.AsyncImage

@Composable
fun CityHeaderCard(
    cityName: String,
    subtitle: String,
    coatOfArmsUri: String?,
    canEdit: Boolean,
    onSubtitleChange: (String) -> Unit,
    onSubtitleSave: (String) -> Unit,
    onImageClick: () -> Unit,
    onImageRemove: () -> Unit,
    ) {

    var editingSubtitle by remember {
        mutableStateOf(false)
    }

    var currentSubtitle by remember(subtitle) {
        mutableStateOf(subtitle)
    }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        enabled = canEdit && !editingSubtitle
                    ) {
                        editingSubtitle = true
                    }
            ) {

                Text(
                    text = cityName,
                    style = MaterialTheme.typography.headlineLarge
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                if (editingSubtitle) {

                    OutlinedTextField(
                        value = currentSubtitle,
                        onValueChange = {
                            currentSubtitle = it
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
                            editingSubtitle = false
                            onSubtitleChange(currentSubtitle)
                            onSubtitleSave(currentSubtitle)
                        }
                    ) {
                        Text(
                            "Fertig",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                } else {

                    Text(
                        text =
                            if (currentSubtitle.isBlank())
                                "Beiname hinzufügen"
                            else
                                currentSubtitle,
                        style = MaterialTheme.typography.titleMedium,
                        color =
                            if (currentSubtitle.isBlank())
                                MaterialTheme.colorScheme.onSurfaceVariant
                            else
                                MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(
                modifier = Modifier.width(20.dp)
            )

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable {
                        onImageClick()
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

                if (canEdit && coatOfArmsUri != null) {

                    Surface(
                        onClick = {
                            onImageRemove()
                        },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp),
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                        tonalElevation = 2.dp
                    ) {

                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Wappen entfernen",
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}