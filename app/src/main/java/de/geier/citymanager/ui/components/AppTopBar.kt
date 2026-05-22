@file:OptIn(ExperimentalMaterial3Api::class)

package de.geier.citymanager.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import de.geier.citymanager.data.preferences.UserPreferences
import de.geier.citymanager.ui.AccessContext
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun AppTopBar(
    title: String,
    accessContext: AccessContext,
    onBack: (() -> Unit)? = null,
    actions: @Composable (AccessContext) -> Unit = {}
) {

    val context = LocalContext.current
    val userPrefs = remember { UserPreferences(context) }

    val fontScale by userPrefs.fontScale.collectAsState(initial = 1.0f)

    var menuExpanded by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    val fontSteps = listOf(
        0.5f,
        0.6f,
        0.7f,
        0.8f,
        0.9f,
        1.0f,
        1.1f,
        1.2f,
        1.3f,
        1.4f,
        1.5f
    )

    val currentStepIndex =
        fontSteps.indexOfFirst {
            abs(it - fontScale) < 0.01f
        }.coerceAtLeast(0)

    TopAppBar(

        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall
            )
        },

        navigationIcon = {

            if (onBack != null) {

                IconButton(
                    onClick = onBack
                ) {

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Zurück"
                    )
                }
            }
        },

        actions = {

            // Externe Actions
            actions(accessContext)

            // Menübutton
            IconButton(
                onClick = {
                    menuExpanded = true
                }
            ) {

                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Menü"
                )
            }

            // Dropdown
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = {
                    menuExpanded = false
                }
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "Schriftgröße",
                        style = Typography().bodyMedium
                    )

                    Row(
                        modifier = Modifier.padding(top = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {

                        IconButton(

                            onClick = {

                                val newIndex =
                                    (currentStepIndex - 1)
                                        .coerceAtLeast(0)

                                scope.launch {
                                    userPrefs.setFontScale(
                                        fontSteps[newIndex]
                                    )
                                }
                            }

                        ) {

                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Kleiner"
                            )
                        }

                        Text(
                            text = "${(fontScale * 100).toInt()}%",
                            style = Typography().bodyMedium
                        )

                        IconButton(

                            onClick = {

                                val newIndex =
                                    (currentStepIndex + 1)
                                        .coerceAtMost(fontSteps.lastIndex)

                                scope.launch {
                                    userPrefs.setFontScale(
                                        fontSteps[newIndex]
                                    )
                                }
                            }

                        ) {

                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Größer"
                            )
                        }
                    }
                }
            }
        }
    )
}