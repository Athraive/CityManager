package de.geier.citymanager.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun EditToolboxPanel(
    onPinAdd: () -> Unit,
    onPinDelete: () -> Unit,
    onChangeMap: () -> Unit,
    onMoveStart: () -> Unit
) {

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val calculatedWidthDp =
        (screenWidthDp * 0.55f).coerceAtMost(420f)

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
            modifier = Modifier
                .fillMaxHeight()
                .width(panelWidth),
            shape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp),
            tonalElevation = 6.dp,
            shadowElevation = 8.dp
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                Text(
                    text = "Edit-Toolbox",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.height(24.dp))

                ToolboxButton("Pin anlegen", onPinAdd)
                Spacer(Modifier.height(12.dp))

                ToolboxButton("Pin löschen", onPinDelete)
                Spacer(Modifier.height(12.dp))

                ToolboxButton("Karte ändern", onChangeMap)
                Spacer(Modifier.height(12.dp))

                ToolboxButton("Pin verschieben", onMoveStart)
            }
        }
    }
}

@Composable
private fun ToolboxButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp)
    ) {
        Text(text)
    }
}