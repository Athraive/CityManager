package de.geier.citymanager.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameMasterDashboardScreen(
    onBack: () -> Unit,
    onCityDescription: () -> Unit,
    onLocations: () -> Unit,
    onShops: () -> Unit,
    onPeople: () -> Unit,
    onGroups: () -> Unit,
    onNotes: () -> Unit,
    onVisibility: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("🧙 Spielleiter-Dashboard", fontSize = 24.sp)

        DashboardCard("🏙 Stadtbeschreibung", onCityDescription)
        DashboardCard("📍 Orte", onLocations)
        DashboardCard("🏪 Geschäfte", onShops)
        DashboardCard("👤 Personen", onPeople)
        DashboardCard("👥 Gruppierungen", onGroups)
        DashboardCard("📝 Notizen", onNotes)
        DashboardCard("👁 Sichtbarkeit verwalten", onVisibility)

        Spacer(modifier = Modifier.height(16.dp))
        DashboardCard("⬅ Zurück", onBack)
    }
}

@Composable
private fun DashboardCard(
    title: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            modifier = Modifier.padding(16.dp)
        )
    }
}
