@file:OptIn(ExperimentalMaterial3Api::class)

package de.geier.citymanager.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.geier.citymanager.ui.AccessContext
import de.geier.citymanager.data.preferences.UserPreferences
import kotlinx.coroutines.launch

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

    TopAppBar(
        title = { Text(title) },

        navigationIcon = {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Zurück"
                    )
                }
            }
        },

        actions = {

            // bestehende Actions (falls genutzt)
            actions(accessContext)

            // Menü-Button
            IconButton(onClick = { menuExpanded = true }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Menü"
                )
            }

            // Dropdown-Menü (korrigiert)
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false }
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "Schriftgröße: ${(fontScale * 100).toInt()}%",
                        style = MaterialTheme.typography.labelMedium
                    )

                    Slider(
                        value = fontScale,
                        onValueChange = { newValue ->
                            scope.launch {
                                userPrefs.setFontScale(newValue)
                            }
                        },
                        valueRange = 0.85f..1.25f
                    )
                }
            }
        }
    )
}