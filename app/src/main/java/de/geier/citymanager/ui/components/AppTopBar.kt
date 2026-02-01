@file:OptIn(ExperimentalMaterial3Api::class)

package de.geier.citymanager.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import de.geier.citymanager.ui.AccessContext

@Composable
fun AppTopBar(
    title: String,
    accessContext: AccessContext,
    onBack: (() -> Unit)? = null,
    actions: @Composable (AccessContext) -> Unit = {}
) {
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
            actions(accessContext)
        }
    )
}
