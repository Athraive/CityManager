package de.geier.citymanager.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

/**
 * Scrollbare Bottom-Bar.
 */
@Composable
fun CityBottomBar(
    cityName: String,
    activeTab: CityTab,
    accessContext: AccessContext,
    onTabSelected: (CityTab) -> Unit
) {

    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier.fillMaxWidth(),
        tonalElevation = 3.dp
    ) {

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState)
                    .padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                BottomTabButton(
                    text = "Über $cityName",
                    selected = activeTab == CityTab.CITY,
                    onClick = { onTabSelected(CityTab.CITY) }
                )

                BottomTabButton(
                    text = "Personen",
                    selected = activeTab == CityTab.PERSONS,
                    onClick = { onTabSelected(CityTab.PERSONS) }
                )

                BottomTabButton(
                    text = "POIs",
                    selected = activeTab == CityTab.POIS,
                    onClick = { onTabSelected(CityTab.POIS) }
                )

                BottomTabButton(
                    text = "Fraktionen",
                    selected = activeTab == CityTab.FACTIONS,
                    onClick = { onTabSelected(CityTab.FACTIONS) }
                )
            }

            // 🔹 LINKER PFEIL
            if (scrollState.value > 0) {

                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 4.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.75f),
                            shape = CircleShape
                        )
                        .padding(2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = null,
                        modifier = Modifier.graphicsLayer(alpha = 0.8f),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // 🔹 RECHTER PFEIL
            if (scrollState.value < scrollState.maxValue) {

                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 4.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.75f),
                            shape = CircleShape
                        )
                        .padding(2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = null,
                        modifier = Modifier.graphicsLayer(alpha = 0.8f),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomTabButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    TextButton(
        onClick = onClick
    ) {

        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color =
                if (selected)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}