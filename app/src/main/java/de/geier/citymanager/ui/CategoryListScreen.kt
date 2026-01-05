package de.geier.citymanager.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import de.geier.citymanager.ui.components.AppTopBar

@Composable
fun CategoryListScreen(
    categories: List<PoiCategory>,
    onCategoryClick: (PoiCategory) -> Unit,   // Öffnen → POIs
    onEditCategory: (PoiCategory) -> Unit,    // Bearbeiten
    onAddCategory: () -> Unit,
    onDeleteCategory: (PoiCategory) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = "📂 Kategorien",
                onBack = onBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddCategory) {
                Text("＋")
            }
        }
    ) { padding ->

        if (categories.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Noch keine Kategorien angelegt")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(categories) { category ->
                    CategoryCard(
                        category = category,
                        onEdit = { onEditCategory(category) },
                        onOpen = { onCategoryClick(category) },
                        onDelete = { onDeleteCategory(category) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryCard(
    category: PoiCategory,
    onEdit: () -> Unit,
    onOpen: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onEdit() }   // 🔹 Klick = Bearbeiten
        ) {

            // Hintergrundbild
            if (category.backgroundImageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(category.backgroundImageUri),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = category.icon,
                            style = MaterialTheme.typography.headlineSmall
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = category.title,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            text = "Öffnen",
                            modifier = Modifier
                                .clickable { onOpen() }
                                .padding(end = 12.dp),
                            style = MaterialTheme.typography.labelLarge
                        )

                        Text(
                            text = "🗑",
                            modifier = Modifier
                                .clickable { onDelete() }
                                .padding(start = 8.dp),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }
    }
}
