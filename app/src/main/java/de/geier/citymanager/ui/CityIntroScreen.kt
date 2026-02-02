package de.geier.citymanager.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Stadtübersicht – reiner Informationsscreen.
 * Keine Navigation, keine Aktionen.
 */
@Composable
fun CityIntroScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        Text(
            text = "Über die Stadt",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text =
                "Diese Stadt ist ein bedeutender Knotenpunkt von Handel, Politik und Geschichte. " +
                        "Ihre Lage, ihre Viertel und ihre Bewohner prägen das Leben innerhalb der Mauern.",
            style = MaterialTheme.typography.bodyLarge
        )

        // Platz für spätere Fakten:
        // Einwohnerzahl, Klima, Besonderheiten …
    }
}
