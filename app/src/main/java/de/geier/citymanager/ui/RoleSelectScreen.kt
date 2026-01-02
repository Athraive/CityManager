package de.geier.citymanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RoleSelectScreen(
    onPlayerClick: () -> Unit,
    onGameMasterClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Rolle wählen",
            fontSize = 28.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Button(
            onClick = onPlayerClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Text("Spieler", fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onGameMasterClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Text("Spielleiter", fontSize = 20.sp)
        }
    }
}
