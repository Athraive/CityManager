package de.geier.citymanager.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun EditSidePanel(
    visible: Boolean,
    tapPosition: androidx.compose.ui.geometry.Offset?,
    onClose: () -> Unit
) {

    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
    val responsiveWidth = screenWidthDp * 0.35f

    // ✅ Dp-safe Berechnung
    val panelWidth = responsiveWidth.coerceIn(280.dp, 400.dp)

    if (visible) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
                .clickable { onClose() }
        )
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(
            initialOffsetX = { -it },
            animationSpec = tween(250)
        ),
        exit = slideOutHorizontally(
            targetOffsetX = { -it },
            animationSpec = tween(250)
        )
    ) {

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(panelWidth)
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {

            Column {

                Text(
                    text = "Neuen Pin hinzufügen",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (tapPosition != null) {
                    Text(
                        text = "Position: %.2f / %.2f"
                            .format(tapPosition.x, tapPosition.y)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(onClick = { }) {
                    Text("Person hinzufügen")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = { }) {
                    Text("POI hinzufügen")
                }
            }
        }
    }
}