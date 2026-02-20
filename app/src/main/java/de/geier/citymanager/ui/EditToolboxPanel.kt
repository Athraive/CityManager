package de.geier.citymanager.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun EditToolboxPanel(
    onPinAdd: () -> Unit,
    onPinDelete: () -> Unit,
    onChangeMap: () -> Unit,
    onClose: () -> Unit
) {

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

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(280.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {

            Column {

                Text(
                    text = "Edit-Toolbox",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = onPinAdd,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Pin anlegen")
                }

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = onPinDelete,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Pin löschen")
                }

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = onChangeMap,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Karte ändern")
                }

                Spacer(Modifier.height(12.dp))

                OutlinedButton(
                    onClick = onClose,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Schließen")
                }
            }
        }
    }
}