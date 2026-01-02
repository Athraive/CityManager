package de.geier.citymanager.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import java.util.UUID

@Composable
fun CategoryEditScreen(
    category: PoiCategory?,            // null = neue Kategorie
    onSave: (PoiCategory) -> Unit,
    onCancel: () -> Unit
) {
    val isNew = category == null

    var title by remember { mutableStateOf(category?.title ?: "") }
    var icon by remember { mutableStateOf(category?.icon ?: "📁") }
    var backgroundImageUri by remember {
        mutableStateOf(category?.backgroundImageUri)
    }

    // Image Picker (Galerie)
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        backgroundImageUri = uri?.toString()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // ───── Titel ─────
        Text(
            text = if (isNew) "➕ Neue Kategorie" else "✏ Kategorie bearbeiten",
            style = MaterialTheme.typography.headlineSmall
        )

        // ───── Name ─────
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Name der Kategorie") },
            modifier = Modifier.fillMaxWidth()
        )

        // ───── Icon ─────
        OutlinedTextField(
            value = icon,
            onValueChange = { icon = it },
            label = { Text("Icon (Emoji)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ───── Hintergrundbild ─────
        Text(
            text = "Hintergrundbild",
            style = MaterialTheme.typography.titleMedium
        )

        if (backgroundImageUri != null) {
            Image(
                painter = rememberAsyncImagePainter(backgroundImageUri),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Kein Hintergrundbild gesetzt")
            }
        }

        Button(
            onClick = { imagePicker.launch("image/*") }
        ) {
            Text("Hintergrundbild auswählen")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ───── Aktionen ─────
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                enabled = title.isNotBlank(),
                onClick = {
                    onSave(
                        PoiCategory(
                            id = category?.id ?: UUID.randomUUID().toString(),
                            title = title,
                            icon = icon,
                            backgroundImageUri = backgroundImageUri
                        )
                    )
                }
            ) {
                Text("Speichern")
            }

            OutlinedButton(onClick = onCancel) {
                Text("Abbrechen")
            }
        }
    }
}
