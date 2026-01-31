package de.geier.citymanager.ui

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

/**
 * Reine Tab-Bar ohne Navigation.
 * Sichtbarkeit und Verhalten erfolgen ausschließlich
 * über AccessContext.
 */
@Composable
fun CityBottomBar(
    activeTab: CityTab,
    accessContext: AccessContext,
    onTabSelected: (CityTab) -> Unit
) {
    NavigationBar {

        NavigationBarItem(
            selected = activeTab == CityTab.CITY,
            onClick = { onTabSelected(CityTab.CITY) },
            icon = {},
            label = { Text("Über die Stadt") }
        )

        NavigationBarItem(
            selected = activeTab == CityTab.PERSONS,
            onClick = { onTabSelected(CityTab.PERSONS) },
            icon = {},
            label = { Text("Personen") }
        )

        NavigationBarItem(
            selected = activeTab == CityTab.POIS,
            onClick = { onTabSelected(CityTab.POIS) },
            icon = {},
            label = { Text("POIs") }
        )

        if (accessContext.canEdit()) {
            NavigationBarItem(
                selected = activeTab == CityTab.FACTIONS,
                onClick = { onTabSelected(CityTab.FACTIONS) },
                icon = {},
                label = { Text("Fraktionen") }
            )
        }
    }
}
