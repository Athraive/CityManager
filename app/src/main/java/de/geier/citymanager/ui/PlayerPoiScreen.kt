package de.geier.citymanager.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import de.geier.citymanager.ui.navigation.Screen
import de.geier.citymanager.ui.viewmodel.CityViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerPoiScreen(
    navController: NavController,
    categoryId: String,
    onBack: () -> Unit
) {
    val cityViewModel: CityViewModel = viewModel()
    val pois by cityViewModel.pois.collectAsState()

    var selectedTab by remember { mutableStateOf(PoiType.LOCATION) }

    LaunchedEffect(categoryId) {
        cityViewModel.loadPoisForCategory(categoryId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("📍 Orte & Geschäfte") },
                navigationIcon = {
                    Text(
                        text = "⬅",
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clickable { onBack() }
                    )
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            // Tabs
            TabRow(
                selectedTabIndex = if (selectedTab == PoiType.LOCATION) 0 else 1
            ) {
                Tab(
                    selected = selectedTab == PoiType.LOCATION,
                    onClick = { selectedTab = PoiType.LOCATION },
                    text = { Text("📍 Orte") }
                )
                Tab(
                    selected = selectedTab == PoiType.SHOP,
                    onClick = { selectedTab = PoiType.SHOP },
                    text = { Text("🏪 Geschäfte") }
                )
            }

            // Liste
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(
                    pois.filter { it.type == selectedTab && it.visible }
                ) { poi ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(
                                    Screen.PlayerPoiDetail.createRoute(poi.id)
                                )
                            }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(poi.name, fontSize = 18.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(poi.description)
                        }
                    }
                }
            }
        }
    }
}
