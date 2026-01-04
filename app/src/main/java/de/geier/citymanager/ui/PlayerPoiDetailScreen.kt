package de.geier.citymanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import de.geier.citymanager.ui.components.AppTopBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerPoiDetailScreen(
    poi: PointOfInterest,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = poi.name,
                onBack = onBack
            )

        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = poi.name,
                fontSize = 24.sp
            )

            Text(
                text = poi.description
            )
        }
    }
}
