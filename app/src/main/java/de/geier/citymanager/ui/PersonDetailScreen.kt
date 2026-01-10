package de.geier.citymanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.geier.citymanager.ui.components.AppTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonDetailScreen(
    person: Person,
    factions: List<Faction>, // 🔹 vorbereitet, noch ungenutzt
    viewModel: PersonViewModel,
    onBack: () -> Unit
) {
    var notes by remember(person.id) {
        mutableStateOf(person.sharedNotes)
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = person.name,
                onBack = onBack
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = person.name,
                fontSize = 24.sp
            )

            if (!person.description.isNullOrBlank()) {
                Text(text = person.description)
            }

            Divider()

            Text(
                text = "Notizen",
                style = MaterialTheme.typography.titleMedium
            )

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4,
                label = { Text("Gemeinsame Notizen") }
            )

            Button(
                onClick = {
                    viewModel.save(
                        person.copy(sharedNotes = notes)
                    )
                }
            ) {
                Text("Notizen speichern")
            }
        }
    }
}
