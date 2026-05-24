package de.geier.citymanager.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.Alignment

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun EditToolboxPanel(
    onClose: () -> Unit,
    onPinAdd: () -> Unit,
    onPinDelete: () -> Unit,
    onChangeMap: () -> Unit,
    onMoveStart: () -> Unit
) {

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val calculatedWidthDp =
        (screenWidthDp * 0.68f).coerceAtMost(460f)

    val panelWidth: Dp = calculatedWidthDp.dp

    AnimatedVisibility(
        visible = true,
        enter = slideInHorizontally(
            initialOffsetX = { -it },
            animationSpec = tween(250)
        ),
        exit = slideOutHorizontally(
            targetOffsetX = { -it },
            animationSpec = tween(250)
        )
    ) {

        Surface(
            modifier = Modifier.width(panelWidth),
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
            shape = RoundedCornerShape(topEnd = 28.dp, bottomEnd = 28.dp),
            tonalElevation = 4.dp,
            shadowElevation = 6.dp
        ) {

            Column(
                modifier = Modifier
                    .padding(20.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text("Edit-Toolbox")

                    TextButton(
                        onClick = onClose,
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("✕")
                    }
                }

                Spacer(Modifier.height(24.dp))

                ToolboxButton("Pin anlegen", onPinAdd)
                Spacer(Modifier.height(12.dp))

                ToolboxButton("Pin verschieben", onMoveStart)
                Spacer(Modifier.height(12.dp))

                ToolboxButton("Pin löschen", onPinDelete)
                Spacer(Modifier.height(12.dp))

                ToolboxButton(
                    text = "Karte ändern",
                    onClick = onChangeMap,
                    highlight = true
                )
            }
        }
    }
}

@Composable
private fun ToolboxButton(
    text: String,
    onClick: () -> Unit,
    highlight: Boolean = false
) {
    val colors =
        if (highlight) {
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary
            )
        } else {
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        }

    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = colors
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}