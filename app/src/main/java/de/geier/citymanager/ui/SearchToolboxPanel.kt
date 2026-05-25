package de.geier.citymanager.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SearchToolboxPanel(
    visible: Boolean,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    categories: List<PoiCategory>,
    selectedCategoryIds: Set<String>,
    onToggleCategory: (String) -> Unit,
    onClose: () -> Unit
) {

    val screenWidthDp = LocalConfiguration.current.screenWidthDp

    val calculatedWidthDp =
        (screenWidthDp * 0.48f)
            .coerceAtMost(260f)

    val panelWidth: Dp = calculatedWidthDp.dp

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

        Surface(
            modifier = Modifier.width(panelWidth),
            color = MaterialTheme.colorScheme
                .surface
                .copy(alpha = 0.94f),
            shape = RoundedCornerShape(
                topEnd = 28.dp,
                bottomEnd = 28.dp
            ),
            tonalElevation = 4.dp,
            shadowElevation = 6.dp
        ) {

            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "Suche",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    TextButton(
                        onClick = onClose,
                        contentPadding = PaddingValues(0.dp)
                    ) {

                        Text(
                            text = "✕",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = {
                        Text(
                            text = "Suchbegriff",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Kategorien",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                categories
                    .sortedBy { it.title.lowercase() }
                    .forEach { category ->

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            Checkbox(
                                checked =
                                    selectedCategoryIds.contains(category.id),
                                onCheckedChange = {
                                    onToggleCategory(category.id)
                                }
                            )

                            Text(
                                text = category.title,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
            }
        }
    }
}