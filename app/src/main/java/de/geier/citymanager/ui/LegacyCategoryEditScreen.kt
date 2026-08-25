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
    category: PoiCategory?,   // null = NEU, != null = BEARBEITEN
    onSave: (PoiCategory) -> Unit,
    onCancel: () -> Unit
) {
    // 🔹 explizit merken, ob wir editieren
    val isEditMode = category != null

    var title by remember { mutableStateOf(category?.title ?: "") }
    var icon by remember { mutableStateOf(category?.icon ?: "📁") }
    var backgroundImageUri by remember {
        mutableStateOf(category?.backgroundImageUri)
    }

    // Image Picker
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

        Text(
            text = if (isEditMode) "✏ Kategorie bearbeiten" else "➕ Neue Kategorie",
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Name der Kategorie") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = icon,
            onValueChange = { icon = it },
            label = { Text("Icon (Emoji)") },
            modifier = Modifier.fillMaxWidth()
        )

        Text("Hintergrundbild", style = MaterialTheme.typography.titleMedium)

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

        Button(onClick = { imagePicker.launch("image/*") }) {
            Text("Hintergrundbild auswählen")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

            Button(
                enabled = title.isNotBlank(),
                onClick = {
                    onSave(
                        PoiCategory(
                            // 🔴 DER ENTSCHEIDENDE FIX
                            id = if (isEditMode) {
                                category!!.id          // ✅ ID BEHALTEN
                            } else {
                                UUID.randomUUID().toString() // 🆕 NEU
                            },
                            title = title,
                            icon = icon,
                            backgroundImageUri = backgroundImageUri
                        )
                    )
                }
            ) {
                Text("Speichern",
                    style = MaterialTheme.typography.bodyMedium)
            }

            OutlinedButton(onClick = onCancel) {
                Text("Abbrechen")
            }
        }
    }
}
