package de.geier.citymanager.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PointOfInterestScreen(
    poiType: PoiType,
    onBack: () -> Unit
) {
    var showCreate by remember { mutableStateOf(false) }

    // 🔹 Lokale, veränderbare POI-Liste
    val pois = remember {
        mutableStateListOf(
            PointOfInterest(
                id = "1",
                name = "Alte Taverne",
                description = "Ein beliebter Treffpunkt für Abenteurer.",
                categoryId = "tavern",
                type = PoiType.SHOP,
                visible = true
            ),

                    PointOfInterest(
                    id = "2",
            name = "Verlassener Turm",
            description = "Niemand weiß, was dort wirklich passiert ist.",
            categoryId = "public_building",
            type = PoiType.LOCATION,
            visible = true
        )
        )
    }

    // 🔹 Eingabemaske anzeigen
    if (showCreate) {
        PoiCreateScreen(
            poiType = poiType,
            onSave = { newPoi ->
                pois.add(newPoi)
                showCreate = false
            },
            onCancel = {
                showCreate = false
            }
        )
        return
    }

    val title = when (poiType) {
        PoiType.LOCATION -> "📍 Orte"
        PoiType.SHOP -> "🏪 Geschäfte"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "⬅ Zurück",
            modifier = Modifier
                .clickable { onBack() }
                .padding(bottom = 16.dp)
        )

        Text(title, fontSize = 24.sp)

        Spacer(modifier = Modifier.height(12.dp))

        // ➕ Neuer POI
        Button(
            onClick = { showCreate = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("➕ Neu")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                pois.filter { it.type == poiType }
            ) { poi ->
                PoiCard(poi)
            }
        }
    }
}

@Composable
private fun PoiCard(poi: PointOfInterest) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                // später: Detailansicht
            }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(poi.name, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(poi.description)

            if (!poi.visible) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("🔒 Nicht sichtbar für Spieler", fontSize = 12.sp)
            }
        }
    }
}
