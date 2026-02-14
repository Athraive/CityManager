package de.geier.citymanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.geier.citymanager.data.entity.CityEntity
import de.geier.citymanager.ui.viewmodel.CitySelectViewModel
import de.geier.citymanager.ui.viewmodel.CitySelectViewModelFactory

@Composable
fun CitySelectScreen(
    onCitySelected: (String) -> Unit,
    onCreateCity: () -> Unit
) {
    val context = LocalContext.current

    val viewModel: CitySelectViewModel = viewModel(
        factory = CitySelectViewModelFactory(context)
    )

    val cities by viewModel.cities.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Stadt auswählen",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            items(cities) { city ->
                CityItem(
                    city = city,
                    onEnter = { onCitySelected(city.id) },
                    onDelete = { viewModel.deleteCity(city.id) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = onCreateCity,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("➕ Neue Stadt anlegen")
                }
            }
        }
    }
}

@Composable
private fun CityItem(
    city: CityEntity,
    onEnter: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = city.name,
                style = MaterialTheme.typography.titleMedium
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                TextButton(onClick = onEnter) {
                    Text("Betreten")
                }

                TextButton(onClick = onDelete) {
                    Text("Löschen")
                }
            }
        }
    }
}
