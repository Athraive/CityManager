package de.geier.citymanager.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RoleSelectScreen(
    onRoleSelected: (Role) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Rolle wählen",
            style = MaterialTheme.typography.headlineMedium
        )

        Button(onClick = { onRoleSelected(Role.PLAYER) }) {
            Text("Spieler")
        }

        Button(onClick = { onRoleSelected(Role.GAME_MASTER) }) {
            Text("Spielleiter")
        }
    }
}
