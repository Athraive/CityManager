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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryListScreen(
    categories: List<PoiCategory>,
    onCategoryClick: (PoiCategory) -> Unit,
    onAddCategory: () -> Unit,
    onDeleteCategory: (PoiCategory) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("📂 Kategorien") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("⬅")
                    }
                }
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
                        onClick = { onCategoryClick(category) },
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
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            if (category.backgroundImageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(category.backgroundImageUri),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // Klickbarer Inhalt
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { onClick() }
                    .padding(16.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(category.icon, style = MaterialTheme.typography.headlineSmall)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(category.title, style = MaterialTheme.typography.titleMedium)
                    }
                }
            }

            // Löschen-Button
            IconButton(
                onClick = onDelete,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Text("🗑")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CategoryListPreview() {
    CategoryListScreen(
        categories = emptyList(),
        onCategoryClick = {},
        onAddCategory = {},
        onDeleteCategory = {},
        onBack = {}
    )
}
