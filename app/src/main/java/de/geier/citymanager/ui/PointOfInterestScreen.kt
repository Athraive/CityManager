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
    categoryId: String,
    isGameMaster: Boolean,      // ✅ NEU
    onBack: () -> Unit
) {
    var showCreate by remember { mutableStateOf(false) }

    val pois = remember {
        mutableStateListOf(
            PointOfInterest(
                id = "1",
                name = "Alte Taverne",
                description = "Ein beliebter Treffpunkt für Abenteurer.",
                categoryId = "1",
                type = PoiType.SHOP,
                visible = true
            ),
            PointOfInterest(
                id = "2",
                name = "Geheimer Keller",
                description = "Nicht für Spieler gedacht.",
                categoryId = "1",
                type = PoiType.LOCATION,
                visible = false
            ),
            PointOfInterest(
                id = "3",
                name = "Tempel des Lichts",
                description = "Zentrum des Glaubens.",
                categoryId = "2",
                type = PoiType.LOCATION,
                visible = true
            )
        )
    }

    // ➕ POI anlegen nur für SL
    if (showCreate && isGameMaster) {
        PoiCreateScreen(
            poiType = poiType,
            onSave = { newPoi ->
                pois.add(newPoi.copy(categoryId = categoryId))
                showCreate = false
            },
            onCancel = { showCreate = false }
        )
        return
    }

    val title = when (poiType) {
        PoiType.LOCATION -> "📍 Orte"
        PoiType.SHOP -> "🏪 Geschäfte"
    }

    // 🔥 ZENTRALER FILTER
    val filteredPois =
        pois.filter {
            it.type == poiType &&
                    it.categoryId == categoryId &&
                    (isGameMaster || it.visible)
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

        if (isGameMaster) {
            Button(
                onClick = { showCreate = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("➕ Neu")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredPois) { poi ->
                PoiCard(poi, isGameMaster)
            }
        }
    }
}

@Composable
private fun PoiCard(
    poi: PointOfInterest,
    isGameMaster: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(poi.name, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(poi.description)

            if (isGameMaster && !poi.visible) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("🔒 Nicht sichtbar für Spieler", fontSize = 12.sp)
            }
        }
    }
}
